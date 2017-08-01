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
import uk.co.randomcoding.cucumber.generator.{FeatureTestHelpers, FlatSpecTest}

import scala.language.implicitConversions

class CucumberFeatureReaderSpec extends FlatSpecTest with FeatureTestHelpers {

  behaviour of "Feature Reader"

  it should "Read the Feature details from a feature that is described all in single lines and ends with a Scenario" in {
    val description = singleLineFeatureDescription ++ List("\n", SCENARIO)

    val feature = FeatureReader.read(description)
    feature.description should be(simpleDescription)
    feature.inOrderTo should be(simpleInOrderTo)
    feature.asA should be(simpleAsA)
    feature.iWantTo should be(simpleIWantTo)
  }

  it should "Read the Feature details from a feature that is described all in single lines and ends with a Scenario Outline" in {
    val description = singleLineFeatureDescription ++ List("\n", SCENARIO_OUTLINE)

    val feature = FeatureReader.read(description)
    feature.description should be(simpleDescription)
    feature.inOrderTo should be(simpleInOrderTo)
    feature.asA should be(simpleAsA)
    feature.iWantTo should be(simpleIWantTo)
  }

  it should "Read the Feature details from a feature that is described all in single lines and the first Scenario (or Outline) has a tag"in {
    val description = singleLineFeatureDescription ++ List("\n", "@a-tag", s"$SCENARIO A Scenario", s"$GIVEN Given", s"$WHEN When", s"$THEN Then")

    val feature = FeatureReader.read(description)
    feature.description should be(simpleDescription)
    feature.inOrderTo should be(simpleInOrderTo)
    feature.asA should be(simpleAsA)
    feature.iWantTo should be(simpleIWantTo)
  }

  it should "Read a single tag associated to a Feature"in {
    val description = List("@start-tag") ++ singleLineFeatureDescription

    val feature = FeatureReader.read(description)
    feature.tags should be (Seq("@start-tag"))
  }

  it should "Read a multiple tags associated to a Feature"in {
    val description = List("@start-tag @feature-tag @final-tag") ++ singleLineFeatureDescription

    val feature = FeatureReader.read(description)
    feature.tags should be (Seq("@start-tag", "@feature-tag", "@final-tag"))
  }

  it should "Read the basic feature details from a file" in {
    val feature = FeatureReader.read("/basic-feature.feature")
    feature.description should be ("The Feature Reader should be able to read basic feature files that have simple scenarios in.")
    feature.inOrderTo should be("be able to parse feature details from a file")
    feature.asA should be("person developing the library")
    feature.iWantTo should be("to be able to read details from a file")
  }

  it should "Correctly identify the number of different scenarios present under the feature" in {
    FeatureReader.read("/basic-feature.feature").scenarios should have size 2
  }

  it should "Read the full feature and scenarios from a feature file with Scenarios and Scenario Outlines" in {
     FeatureReader.read("/basic-feature-mixed-scenario-and-outline.feature") should be (
       Feature("The Feature Reader should be able to read basic feature files that have simple scenarios in.",
       "be able to parse feature details from a file",
       "person developing the library",
       "to be able to read details from a file",
       Seq("@basic",  "@feature", "@demo"),
       Seq(basicScenario1, basicScenarioOutline1))
     )

  }

  private[this] val simpleDescription = "A Simple feature that is described on a single line"
  private[this] val simpleInOrderTo = "test the running of the feature reader"
  private[this] val simpleAsA = "person who is writing the code"
  private[this] val simpleIWantTo = "to test it with simple single line descriptions"

  private[this] val singleLineFeatureDescription = List(s"Feature: $simpleDescription",
                                        s"In order to $simpleInOrderTo",
                                        s"As a $simpleAsA",
                                        s"I want $simpleIWantTo")

  private[this] val basicScenario1 = Scenario("A simple scenario that has single line steps", Seq("@scenario-tag-1"),
    Seq(Given("Given a precondition")), Seq(When("When I do something")), Seq(Then("Then I get the result I expected")))

  private[this] val basicScenarioOutline1 = ScenarioOutline("A simple scenario outline that has single line steps", Seq("@scenario-outline-tag-1"),
    Seq(Given("Given a precondition <condition>")), Seq(When("When I do something")), Seq(Then("Then I get the result I expected of <result>")),
    Seq(Examples(Seq("condition", "result"), Seq(Seq("test 1", "result 1")), Nil)))
}
