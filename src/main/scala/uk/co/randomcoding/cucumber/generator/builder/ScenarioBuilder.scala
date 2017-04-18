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

import uk.co.randomcoding.cucumber.generator.gherkin.Scenario
import uk.co.randomcoding.cucumber.generator.gherkin.GherkinComponentIdentifier._
import gherkin.formatter.model.Step

/**
 * Build a [[uk.co.randomcoding.cucumber.generator.gherkin.Scenario]] from incremental input
 *
 * @author RandomCoder
 */
class ScenarioBuilder {

  private var desc = ""
  private var givens = Seq.empty[String]
  private var whens = Seq.empty[String]
  private var thens = Seq.empty[String]
  private var tags = Seq.empty[String]

  def description(des: String) { desc = des }

  def step(step: Step){
    val keyword = step.getKeyword.trim
    val name = step.getName

    val stepText = s"$keyword $name"

    keyword match {
      case GIVEN => givens = givens :+ stepText
      case WHEN => whens = whens :+ stepText
      case THEN => thens = thens :+ stepText
      case AND => (givens.size, whens.size, thens.size) match {
        case (_, 0, 0) => givens = givens :+ stepText
        case (_, _, 0) => whens = whens :+ stepText
        case (_, _, _) => thens = thens :+ stepText
      }
    }
  }

  def tag(tag: String) { tags = tag +: tags }

  def build: Scenario = Scenario(desc, tags, givens, whens, thens)
}
