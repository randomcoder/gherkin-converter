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

import uk.co.randomcoding.cucumber.generator.gherkin.GherkinComponentIdentifier._

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
    def isScenarioStartLine(line: (String, Int)) = line._1.startsWith(SCENARIO) || line._1.startsWith(SCENARIO_OUTLINE) || line._1.startsWith("@")
    def lineIndex(line: (String, Int)) = line._2

    val indexedLines = lines.zipWithIndex
    val sectionBounds = indexedLines.filter(isScenarioStartLine).map(lineIndex)

    val scenarios = for {
      bound <- sectionBounds.sliding(2, 1).toList
    } yield {
      bound match {
        case start :: end :: Nil => lines.slice(bound(0), bound(1))
        case start :: Nil => lines.drop(start)
      }
    }

    scenarios
  }
}
