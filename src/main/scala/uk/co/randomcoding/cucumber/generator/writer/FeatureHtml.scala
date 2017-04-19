package uk.co.randomcoding.cucumber.generator.writer

import uk.co.randomcoding.cucumber.generator.gherkin.{Feature, ScenarioDesc}

import scala.xml.NodeSeq

// TODO: Add ScalaDoc
class FeatureHtml {

}

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
          <!-- Custom stles here -->
        </style>
      </head>
      <body>
        <div class="container feature">
          <div class="row feature-title">
            <div class="col col-sm-8 col-sm-offset-2">{ if(feature.tags.nonEmpty) {
              <div class="row" id="feature_tags">
                <div class="col-sm-12">
                  {feature.tags.mkString(" ")}
                </div>
              </div>}}
              <div class="row">
                <div class="col-sm-2">
                  <strong>Feature</strong>
                </div>
                <div class="col col-sm-6" id="feature_description">
                  {feature.description}
                </div>
              </div>
              <div class="row">
                <div class="col-sm-2">
                  <strong>As a</strong>
                </div>
                <div class="col col-sm-6" id="as_a">
                  {feature.asA}
                </div>
              </div>
              <div class="row">
                <div class="col-sm-2">
                  <strong>I Want</strong>
                </div>
                <div class="col col-sm-6" id="i_want">
                  {feature.iWantTo}
                </div>
              </div>
              <div class="row">
                <div class="col-sm-2">
                  <strong>In order to</strong>
                </div>
                <div class="col col-sm-6" id="in_order_to">
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
    <div class="row">TODO - Stuff here for {scenario.description}
    </div>
  }
}
