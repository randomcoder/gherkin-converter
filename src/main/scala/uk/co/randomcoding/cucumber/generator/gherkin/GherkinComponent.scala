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
package uk.co.randomcoding.cucumber.generator.gherkin

/**
 * A generic component that represents a Gherkin Syntax component
 *
 * @author RandomCoder
 */
sealed trait GherkinComponent {
  def identifier: String

  def tags: Seq[String]
}

case class Feature(description: String, inOrderTo: String, asA: String, iWantTo: String, tags: Seq[String], scenarios: Seq[ScenarioDesc]) extends GherkinComponent {
  override val identifier = "Feature"
}

sealed trait ScenarioDesc extends GherkinComponent {
  def description: String
  def tags: Seq[String]
  def givens: Seq[String]
  def whens: Seq[String]
  def thens: Seq[String]
}

case class Scenario(description: String, tags: Seq[String], givens: Seq[String], whens: Seq[String], thens: Seq[String]) extends ScenarioDesc {
  override val identifier = "Scenario"
}

case class ScenarioOutline(description: String, tags: Seq[String], givens: Seq[String], whens: Seq[String], thens: Seq[String], examples: Seq[Examples]) extends ScenarioDesc {
  override val identifier = "Scenario Outline"
}

case class Examples(headings: Seq[String], examples: Seq[Seq[String]], tags: Seq[String]) extends GherkinComponent {
  override val identifier = "Examples"
}
