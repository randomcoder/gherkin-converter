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

package uk.co.randomcoding.cucumber.generator.html

import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files

import uk.co.randomcoding.cucumber.generator.gherkin.{Examples, Feature, ScenarioDesc, ScenarioOutline}
import uk.co.randomcoding.cucumber.generator.reader.FeatureReader
import uk.co.randomcoding.cucumber.generator.writer.writeHtml

import scala.collection.JavaConverters._
import scala.util.Try
import scala.xml.NodeSeq

trait FeatureHtml {

  def generateFeatures(dir: File, baseOutputDir: File, relativeTo: File): Unit = {
    val relativePath = dir.toPath.relativize(relativeTo.toPath).toString
    val targetDir = new File(baseOutputDir, relativePath)

    val dirContents = Try(dir.listFiles.toList).getOrElse(Nil)

    dirContents.partition(_.isDirectory) match {
      case (dirs, files) => {
        writeFeatures(files.filter(_.getName.endsWith(".feature")), targetDir)
        dirs.foreach(generateFeatures(_, baseOutputDir, relativeTo))
      }
    }
  }

  private[this] def writeFeatures(features: Seq[File], outputDir: File) = {
    outputDir.mkdirs()
    features.foreach { featureFile =>
      val html = FeatureHtml(FeatureReader.read(Files.readAllLines(featureFile.toPath, StandardCharsets.UTF_8).asScala.toList))
      val targetFile = new File(outputDir, featureFile.getName + ".html")

      writeHtml(html, targetFile)
    }
  }
}

object FeatureHtml {
  def apply(feature: Feature): NodeSeq = {
    <html lang="en">
      <head>
        <title>Feature: {feature.description}</title>
        {metaTags}
        {jquery}
        {bootstrap}
        {customCss}
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
        case ScenarioOutline(_, _, _, _, _, examples) => examples.map(examplesHtml)
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
}
