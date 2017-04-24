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

package uk.co.randomcoding.cucumber.generator.writer

import uk.co.randomcoding.cucumber.generator.gherkin.{Examples, Feature, ScenarioDesc, ScenarioOutline}

import scala.io.Source
import scala.xml.NodeSeq

object FeatureHtml {
  def apply(feature: Feature): NodeSeq = {
    <html lang="en">
      <head>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <title>Feature: {feature.description}</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous"/>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous"/>
        <script src="https://code.jquery.com/jquery-2.2.4.min.js" crossorigin="anonymous"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>

        <style type="text/css">
          {customCss}
        </style>
      </head>
      <body>
        <div class="container">
          <div class="row">
            <div class="col col-sm-10 col-sm-offset-1 feature">{ if (feature.tags.nonEmpty) {
              <div class="row feature_tags" id="feature_tags">
                <div class="col-sm-12">
                  {feature.tags.mkString(" ")}
                </div>
              </div>}}
              <div class="row">
                <div class="col-sm-12" id="feature_description">
                  <span class="feature_heading">Feature: </span><span class="feature_description">{feature.description}</span>
                </div>
              </div>
              <div class="row">
                <div class="col-sm-12" id="as_a">
                  <span class="feature_part_prefix">As a </span><span class="feature_part">{feature.asA}</span>
                </div>
              </div>
              <div class="row">
                <div class="col-sm-12" id="i_want">
                  <span class="feature_part_prefix">I want </span><span class="feature_part" >{feature.iWantTo}</span>
                </div>
              </div>
              <div class="row">
                <div class="col-sm-12 feature_part_prefix" id="in_order_to">
                  <span class="feature_part_prefix">In order to </span><span class="feature_part">{feature.inOrderTo}</span>
                </div>
              </div>
              { feature.scenarios.zipWithIndex.map { case (scenario, index) => scenarioHtml(scenario, s"scenario_$index") } }
            </div>
          </div>
        </div>
      </body>
    </html>
  }

  private[this] def scenarioHtml(scenario: ScenarioDesc, elementId: String) = {
    <div class="scenario">{ if (scenario.tags.nonEmpty) {
      <div class="row" id="scenario_tags">
        <div class="col-sm-12 scenario_tags">
          {scenario.tags.mkString(" ")}
        </div>
      </div>}}
      <div class="row">
        <div class="col-sm-12" id="scenario_description">
          <span class="scenario_heading">{scenario.identifier}: </span>
          <span class="scenario_description">{scenario.description}</span>
        </div>
      </div>
      {stepsHtml(scenario.givens, "given") ++
      stepsHtml(scenario.whens, "when") ++
      stepsHtml(scenario.thens, "then") ++
      (scenario match {
        case ScenarioOutline(_, _, _, _, _, examples) => examplesHtml(examples)
        case _ => Nil
      })
      }
    </div>
  }

  private[this] def stepsHtml(steps: Seq[String], gwtClass: String) = {
    steps.flatMap { step =>
      <div class="row">
        <div class={s"col col-sm-12 $gwtClass"}>
          {step}
        </div>
      </div>
    }
  }

  private[this] def examplesHtml(examples: Examples) = {
    <div class="examples">{ if (examples.tags.nonEmpty) {
      <div class="row">
        <div class="col-sm-12 examples_tags" id="example_tags">
          {examples.tags.mkString(" ")}
        </div>
      </div>}}
      <div class="row">
        <div class="col col-sm-12 examples_heading">
          Examples:
        </div>
      </div>
      <div class="row">
        <div class="col col-sm-8">
          <table>
            <tr>{ examples.headings.map(heading => <th>{heading}</th>) }</tr>
            { examples.examples.flatMap { example => <tr>{example.map(ex => <td>{ex}</td>) }</tr> } }
          </table>
        </div>
      </div>
    </div>
  }

  private[this] val customCss = Source.fromInputStream(getClass.getResourceAsStream("/feature_styles.css")).getLines().mkString("\n")
}
