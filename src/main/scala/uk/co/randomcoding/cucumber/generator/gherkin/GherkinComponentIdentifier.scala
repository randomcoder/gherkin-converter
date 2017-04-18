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
package uk.co.randomcoding.cucumber.generator.gherkin

/**
 * Identifiers for the different Gherkin Components
 *
 * @author Tim Sheppard
 */
object GherkinComponentIdentifier {

  val FEATURE = "Feature:"

  val AS_A = "As a"

  val IN_ORDER_TO = "In order to"

  val I_WANT_TO = "I want to"

  val I_WANT = "I want"

  val SCENARIO = "Scenario:"

  val SCENARIO_OUTLINE = "Scenario Outline:"

  val GIVEN = "Given"

  val WHEN = "When"

  val THEN = "Then"

  val AND = "And"

  val BUT = "But"
}
