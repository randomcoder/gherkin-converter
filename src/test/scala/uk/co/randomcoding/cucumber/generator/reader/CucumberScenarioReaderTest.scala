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

import uk.co.randomcoding.cucumber.generator.gherkin.Scenario
import uk.co.randomcoding.cucumber.generator.{FeatureTestHelpers, FlatSpecTest}

import scala.io.Source

import scala.language.implicitConversions

/**
 * Tests for the correct reading of Scenario information including tags and Example data
 * for Scenario Outlines
 *
 * @author RandomCoder
 */
class CucumberScenarioReaderTest extends FlatSpecTest with FeatureTestHelpers {

  behaviour of "A Feature Reader"

  it should "Read a Scenario from a Feature that has a single Scenario" in {
    val feature = FeatureReader.read("/single-scenario.feature")
    feature.scenarios should be(Seq(simpleScenario))
  }

  it should "Read a Scenario from a Feature that has two simple Scenarios" in {
    val feature = FeatureReader.read("/basic-feature.feature")
    feature.scenarios should be(Seq(basicScenario1, basicScenario2))
  }

  it should "Read a Scenario from a Feature that has a single Scenario with each step having 'Ands'" in {
    val feature = FeatureReader.read("/single-scenario-with-ands.feature")
    feature.scenarios should be(Seq(simpleScenarioWithAnds))
  }

  it should "Read a Scenario from a Feature that has a single Scenario with each step having 'Buts'" in {
    val feature = FeatureReader.read("/single-scenario-with-buts.feature")
    feature.scenarios should be(Seq(simpleScenarioWithButs))
  }

  private[this] implicit def pathToLInes(p: String): Seq[String] = Source.fromInputStream(getClass.getResourceAsStream(p)).getLines().toSeq

  private[this] val simpleScenario = Scenario("A simple scenario that has single line steps", Seq("@scenario-tag-1"),
    Seq("Given a simple precondition"), Seq("When I do something easy"), Seq("Then I get the result I expected"))

  private[this] val simpleScenarioWithAnds = Scenario("A simple scenario where all steps have an 'and'", Seq("@scenario-and-tag-1"),
    Seq("Given a simple precondition", "And another simple precondition"),
    Seq("When I do something easy", "And do something tricky"),
    Seq("Then I get the result I expected", "And nothing else happens"))

  private[this] val simpleScenarioWithButs = Scenario("A simple scenario where all steps have a 'but'", Seq("@scenario-and-tag-2"),
    Seq("Given a simple precondition", "But another simple precondition"),
    Seq("When I do something easy", "But do something tricky"),
    Seq("Then I get the result I expected", "But nothing else happens"))

  private[this] val basicScenario1 = Scenario("A simple scenario that has single line steps", Seq("@scenario-tag-1"),
    Seq("Given a precondition"), Seq("When I do something"), Seq("Then I get the result I expected"))

  private[this] val basicScenario2 = Scenario("Another simple scenario that has single line steps", Seq("@scenario-tag-2"),
    Seq("Given a second precondition"), Seq("When I do something else"), Seq("Then I also get the result I expected"))
}
