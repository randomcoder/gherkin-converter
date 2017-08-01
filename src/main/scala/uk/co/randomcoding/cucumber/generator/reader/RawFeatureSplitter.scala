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

package uk.co.randomcoding.cucumber.generator.reader

import scala.annotation.tailrec
import scala.io.Source

class RawFeatureSplitter {
  def split(featureFileLines: Source): RawFeature = {
    val trimmedLines = featureFileLines.getLines.map(_.trim)
    val featureSection = trimmedLines.takeWhile(! _.startsWith("Background:")).toList
    val backgroundSection = trimmedLines.takeWhile(_.nonEmpty).toList
    RawFeature(featureSection.tidy, backgroundSection.tidy, Nil)
  }

  implicit class RichStringList(strings: List[String]) {
    def dropTrailingEmptyLines: List[String] = {

      @tailrec
      def dropLastEmpty(in: List[String]): List[String] = {
        in match {
          case Nil => in
          case _ :+ notEmpty if notEmpty.trim.nonEmpty => in
          case remaining :+ _ => dropLastEmpty(remaining)
        }
      }

      dropLastEmpty(strings)
    }

    def dropCommentLines: List[String] = strings.filterNot(_.trim.startsWith("#"))

    def tidy: List[String] = strings.dropCommentLines.dropTrailingEmptyLines
  }
}
