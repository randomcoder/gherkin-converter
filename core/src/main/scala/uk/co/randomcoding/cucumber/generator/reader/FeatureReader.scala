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


import uk.co.randomcoding.cucumber.generator.gherkin.GherkinComponentIdentifier._
import uk.co.randomcoding.cucumber.generator.gherkin._

/**
 * Reads the Feature level details from a feature description
 *
 * This relies on reading a well formatted Feature file i.e.
 *
 *  - The first line is either a '''Feature:''' or a set of '''@tags'''
 *  - The feature description is ended by the next line being an '''In order to'''
 *  - The detail lines are in the order '''In order to''', '''As a''', '''I want to'''
 *  - The whole feature section has a blank line between it and the start of the first scenario
 *
 * @author RandomCoder
 */
object FeatureReader extends EntityReader[Feature] {

  /**
   * Read the feature details:
   *
   *  - Feature description
   *  - As a line
   *  - I want to line
   *  - In order to line
   *  - Any tags
   */
  def read(lines: Seq[String]): Feature = {
    val tidyLines = lines.map(_.trim)
    val tags = if (tidyLines(0).startsWith("@")) tidyLines(0).split("[, ]").toList else List.empty

    val featureLineIndex = tidyLines.indexWhere(_.startsWith(FEATURE))
    val inOrderToLineIndex = tidyLines.indexWhere(_.startsWith(IN_ORDER_TO))
    val asALineIndex = tidyLines.indexWhere(_.startsWith(AS_A))
    val iWantToLineIndex = tidyLines.indexWhere(_.startsWith(I_WANT_TO))
    val scenarioStartLineIndex = tidyLines.indexWhere(line => line.startsWith("@") || line.startsWith(SCENARIO) || line.startsWith(SCENARIO_OUTLINE), iWantToLineIndex)


    val featureName = lineSegmentAsString(tidyLines, featureLineIndex, inOrderToLineIndex).drop(FEATURE.length).trim
    val inOrderTo = lineSegmentAsString(tidyLines, inOrderToLineIndex, asALineIndex).drop(IN_ORDER_TO.length).trim
    val asA = lineSegmentAsString(tidyLines, asALineIndex, iWantToLineIndex).drop(AS_A.length).trim
    val iWantTo = lineSegmentAsString(tidyLines, iWantToLineIndex, scenarioStartLineIndex).drop(I_WANT_TO.length).trim

    val scenarios = scenarioSections(lines).map(ScenarioReader.read(_))

    Feature(featureName, inOrderTo, asA, iWantTo, tags, Seq(Scenario("Dummy", Seq.empty)))
  }

  private[this] def scenarioSections(lines: Seq[String]): Seq[Seq[String]] = {
    val scenarioBounds = (lines: Seq[String]) => {
      val startLine = lines.indexWhere(line => line.startsWith(SCENARIO) || line.startsWith(SCENARIO_OUTLINE))
      val nextStartLine = lines.indexWhere(line => line.startsWith(SCENARIO) || line.startsWith(SCENARIO_OUTLINE), startLine) match {
        case -1 => lines.length
        case other => other -1
      }
      (startLine, nextStartLine)
    }

    val sectionBounds = lines.zipWithIndex.filter(line => line._1.startsWith(SCENARIO) || line._1.startsWith(SCENARIO_OUTLINE)).map(_._2).sliding(2, 1).toSeq

    sectionBounds.map(bounds => lines.slice(bounds(0), bounds(1)))
  }
}
