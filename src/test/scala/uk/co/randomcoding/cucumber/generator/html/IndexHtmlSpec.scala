/*
 * Copyright (C) 2017. RandomCoder <randomcoder@randomcoding.co.uk>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package uk.co.randomcoding.cucumber.generator.html

import java.nio.file.{Files, Path}

import org.scalacheck.Gen
import org.scalatest.StreamlinedXmlNormMethods
import org.scalatest.prop.PropertyChecks
import uk.co.randomcoding.cucumber.generator.{FileTestHelpers, FlatSpecTest}

import scala.util.Random
import scala.xml.XML

class IndexHtmlSpec extends FlatSpecTest with FileTestHelpers with PropertyChecks with StreamlinedXmlNormMethods {
  markup {
    """
      |# Generation of `index.html` for Feature Files
      |`IndexHtml` will generate `index.html` for a directory tree containing html files created by the `FeatureHtmlGenerator`.
      |
      |It will look for files ending with `.feature.html` and create a link to each. In addition it will create links to
      |the `index.html` for any sub directories that also contain files ending with `.feature.html`. Finally, it will
      |create a link to the parent directory for each subdirectory it processes whether or not there are any feature html
      |files within in.
      |
      |Each link to a feature file displays the name of the feature file with spaces before each capital letter or number
      |that has a lower case letter after it. e.g. `MyFeature1.feature.html` is shown as `My Feature 1`.
      |
      |The display of the links is done as a simple unordered list with the links to the features within a div with the
      |id `feature_links` and the links to the sub directories in a div with the id `sub_directory_links`. If there is
      |a link to the parent directory that is within a div with the id `parent_link`.
    """.stripMargin
  }

  behaviour of "IndexHtml generation"

  it should "create no index file if the target directory is empty" in withTempDir {
    emptyDir => {
      indexGenerator.writeIndexFiles(emptyDir, "")

      emptyDir.listFiles() should be(empty)
    }
  }

  it should "create no index files if the target directory does not contain any files ending with .feature.html or sub directories" in {
    forAll(Gen.nonEmptyContainerOf[Set, String](Gen.alphaLowerStr)) { fileNames =>
      withTempDir {
        dir => {
          val testFileNames = fileNames.map(f => createFile(dir, f + ".html").toFile.getName)
          indexGenerator.writeIndexFiles(dir, "")

          dir.list should contain theSameElementsAs testFileNames
        }
      }
    }
  }

  it should "create one index files if the target directory contains any files ending with .feature.html" in {
    forAll(Gen.chooseNum(1, 20)) { fileCount =>
      withTempDir {
        dir => {
          val fileNames = Seq.fill(fileCount)(Random.alphanumeric.take(10).mkString)
          val testFileNames = fileNames.map(_ + ".feature.html")
          testFileNames.foreach(createFile(dir, _))

          indexGenerator.writeIndexFiles(dir, "")
          val filesInDir = dir.listFiles()
          filesInDir.map(_.getName) should contain theSameElementsAs("index.html" +: testFileNames)
        }
      }
    }
  }

  it should "put html <a> links for all the .feature.html files in the directory with the name of the file in the text" in {
    forAll(Gen.chooseNum(1, 20)) { fileCount =>
      withTempDir {
        dir => {
          val fileNames = Seq.fill(fileCount)(Random.alphanumeric.take(10).mkString)
          val testFileNames = fileNames.map(f => createFile(dir, f + ".feature.html").toFile.getName)
          val expectedLinkText = testFileNames.map(linkTextFromName)
          val expectedLinks = testFileNames.zip(expectedLinkText).map { case (name, text) => <a href={name}>{text}</a> }

          indexGenerator.writeIndexFiles(dir, "")

          val indexHtml = readIndex(dir)
          (indexHtml \\ "a").toList should contain theSameElementsAs expectedLinks
        }
      }
    }
  }

  it should "put the feature file links inside a div with the id 'feature_links'" in withTempDir {
    dir => {
      createFile(dir, "testing.feature.html")
      indexGenerator.writeIndexFiles(dir, "")
      val indexHtml = readIndex(dir)
      val featureLinkDiv = (indexHtml \\ "div").filter(_.attribute("id").exists(_.text == "feature_links"))

      (featureLinkDiv \\ "a") should contain (<a href="testing.feature.html">testing</a>)
    }
  }

  it should "put html <a> links to the index.html for all the subdirectories in the directory with the name of the directory in the text" in {
    forAll(Gen.chooseNum(1, 10)) { subDirCount =>
      withTempDir {
        dir => {
          val subDirNames = List.fill(subDirCount)(Random.alphanumeric.take(10).mkString)
          subDirNames.foreach(subDir => Files.createDirectory(dir.resolve(subDir)))
          val subDirLinks = subDirNames.map(subDir => <a href={subDir + "/index.html"}>{linkTextFromName(subDir)}</a>)
          indexGenerator.writeIndexFiles(dir, "")
          val indexHtml = readIndex(dir)

          (indexHtml \\ "a") should contain theSameElementsAs subDirLinks
        }
      }
    }
  }

  it should "not add an Up link in the index file in the start directory" in withTempDir {
    dir => {
      createFile(dir, "EmptyFeature.feature.html")
      indexGenerator.writeIndexFiles(dir, "")

      val indexHtml = readIndex(dir)
      (indexHtml \\ "a") should not contain <a href="../index.html">Up</a>
    }
  }

  it should "add a Up link in the index file of a sub directory of the base directory" in withTempDir {
    dir => {
      val subDir = Files.createDirectory(dir.resolve("subdir"))
      createFile(subDir, "TestEmpty.feature.html")
      indexGenerator.writeIndexFiles(dir, "")
      val subDirIndexHtml = readIndex(subDir)

      (subDirIndexHtml \\ "a") should contain(<a href="../index.html">Up</a>)
    }
  }

  it should "set the title of the first index.html to the value provided in writeIndexFiles" in withTempDir {
    dir => {
      createFile(dir, "EmptyTest.feature.html")
      indexGenerator.writeIndexFiles(dir, "All the Features")
      (readIndex(dir) \\ "head" \ "title").text should be ("Features: All the Features")
    }
  }

  it should "set the title of the index.html in a subdirectory to its name" in withTempDir {
    dir => {
      val subDir = Files.createDirectory(dir.resolve("TestSubFeatures"))
      createFile(subDir, "TestEmpty.feature.html")
      indexGenerator.writeIndexFiles(dir, "")
      (readIndex(subDir) \\ "head" \ "title").text should be ("Features: Test Sub Features")
    }
  }

  it should "put the title into the page in a div with the id 'page_title'" in withTempDir {
    dir => {
      createFile(dir, "EmptyTest.feature.html")
      indexGenerator.writeIndexFiles(dir, "The Features")
      val pageTitleDiv = (readIndex(dir) \\ "body" \\ "div").filter(_.attribute("id").exists(_.text == "page_title"))
      pageTitleDiv.text.trim should be("The Features")
    }
  }

  private[this] def readIndex(dir: Path) = XML.loadFile(dir.resolve("index.html"))

  private[this] def linkTextFromName(name: String) = {
    name.takeWhile(_ != '.').replaceAll("""([A-Z0-9][a-z0-9])""", """ $1""").trim
  }

  private[this] def indexGenerator = new IndexHtml {}

}
