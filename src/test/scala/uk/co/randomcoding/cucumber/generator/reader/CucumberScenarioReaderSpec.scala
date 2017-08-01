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

import uk.co.randomcoding.cucumber.generator.gherkin.{Given, Scenario, Then, When}
import uk.co.randomcoding.cucumber.generator.{FeatureTestHelpers, FlatSpecTest}

import scala.io.Source

import scala.language.implicitConversions

/**
  * Tests for the correct reading of Scenario information including tags and Example data
  * for Scenario Outlines
  *
  * @author RandomCoder
  */
class CucumberScenarioReaderSpec extends FlatSpecTest with FeatureTestHelpers {

  behaviour of "A Feature Reader"

  it should "Read a Scenario from a Feature that has a single Scenario" in {
    val feature = FeatureReader.read("/single-scenario.feature")
    feature.scenarios should be(Seq(simpleScenario))
  }

  it should "Read a Scenario from a Feature that has two simple Scenarios only one of which has tags" in {
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

  it should "read a data table from a step within a scenario" in {
    val feature = Source.fromString(
      """|@basic @feature @demo
         |Feature: The Feature Reader should be able to read basic feature files that have simple scenarios in.
         |  In order to be able to parse feature details from a file
         |  As a person developing the library
         |  I want to be able to read details from a file
         |
         |Scenario: A simple scenario that has single line steps and a data table
         |    Given a precondition with data
         |      | head 1   | head 2   |
         |      | value 11 | value 12 |
         |      | value 21 | value 22 |
         |    When I do something
         |    Then I get the result I expected
      """.stripMargin)

    val scenario = Scenario("A simple scenario that has single line steps and a data table", Nil,
      Seq(Given("Given a precondition with data", Some(Seq("| head 1   | head 2   |", "| value 11 | value 12 |", "| value 21 | value 22 |")))),
      Seq(When("When I do something")),
      Seq(Then("Then I get the result I expected")))

    FeatureReader.read(feature).scenarios should be(Seq(scenario))
  }

  it should "read a data on the same line from a step within a scenario" in {
    val feature = Source.fromString(
      """|@basic @feature @demo
         |Feature: The Feature Reader should be able to read basic feature files that have simple scenarios in.
         |  In order to be able to parse feature details from a file
         |  As a person developing the library
         |  I want to be able to read details from a file
         |
         |Scenario: A simple scenario that has single line steps and a data table
         |    Given a precondition with data: value1, value2
         |    When I do something
         |    Then I get the result I expected
      """.stripMargin)

    val scenario = Scenario("A simple scenario that has single line steps and a data table", Nil,
      Seq(Given("Given a precondition with data: value1, value2", None)),
      Seq(When("When I do something")),
      Seq(Then("Then I get the result I expected")))

    FeatureReader.read(feature).scenarios should be(Seq(scenario))
  }

  private[this] val simpleScenario = Scenario("A simple scenario that has single line steps", Seq("@scenario-tag-1"),
    Seq(Given("Given a simple precondition")), Seq(When("When I do something easy")), Seq(Then("Then I get the result I expected")))

  private[this] val simpleScenarioWithAnds = Scenario("A simple scenario where all steps have an 'and'", Seq("@scenario-and-tag-1"),
    Seq(Given("Given a simple precondition"), Given("And another simple precondition")),
    Seq(When("When I do something easy"), When("And do something tricky")),
    Seq(Then("Then I get the result I expected"), Then("And nothing else happens")))

  private[this] val simpleScenarioWithButs = Scenario("A simple scenario where all steps have a 'but'", Seq("@scenario-and-tag-2"),
    Seq(Given("Given a simple precondition"), Given("But another simple precondition")),
    Seq(When("When I do something easy"), When("But do something tricky")),
    Seq(Then("Then I get the result I expected"), Then("But nothing else happens")))

  private[this] val basicScenario1 = Scenario("A simple scenario that has single line steps", Seq("@scenario-tag-1"),
    Seq(Given("Given a precondition")), Seq(When("When I do something")), Seq(Then("Then I get the result I expected")))

  private[this] val basicScenario2 = Scenario("Another simple scenario that has single line steps", Nil,
    Seq(Given("Given a second precondition")), Seq(When("When I do something else")), Seq(Then("Then I also get the result I expected")))
}
