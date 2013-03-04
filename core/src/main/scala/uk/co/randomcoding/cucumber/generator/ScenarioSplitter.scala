/*
 *
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

package uk.co.randomcoding.cucumber.generator

/**
 * Splits the parts of a file (as lines) into scenario sections
 *
 * @author RandomCoder
 */
object ScenarioSplitter {
  /**
   * Given a set of input lines, get the line ranges that contain whole Scenarios or Scenario Outlines.
   *
   * A scenario is denoted as starting with either a tag line or a '''Scenario:''' or '''Scenario Outline:''' entry.
   * A scenario continues until the next scenario or until the end of the lines.
   *
   * The lines are assumed to '''NOT''' contain any Feature lines.
   *
   * @param lines The raw input lines of the feature file with the feature components removed
   * @return The collections of lines that make up each '''Scenario:''' or '''Scenario Outline:'''
   */
  def split(lines: Seq[String]): Seq[Seq[String]] = {

    // Get the indexes of the starts of each scenario
    Nil
  }
}
