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

package uk.co.randomcoding.cucumber.generator.builder

import java.util

import gherkin.formatter.Formatter
import gherkin.formatter.model._

import scala.collection.JavaConversions._

import uk.co.randomcoding.cucumber.generator.gherkin.{Scenario => GScenario, Feature => GFeature}
import uk.co.randomcoding.cucumber.generator.gherkin.GherkinComponentIdentifier._
import uk.co.randomcoding.cucumber.generator.gherkin.Feature
import com.typesafe.scalalogging.slf4j.Logging


/**
 * This is a formatter to use with the [[gherkin.parser.Parser]] that will generate
 * a [[uk.co.randomcoding.cucumber.generator.gherkin.Feature]] object from the input
 *
 * @author RandomCoder
 */
class FeatureBuilder extends Formatter with Logging {
  private var overallFeature: GFeature = _

  private var scenarios: Seq[GScenario] = Nil

  private var scenarioBuilder: Option[ScenarioBuilder] = None

  override def uri(featureUri: String) {
    /* Empty Method*/
  }

  override def feature(feature: gherkin.formatter.model.Feature) {
    val description = feature.getDescription.split("\n").map(_.trim).filterNot(_.isEmpty)
    val inOrderToStart = description.indexWhere(_.startsWith(IN_ORDER_TO))
    val asAStart = description.indexWhere(_.startsWith(AS_A))
    val iWantToStart = description.indexWhere(_.startsWith(I_WANT_TO))

    val featureName = inOrderToStart match {
      case 0 => feature.getName
      case other => (feature.getName +: description.slice(0, inOrderToStart)).mkString("\n")
    }

    val inOrderToLines = featurePartLines(description.slice(inOrderToStart, asAStart), IN_ORDER_TO)
    val asALines = featurePartLines(description.slice(asAStart, iWantToStart), AS_A)
    val iWantToLines = featurePartLines(description.drop(iWantToStart), I_WANT_TO)
    val tags = feature.getTags.map(_.getName)

    overallFeature = GFeature(featureName, inOrderToLines, asALines, iWantToLines, tags, Nil)
  }

  private def featurePartLines(lines: Seq[String], lineIdentifier: String): String = lines.map(_.trim match {
    case line if line.startsWith(lineIdentifier) => line.drop(lineIdentifier.length)
    case line => line
  }).mkString("\n").trim

  override def background(bGround: Background) {
    //currently not supported
  }

  override def scenario(scenario: gherkin.formatter.model.Scenario) {
    buildCurrentScenario()

    val builder = new ScenarioBuilder
    builder.description(scenario.getName)
    scenario.getTags.map(t => builder.tag(t.getName))
    scenarioBuilder = Some(builder)
  }


  private[this] def buildCurrentScenario() {
    scenarioBuilder match {
      case Some(sBuilder) => scenarios = scenarios :+ sBuilder.build
      case _ => // do nothing
    }
  }

  override def scenarioOutline(scenarioOutline: gherkin.formatter.model.ScenarioOutline) {
    // not implemented yet
  }

  override def examples(examples: Examples) {
    // currently not supported
  }

  override def step(step: Step) {
    scenarioBuilder.get.step(step)
  }

  override def eof() {
    buildCurrentScenario()
  }

  override def syntaxError(p1: String, p2: String, p3: util.List[String], p4: String, p5: Integer) {}

  override def done() {
    // not implemented yet
  }

  override def close() {
    // not implemented yet
  }

  /**
   * @return The generated feature
   */
  def build: Feature = overallFeature.copy(scenarios = scenarios)
}
