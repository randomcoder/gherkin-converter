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
import uk.co.randomcoding.cucumber.generator.FunTest
import uk.co.randomcoding.cucumber.generator.FeatureTestHelpers._
import uk.co.randomcoding.cucumber.generator.gherkin.Scenario

/**
 * Tests for the correct reading of Scenario information including tags and Example data
 * for Scenario Outlines
 *
 * @author RandomCoder
 */
class CucumberScenarioReaderTest extends FunTest {

  private[this] val simpleScenario = Scenario("A simple scenario that has single line steps", Seq("@scenario-tag-1"),
    Seq("Given a simple precondition"), Seq("When I do something easy"), Seq("Then I get the result I expected"))

  private[this] val simpleScenarioWithAnds = Scenario("A simple scenario where all steps have an 'and'", Seq("@scenario-and-tag-1"),
    Seq("Given a simple precondition", "And another simple precondition"),
    Seq("When I do something easy", "And do something tricky"),
    Seq("Then I get the result I expected", "And nothing else happens"))

  private[this] val basicScenario1 = Scenario("A simple scenario that has single line steps", Seq("@scenario-tag-1"),
    Seq("Given a precondition"), Seq("When I do something"), Seq("Then I get the result I expected"))

  private[this] val basicScenario2 = Scenario("Another simple scenario that has single line steps", Seq("@scenario-tag-2"),
    Seq("Given a second precondition"), Seq("When I do something else"), Seq("Then I also get the result I expected"))

  test("A Feature Reader is able to read a Scenario from a Feature that has a single Scenario") {
    Given("a Feature Reader set to read a feature file with a single scenario containing only Given When and Then")
    val source = Source.fromInputStream(getClass.getResourceAsStream("/single-scenario.feature"))
    When("the feature file is parsed")
    val feature = FeatureReader.read(source)
    Then("it contains a single Scenario with the expected properties")
    feature.scenarios should be(Seq(simpleScenario))
  }

  test("A Feature Reader is able to read a Scenario from a Feature that has two simple Scenarios") {
    Given("a Feature Reader set to read a feature file with two simple scenarios containing only Given When and Then")
    val source = Source.fromInputStream(getClass.getResourceAsStream("/basic-feature.feature"))
    When("the feature file is parsed")
    val feature = FeatureReader.read(source)
    Then("it contains a single Scenario with the expected properties")
    feature.scenarios should be(Seq(basicScenario1, basicScenario2))
  }

  test("A Feature Reader is able to read a Scenario from a Feature that has a single Scenario with each step having 'Ands'") {
    Given("a Feature Reader set to read a feature file with a single scenario where each step also has an 'And'")
    val source = Source.fromInputStream(getClass.getResourceAsStream("/single-scenario-with-ands.feature"))
    When("the feature file is parsed")
    val feature = FeatureReader.read(source)
    Then("it contains a single Scenario with the expected properties")
    feature.scenarios should be(Seq(simpleScenarioWithAnds))
  }
}
