package uk.co.randomcoding.cucumber.generator.writer

import uk.co.randomcoding.cucumber.generator.gherkin.{Examples, Feature, ScenarioDesc}

import scala.io.Source
import scala.xml.NodeSeq

object FeatureHtml {
  def apply(feature: Feature): NodeSeq = {
    <html lang="en">
      <head>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <title>Feature: {feature.description}</title>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous"/>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous"/>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>

        <style type="text/css">
          {customCss}
        </style>
      </head>
      <body>
        <div class="container feature">
          <div class="row feature-title">
            <div class="col col-sm-8 col-sm-offset-2">{ if(feature.tags.nonEmpty) {
              <div class="row" id="feature_tags">
                <div class="col-sm-12 tags">
                  {feature.tags.mkString(" ")}
                </div>
              </div>}}
              <div class="row">
                <div class="col-sm-3">
                  <h2>Feature:</h2>
                </div>
                <div class="col col-sm-6 feature_description" id="feature_description">
                  {feature.description}
                </div>
              </div>
              <div class="row">
                <div class="col-sm-3">
                  <h4>As a</h4>
                </div>
                <div class="col col-sm-6 feature_part" id="as_a">
                  {feature.asA}
                </div>
              </div>
              <div class="row">
                <div class="col-sm-3">
                  <h4>I Want</h4>
                </div>
                <div class="col col-sm-6 feature_part" id="i_want">
                  {feature.iWantTo}
                </div>
              </div>
              <div class="row">
                <div class="col-sm-3">
                  <h4>In order to</h4>
                </div>
                <div class="col col-sm-6 feature_part" id="in_order_to">
                  {feature.inOrderTo}
                </div>
              </div>
              { feature.scenarios.zipWithIndex.map{ case (scenario, index) => scenarioHtml(scenario, s"scenario_$index")} }
            </div>
          </div>
        </div>
      </body>
    </html>
  }

  private[this] def scenarioHtml(scenario: ScenarioDesc, elementId: String) = {
    <div class="row">
      <div class="col-sm-3">
        <h3>{scenario.identifier}:</h3>
      </div>
      <div class="col col-sm-6 scenario_description" id="scenario_description">
        {scenario.description}
      </div>
    </div> ++
    {stepsHtml(scenario.givens, "given")} ++
    {stepsHtml(scenario.whens, "when")} ++
    {stepsHtml(scenario.thens, "then")}
  }

  private[this] def stepsHtml(steps: Seq[String], id: String) = {
    steps.flatMap{ step =>
      <div class="row">
        <div class="col col-sm-8 given">
          {step}
        </div>
      </div>
    }
  }

  private[this] def examplesHtml(examples: Examples) = {
    <div class="examples">{ if(examples.tags.nonEmpty) {
      <div class="row" id="example_tags">
        <div class="col-sm-12 tags">
          {examples.tags.mkString(" ")}
        </div>
      </div>}}
      <div class="row">
        <div class="col col-sm-8">
          <h3>Examples:</h3>
        </div>
      </div>
      <div class="row">
        <div class="col col-sm-8">
          <table>
            <tr>{ examples.headings.map(heading => <th>{heading}</th>) }</tr> ++
            { examples.examples.flatMap{ example => <tr>{example.map(ex => <td>{ex}</td>) }</tr> } }
          </table>
        </div>
      </div>
    </div>
  }

  private[this] val customCss = Source.fromInputStream(getClass.getResourceAsStream("/feature_styles.css")).getLines().mkString("\n")
}
