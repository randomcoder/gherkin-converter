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

import java.io.File

import uk.co.randomcoding.cucumber.generator.writer._

import scala.xml.NodeSeq

trait IndexHtml {

  def writeIndexFiles(baseDir: File): Unit = writeIndexFiles(baseDir, false)

  private[this] def writeIndexFiles(htmlDir: File, linkToParent: Boolean): Unit = {
    val htmlFeatureFiles = htmlDir.list().filter(_.endsWith(".feature.html"))
    val filesLinks = htmlFeatureFiles.map(file => <li><a href={file}>{file.takeWhile(_ != '.').replaceAll("""([A-Z0-9])""", """ $1""").trim}</a></li>)
    val dirLinks = htmlDir.listFiles().filter(_.isDirectory).map{ dir => <li><a href={dir.getName + "/index.html"}>{dir.getName.capitalize}</a></li> }
    val parentLink = if (linkToParent) <li><a href="../index.html">Up</a></li> else NodeSeq.Empty
    val linksList = <ul>{parentLink ++ filesLinks ++ dirLinks}</ul>

    writeFile(linksList, new File(htmlDir, "index.html"))

    htmlDir.listFiles().filter(_.isDirectory).foreach(writeIndexFiles(_, true))
  }
}
