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

package uk.co.randomcoding.cucumber.generator.reader

import uk.co.randomcoding.cucumber.generator.gherkin.Scenario
import uk.co.randomcoding.cucumber.generator.gherkin.GherkinComponentIdentifier._

/**
 * Reads the details from a sequence of lines that is a single Scenario.
 * This '''DOES NOT''' process ''Scenario Outline''s
 *
 * @author RandomCoder
 */
class ScenarioReader extends EntityReader[Scenario] {
  def read(lines: Seq[String]): Scenario = {
    val (tags, description) = if (lines(0).startsWith("@")) {
      (lines(0).trim.split("[, ]").toList, lines(1).trim.drop(SCENARIO.length))
    } else (List.empty, lines(0).trim.drop(SCENARIO.length))


    Scenario(description, tags)
  }
}
