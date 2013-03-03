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

import uk.co.randomcoding.cucumber.generator.gherkin._
import uk.co.randomcoding.cucumber.generator.gherkin.GherkinComponentIdentifier._

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
  def read(source: Source): Feature = {
    val lines = source.getLines().toSeq.map(_.trim)

    val tags = if (lines(0).trim.startsWith("@")) lines(0).trim().split("[, ]").toSeq else Seq.empty

    val featureLineIndex = lines.indexWhere(_.startsWith(FEATURE))
    val inOrderToLineIndex = lines.indexWhere(_.startsWith(IN_ORDER_TO))
    val asALineIndex = lines.indexWhere(_.startsWith(AS_A))
    val iWantToLineIndex = lines.indexWhere(_.startsWith(I_WANT_TO))
    val scenarioStartLineIndex = lines.indexWhere(line => line.startsWith("@") || line.startsWith(SCENARIO) || line.startsWith(SCENARIO_OUTLINE))

    val featureName = lineSegmentAsString(source.reset(), featureLineIndex, inOrderToLineIndex).drop(FEATURE.length).trim
    val inOrderTo = lineSegmentAsString(source.reset(), inOrderToLineIndex, asALineIndex).drop(IN_ORDER_TO.length).trim
    val asA = lineSegmentAsString(source.reset(), asALineIndex, iWantToLineIndex).drop(AS_A.length).trim
    val iWantTo = lineSegmentAsString(source.reset(), iWantToLineIndex, scenarioStartLineIndex).drop(I_WANT_TO.length).trim
    Feature(featureName, inOrderTo, asA, iWantTo, tags)
  }
}