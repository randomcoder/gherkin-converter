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
import uk.co.randomcoding.cucumber.generator.gherkin.GherkinComponentIdentifier._

/**
 * Tests to define and check the capabilities of the feature reader to read a
 * feature, its tags and detail lines
 *
 * @author RandomCoder
 */
class CucumberFeatureReaderTest extends FunTest {

  val simpleDescription = "A Simple feature that is described on a single line"
  val simpleInOrderTo = "test the running of the feature reader"
  val simpleAsA = "person who is writing the code"
  val simpleIWantTo = "test it with simple single line descriptions"

  val singleLineFeatureDescription = s"""Feature: $simpleDescription
    |In order to $simpleInOrderTo
    |As a $simpleAsA
    |I want to $simpleIWantTo""".stripMargin

  val multiLineDescription = """A more complex description of a feature.
  		|It spans two lines""".stripMargin
  val multiLineInOrderTo = """long in order to
    |over two lines""".stripMargin
  val multiLineAsA = """long description of who I am
    |and why I am the one to do this""".stripMargin
  val multiLineIWantTo = """long list of the things I want to be able to do.
    |Thing 1, Thing 2 and Thing 3""".stripMargin

  val multiLineFeatureDescription = s"""Feature: $multiLineDescription
    |In order to $multiLineInOrderTo
    |As a $multiLineAsA
    |I want to $multiLineIWantTo""".stripMargin

  test("A Feature Reader should be able to read the Feature details from a feature that is described all in single lines and ends with a Scenario") {
    Given("a Feature with a single line description")
    val description = s"""$singleLineFeatureDescription
    |
    |$SCENARIO""".stripMargin

    When("the Feature is read")
    val feature = FeatureReader.read(Source.fromString(description))

    Then("the generated Feature class contains the correct description")
    feature.description should be(simpleDescription)
    feature.inOrderTo should be(simpleInOrderTo)
    feature.asA should be(simpleAsA)
    feature.iWantTo should be(simpleIWantTo)
  }

  test("A Feature Reader should be able to read the Feature details from a feature that is described all in multiple lines and ends with a Scenario") {
    Given("a Feature with a multi line description")
    val description = s"""$multiLineFeatureDescription
    |
    |$SCENARIO""".stripMargin

    When("the Feature is read")
    val feature = FeatureReader.read(Source.fromString(description))

    Then("the generated Feature class contains the correct description")
    feature.description should be(multiLineDescription)
    feature.inOrderTo should be(multiLineInOrderTo)
    feature.asA should be(multiLineAsA)
    feature.iWantTo should be(multiLineIWantTo)
  }

  test("A Feature Reader should be able to read the Feature details from a feature that is described all in single lines and ends with a Scenario Outline") {
    Given("a Feature with a single line description")
    val description = s"""$singleLineFeatureDescription
    |
    |$SCENARIO_OUTLINE""".stripMargin

    When("the Feature is read")
    val feature = FeatureReader.read(Source.fromString(description))

    Then("the generated Feature class contains the correct description")
    feature.description should be(simpleDescription)
    feature.inOrderTo should be(simpleInOrderTo)
    feature.asA should be(simpleAsA)
    feature.iWantTo should be(simpleIWantTo)
  }

  test("A Feature Reader should be able to read the Feature details from a feature that is described all in multiple lines and ends with a Scenario Outline") {
    Given("a Feature with a multi line description")
    val description = s"""$multiLineFeatureDescription
    |
    |$SCENARIO_OUTLINE""".stripMargin

    When("the Feature is read")
    val feature = FeatureReader.read(Source.fromString(description))

    Then("the generated Feature class contains the correct description")
    feature.description should be(multiLineDescription)
    feature.inOrderTo should be(multiLineInOrderTo)
    feature.asA should be(multiLineAsA)
    feature.iWantTo should be(multiLineIWantTo)
  }

  test("A Feature Reader should be able to read the Feature details from a feature that is described all in single lines and the first Scenario (or Outline) has a tag") {
    Given("a Feature with a single line description")
    val description = s"""$singleLineFeatureDescription
    |
    |@a-tag""".stripMargin

    When("the Feature is read")
    val feature = FeatureReader.read(Source.fromString(description))

    Then("the generated Feature class contains the correct description")
    feature.description should be(simpleDescription)
    feature.inOrderTo should be(simpleInOrderTo)
    feature.asA should be(simpleAsA)
    feature.iWantTo should be(simpleIWantTo)
  }

  test("A Feature Reader should be able to read the Feature details from a feature that is described all in multiple lines and and the first Scenario (or Outline) has a tag") {
    Given("a Feature with a multi line description")
    val description = s"""$multiLineFeatureDescription
    |
    |@b-tag""".stripMargin

    When("the Feature is read")
    val feature = FeatureReader.read(Source.fromString(description))

    Then("the generated Feature class contains the correct description")
    feature.description should be(multiLineDescription)
    feature.inOrderTo should be(multiLineInOrderTo)
    feature.asA should be(multiLineAsA)
    feature.iWantTo should be(multiLineIWantTo)
  }



  test("A Feature Reader should be able to read a single tag associated to a Feature") {
    Given("a Feature with a single tag")
    val description = s"""@start-tag
    |$singleLineFeatureDescription""".stripMargin

    When("the Feature is read")
    val feature = FeatureReader.read(Source.fromString(description))
    Then("the Feature class has the correct tag")
    feature.tags should be (Seq("@start-tag"))
  }

  test("A Feature Reader should be able to read a multiple tags associated to a Feature") {
    Given("a Feature with a multiple tags")
    val description = s"""@start-tag @feature-tag @final-tag
    |$singleLineFeatureDescription""".stripMargin

    When("the Feature is read")
    val feature = FeatureReader.read(Source.fromString(description))
    Then("the Feature class has the correct tag")
    feature.tags should be (Seq("@start-tag", "@feature-tag", "@final-tag"))
  }

  test("A Feature Reader should be able to read the basic feature details from a file"){
    Given("a simple feature file")
    val file = Source.fromInputStream(getClass.getResourceAsStream("/basic-feature.feature"))

    When("the file is read")
    val feature = FeatureReader.read(file)

    Then("The feature has the expected details and tags")
    feature.description should be ("The Feature Reader should be able to read basic feature files that have simple scenarios in.")
    feature.inOrderTo should be("be able to parse feature details from a file")
    feature.asA should be("person developing the library")
    feature.iWantTo should be("be able to read details from a file")
  }

  test("A Feature Reader should be able to correctly identify the number of different scenarios present under the feature"){
    Given("a feature with two simple scenarios")
    val file = Source.fromInputStream(getClass.getResourceAsStream("/basic-feature.feature"))

    When("the feature is read")
    val feature = FeatureReader.read(file)

    Then("the feature contains 2 scenarios")
    feature.scenarios.size should be(2)
  }

  private[this] implicit def sourceToLines(s: Source): List[String] = s.getLines().toList
}