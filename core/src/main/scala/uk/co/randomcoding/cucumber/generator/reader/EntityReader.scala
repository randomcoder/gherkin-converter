/*
 * Copyright (C) 2013 RandomCoder <randomcoder@randomcoding.co.uk>
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
 * Contributors:
 * RandomCoder - initial API and implementation and/or initial documentation
 */
package uk.co.randomcoding.cucumber.generator.reader

import scala.io.Source
import uk.co.randomcoding.cucumber.generator.gherkin.GherkinComponent

/**
 * Interface for a reader type that will generate certain
 * [[uk.co.randomcoding.cucumber.generator.gherkin.GherkinComponent]]s from an input
 * feature or scenario description
 *
 * @author RandomCoder
 */
trait EntityReader[T <: GherkinComponent] {

  /**
   * Generate a [[uk.co.randomcoding.cucumber.generator.gherkin.GherkinComponent]] from the input source
   */
  def read(source: Source): T

  protected def lineSegmentAsString(source: Seq[String], start: Int, end: Int): String = {
    val linesToUse = source.slice(start, end)
    linesToUse.fold("")("%s\n%s".format(_, _).trim)
  }
}