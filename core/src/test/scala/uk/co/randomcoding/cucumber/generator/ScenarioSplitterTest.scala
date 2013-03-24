/*
 *
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

package uk.co.randomcoding.cucumber.generator

import sun.java2d.pipe.SpanShapeRenderer.Simple

/**
 * TODO: Brief Description
 *
 * @author RandomCoder
 */
class ScenarioSplitterTest extends FunTest {
  test("A Scenario Splitter can create the correct set of lines from a single input scenario") {
    Given("a single scenario with each element on a single line")
    val scenarioLines = Seq("Scenario: A Simple scenario",
    "Given a simple setup",
    "When the thing is done",
    "Then the results are good")

    When("the splitter is invoked on the lines")
    Then("they are returned unchanged")
    ScenarioSplitter.split(scenarioLines) should be(Seq(scenarioLines))
  }

  test("A Scenario Splitter can create the correct set of lines from a single input scenario with a tag line") {
    Given("a single scenario with each element on a single line")
    val scenarioLines = Seq("@tag-1 @tag-2", "Scenario: A Simple scenario",
    "Given a simple setup",
    "When the thing is done",
    "Then the results are good")

    When("the splitter is invoked on the lines")
    Then("they are returned unchanged")
    ScenarioSplitter.split(scenarioLines) should be(Seq(scenarioLines))
  }
}
