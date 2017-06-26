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

import java.nio.file.Files

import org.scalatest.Inside
import uk.co.randomcoding.cucumber.generator.{FeatureTestHelpers, FlatSpecTest}

class HtmlFeatureGeneratorSpec extends FlatSpecTest with FeatureTestHelpers with Inside {
  markup {
    """
      |# Html Feature Generator
      |
      |Generates html from feature files and creates indexes for the generated files
      |
      |## `generateFeatures`
      |
      |## `generateIndexes`
      |
    """.stripMargin
  }

  behaviour of "generateFeatures"

  it should "create html for features stored in a subdirectory of the base directory when the base directory is empty" in {
    val baseDir = Files.createTempDirectory("test-features")
    val featuresDir = Files.createDirectory(baseDir.resolve("features"))
    Files.copy(getClass.getResourceAsStream("/basic-feature.feature"), featuresDir.resolve("basic-feature.feature"))

    featuresDir.resolve("basic-feature.feature").toFile should exist
    featuresDir.resolve("basic-feature.feature").toFile should be ('file)

    val targetDir = Files.createTempDirectory("test-feature-html")
    new HtmlFeatureGenerator().generateFeatures(baseDir.toFile, targetDir.toFile)

    targetDir.resolve("features").toFile should exist

    inside(Option(targetDir.resolve("features").toFile.list())) {
      case Some(files) => files should contain only "basic-feature.feature.html"
    }
  }
}
