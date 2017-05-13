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
   * Parse the feature details from the file input into a [[Feature]]
   */
  def read(lines: List[String]): Feature = {
    readFeature(lines.map(_.trim), Feature("", "", "", "", Nil, Nil))
  }

  private[this] def readFeature(featureLines: Seq[String], feature: Feature): Feature = {
    featureLines match {
      case Nil => feature
      case featureLine :: rest if featureLine.startsWith(FEATURE) => readFeature(rest, feature.copy(description = tidyLine(featureLine, FEATURE)))
      case asALine :: rest if asALine.startsWith(AS_A) => readFeature(rest, feature.copy(asA = tidyLine(asALine, AS_A)))
      case inOrderToLine :: rest if inOrderToLine.startsWith(IN_ORDER_TO) => readFeature(rest, feature.copy(inOrderTo = tidyLine(inOrderToLine, IN_ORDER_TO)))
      case iWantLine :: rest if iWantLine.startsWith(I_WANT) => readFeature(rest, feature.copy(iWantTo = tidyLine(iWantLine, I_WANT)))
      case featureTagsLine :: rest if featureTagsLine.startsWith("@") && feature.description.isEmpty => {
        readFeature(rest, feature.copy(tags = featureTagsLine.split("""\s+""").toSeq))
      }
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
          val (s, restOfLines) = readScenarioOutline(rest, ScenarioOutline(tidyLine(scenarioLine, SCENARIO_OUTLINE), theTags, Nil, Nil, Nil, Nil))
          readScenarios(restOfLines, scenarios :+ s)
        }
        else {
          val (s, restOfLines) = readScenario(rest, Scenario(tidyLine(scenarioLine, SCENARIO), theTags, Nil, Nil, Nil))
          readScenarios(restOfLines, scenarios :+ s)
        }
      }
      case scenarioLine :: rest if scenarioLine.startsWith(SCENARIO) => {
        val (s, restOfLines) = readScenario(rest, Scenario(tidyLine(scenarioLine, SCENARIO), Nil, Nil, Nil, Nil))
        readScenarios(restOfLines, scenarios :+ s)
      }
      case scenarioOutlineLine :: rest if scenarioOutlineLine.startsWith(SCENARIO_OUTLINE) => {
        val (s, restOfLines) = readScenarioOutline(rest, ScenarioOutline(tidyLine(scenarioOutlineLine, SCENARIO_OUTLINE), Nil, Nil, Nil, Nil, Nil))
        readScenarios(restOfLines, scenarios :+ s)
      }
      case _ :: rest => readScenarios(rest, scenarios)
    }
  }

  private[this] def readScenario(scenarioLines: Seq[String], scenario: Scenario): (Scenario, Seq[String]) = {
    scenarioLines match {
      case Nil => (scenario, Nil)
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
      case exWithTags@("" :: exampleTags :: EXAMPLES :: _) if exampleTags.startsWith("@") => {
        val (newExamples, remaining) = readExamples(exWithTags.tail, Examples(Nil, Nil, Nil))
        readScenarioOutline(remaining, scenarioOutline.copy(examples = scenarioOutline.examples :+ newExamples))
      }
      case exWithoutTags@("" :: EXAMPLES :: _) => {
        val (newExamples, remaining) = readExamples(exWithoutTags.tail, Examples(Nil, Nil, Nil))
        readScenarioOutline(remaining, scenarioOutline.copy(examples = scenarioOutline.examples :+ newExamples))
      }
      case "" :: rest => (scenarioOutline, rest)
      case _ :: rest => readScenarioOutline(rest, scenarioOutline)
    }
  }

  private[this] def readExamples(examplesLines: Seq[String], examples: Examples): (Examples, Seq[String]) = {
    examplesLines match {
      case exampleTags :: EXAMPLES :: rest if exampleTags.startsWith("@") => {
        val tags = exampleTags.split("""\s+""")
        val exampleLines = rest.takeWhile(_.startsWith("|"))
        val examples = exampleLines.map(_.split("""\s*\|\s*""").map(_.trim).filterNot(_.isEmpty).toSeq)
        (Examples(examples.head, examples.tail, tags), rest)
      }
      case EXAMPLES :: rest => {
        val exampleLines = rest.takeWhile(_.startsWith("|"))
        val examples = exampleLines.map(_.split("""\s*\|\s*""").map(_.trim).filterNot(_.isEmpty).toSeq)
        (Examples(examples.head, examples.tail, Nil), rest)
      }
    }
  }

  private[this] def tidyLine(line: String, removePrefix: String) = line.drop(removePrefix.length).trim
}
