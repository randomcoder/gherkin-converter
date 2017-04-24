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

package uk.co.randomcoding.cucumber.generator

import java.io.{File, FileWriter}

import scala.xml.{NodeSeq, XML}

package object writer {
  def writeFile(html: NodeSeq, targetFile: File) = {
    val writer = new FileWriter(targetFile)
    writer.write("<!DOCTYPE html>\n")
    XML.write(writer, html.head, "UTF-8", false, null)
    writer.close()
  }
}
