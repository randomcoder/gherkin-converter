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

import uk.co.randomcoding.cucumber.generator.FunTest

/**
 * Tests for the correct reading of Scenario information including tags and Example data
 * for Scenario Outlines
 *
 * @author RandomCoder
 */
class CucumberScenarioReaderTest extends FunTest {

  test("A Scenario Reader should be able to read a Scenario description from a single line") {
    Given("a Scenario with a single line description")

    When("the Scenario is read")

    Then("the generated Scenario class contains the correct description")
    pending
  }

  test("A Scenario Reader should be able to read a Scenario description that spans multiple lines") {
    Given("a Scenario with a single line description")

    When("the Scenario is read")

    Then("the generated Scenario class contains the correct description")
    pending
  }

  test("A Scenario Reader should be able to read a single tag associated to a Scenario") {
    Given("a Scenario with a single tag")
    When("the Scenario is read")
    Then("the Scenario class has the correct tag")
    pending
  }

  test("A Scenario Reader should be able to read a multiple tags associated to a scenario") {
    Given("a Scenario with a multiple tags")
    When("the Scenario is read")
    Then("the Scenario class has the correct tags")
    pending
  }

  test("A Scenario Reader will set a Scenario Outline flag if the Scenario is a Scenario Outline") {
    Given("a Scenario Outline definition")
    When("the Scenario is read")
    Then("the Scenario class will have the 'outline' flas set")
    pending
  }

  test("A Scenario Reader should be able to read the description line for a Scenario Outline if it is on a single line") {
    Given("a Scenario Outline with a single line description")
    When("the Scenario Outline is read")
    Then("the Scenario class has the correct description")
    And("the Scenario class has the 'outline' flag set")
    pending
  }

  test("A Scenario Reader should be able to read the description line for a Scenario Outline if it spans multiple lines") {
    Given("a Scenario Outline with a multi line description")
    When("the Scenario Outline is read")
    Then("the Scenario class has the correct description")
    And("the Scenario class has the 'outline' flag set")
    pending
  }

  test("A Scenario Reader will not contain any example data if the Scenario is not a Scenario Outline if none is specified") {
    Given("a basic Scenario without examples")
    When("the Scenario is read")
    Then("the Scenario class does not have any examples")
    And("the 'outline' flag is 'false'")
    pending
  }

  test("A Scenario Reader will not contain any example data if the Scenario is not a Scenario Outline even if some is specified") {
    Given("a basic Scenario which includes an examples section")
    When("the Scenario is read")
    Then("the Scenario class does not have any examples")
    And("the 'outline' flag is 'false'")
    pending
  }

  test("A Scenario Reader will contain all example data lines if the Scenario is a Scenario Outline with Examples specified") {
    Given("a Scenario Outline which includes an examples section")
    When("the Scenario Outline is read")
    Then("the Scenario class contains the expected examples")
    And("the 'outline' flag is 'true'")
    pending
  }

  test("A Scenario Reader will contain no example data lines if the Scenario is a Scenario Outline without Examples specified") {
    Given("a Scenario Outline which does not have examples section")
    When("the Scenario Outline is read")
    Then("the Scenario class contains no examples")
    And("the 'outline' flag is 'true'")
    pending
  }

}