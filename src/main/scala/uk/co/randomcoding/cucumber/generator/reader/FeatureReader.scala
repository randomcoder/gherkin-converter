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
 *   RandomCoder - initial API and implementation and/or initial documentation
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
object FeatureReader {

  /**
   * Parse the feature details from the file input as a sequence of strings
   */
  def read(lines: List[String]): Feature = {
    readFeature(lines.map(_.trim), Feature("", "", "", "", Nil, Nil))
  }

  private[this] def readFeature(featureLines: Seq[String], feature: Feature): Feature = {
    featureLines match {
      case Nil => feature
      case featureLine :: rest if featureLine.startsWith(FEATURE) => readFeature(rest, feature.copy(description = featureLine.drop(FEATURE.length).trim))
      case asALine :: rest if asALine.startsWith(AS_A) => readFeature(rest, feature.copy(asA = asALine.drop(AS_A.length).trim))
      case inOrderToLine :: rest if inOrderToLine.startsWith(IN_ORDER_TO) => readFeature(rest, feature.copy(inOrderTo = inOrderToLine.drop(IN_ORDER_TO.length).trim))
      case iWantToLine :: rest if iWantToLine.startsWith(I_WANT_TO) => readFeature(rest, feature.copy(iWantTo = iWantToLine.drop(I_WANT_TO.length).trim))
      case iWantLine :: rest if iWantLine.startsWith(I_WANT) => readFeature(rest, feature.copy(iWantTo = iWantLine.drop(I_WANT.length).trim))
      case featureTagsLine :: rest if featureTagsLine.startsWith("@") && feature.description.isEmpty => readFeature(rest, feature.copy(tags = featureTagsLine.split("""\s+""").toSeq))
      case scenarioLines if feature.iWantTo.nonEmpty && feature.scenarios.isEmpty => feature.copy(scenarios = readScenarios(scenarioLines, Nil))
      case _ :: rest => readFeature(rest, feature)
    }
  }

  private[this] def readScenarios(scenarioLines: Seq[String], scenarios: Seq[ScenarioDesc]): Seq[ScenarioDesc] = {
    scenarioLines match {
      case Nil => scenarios
      case tagsLine :: scenarioLine :: rest if tagsLine.startsWith("@") => {
        val theTags = tagsLine.split("""\s""")
        if (scenarioLine.startsWith(SCENARIO_OUTLINE)) {
          val (s, restOfLines) = readScenarioOutline(rest, ScenarioOutline(scenarioLine.drop(SCENARIO_OUTLINE.length).trim, theTags, Nil, Nil, Nil, Nil))
          readScenarios(restOfLines, scenarios :+ s)
        }
        else {
          val (s, restOfLines) = readScenario(rest, Scenario(scenarioLine.drop(SCENARIO.length).trim, theTags, Nil, Nil, Nil))
          readScenarios(restOfLines, scenarios :+ s)
        }
      }
      case scenarioLine :: rest if scenarioLine.trim.startsWith(SCENARIO) => {
        val (s, restOfLines) = readScenario(rest, Scenario(scenarioLine.drop(SCENARIO.length).trim, Nil, Nil, Nil, Nil))
        readScenarios(restOfLines, scenarios :+ s)
      }
      case scenarioOutlineLine :: rest if scenarioOutlineLine.trim.startsWith(SCENARIO_OUTLINE) => {
        val (s, restOfLines) = readScenarioOutline(rest, ScenarioOutline(scenarioOutlineLine.drop(SCENARIO_OUTLINE.length).trim, Nil, Nil, Nil, Nil, Nil))
        readScenarios(restOfLines, scenarios :+ s)
      }
      case _ :: rest => readScenarios(rest, scenarios)
    }
  }

  private[this] def readScenario(scenarioLines: Seq[String], scenario: Scenario): (Scenario, Seq[String]) = {
    scenarioLines match {
      case Nil => (scenario, Nil)
      case scenarioLine :: rest if scenarioLine.startsWith(SCENARIO) => readScenario(rest, scenario.copy(description = scenarioLine.drop(SCENARIO.length).trim))
      case givenLine :: rest if givenLine.startsWith(GIVEN) => readScenario(rest, scenario.copy(givens = scenario.givens :+ givenLine))
      case whenLine :: rest if whenLine.startsWith(WHEN) => readScenario(rest, scenario.copy(whens = scenario.whens :+ whenLine))
      case thenLine :: rest if thenLine.startsWith(THEN) => readScenario(rest, scenario.copy(thens = scenario.thens :+ thenLine))
      case andOrButLine :: rest if andOrButLine.startsWith(AND) || andOrButLine.startsWith(BUT) => scenario match {
        case Scenario(_, _, _, Nil, Nil) => readScenario(rest, scenario.copy(givens = scenario.givens :+ andOrButLine))
        case Scenario(_, _, _, _, Nil) => readScenario(rest, scenario.copy(whens = scenario.whens :+ andOrButLine))
        case _ => readScenario(rest, scenario.copy(thens = scenario.thens :+ andOrButLine))
      }
      case "" :: rest => (scenario, rest)
      case _ :: rest => readScenario(rest, scenario)
    }
  }

  private[this] def readScenarioOutline(scenarioLines: Seq[String], scenarioOutline: ScenarioOutline): (ScenarioOutline, Seq[String]) = {
    scenarioLines match {
      case Nil => (scenarioOutline, Nil)
      case givenLine :: rest if givenLine.startsWith(GIVEN) => readScenarioOutline(rest, scenarioOutline.copy(givens = scenarioOutline.givens :+ givenLine))
      case whenLine :: rest if whenLine.startsWith(WHEN) => readScenarioOutline(rest, scenarioOutline.copy(whens = scenarioOutline.whens :+ whenLine))
      case thenLine :: rest if thenLine.startsWith(THEN) => readScenarioOutline(rest, scenarioOutline.copy(thens = scenarioOutline.thens :+ thenLine))
      case andOrButLine :: rest if andOrButLine.startsWith(AND) || andOrButLine.startsWith(BUT) => scenarioOutline match {
        case ScenarioOutline(_, _, _, Nil, Nil, _) => readScenarioOutline(rest, scenarioOutline.copy(givens = scenarioOutline.givens :+ andOrButLine))
        case ScenarioOutline(_, _, _, _, Nil, _) => readScenarioOutline(rest, scenarioOutline.copy(whens = scenarioOutline.whens :+ andOrButLine))
        case _ => readScenarioOutline(rest, scenarioOutline.copy(thens = scenarioOutline.thens :+ andOrButLine))
      }
      case "" :: EXAMPLES :: rest => {
        val exampleLines = rest.takeWhile(_.startsWith("|"))
        val examples = exampleLines.map(_.split("""\s*\|\s*""").map(_.trim).filterNot(_.isEmpty).toSeq)
        readScenarioOutline(rest.drop(exampleLines.length), scenarioOutline.copy(examples = examples))
      }
      case "" :: rest => (scenarioOutline, rest)
      case _ :: rest => readScenarioOutline(rest, scenarioOutline)
    }
  }
}
