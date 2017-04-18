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

import uk.co.randomcoding.cucumber.generator.{FeatureTestHelpers, FlatSpecTest}
import uk.co.randomcoding.cucumber.generator.gherkin.GherkinComponentIdentifier._

import scala.io.Source

import scala.language.implicitConversions

class CucumberFeatureReaderTest extends FlatSpecTest with FeatureTestHelpers {

  behaviour of "Feature Reader"

  it should "Read the Feature details from a feature that is described all in single lines and ends with a Scenario" in {
    val description = s"""$singleLineFeatureDescription
    |
    |$SCENARIO""".stripMargin

    val feature = FeatureReader.read(description)
    feature.description should be(simpleDescription)
    feature.inOrderTo should be(simpleInOrderTo)
    feature.asA should be(simpleAsA)
    feature.iWantTo should be(simpleIWantTo)
  }

  it should "Read the Feature details from a feature that is described all in single lines and ends with a Scenario Outline" in {
    val description = s"""$singleLineFeatureDescription
    |
    |$SCENARIO_OUTLINE""".stripMargin

    val feature = FeatureReader.read(description)
    feature.description should be(simpleDescription)
    feature.inOrderTo should be(simpleInOrderTo)
    feature.asA should be(simpleAsA)
    feature.iWantTo should be(simpleIWantTo)
  }

  it should "Read the Feature details from a feature that is described all in single lines and the first Scenario (or Outline) has a tag"in {
    val description = s"""$singleLineFeatureDescription
    |
    |@a-tag
    |$SCENARIO A Scenario
    |$GIVEN Given
    |$WHEN When
    |$THEN Then""".stripMargin

    val feature = FeatureReader.read(description)
    feature.description should be(simpleDescription)
    feature.inOrderTo should be(simpleInOrderTo)
    feature.asA should be(simpleAsA)
    feature.iWantTo should be(simpleIWantTo)
  }

  it should "Read a single tag associated to a Feature"in {
    val description = s"""@start-tag
    |$singleLineFeatureDescription""".stripMargin

    val feature = FeatureReader.read(description)
    feature.tags should be (Seq("@start-tag"))
  }

  it should "Read a multiple tags associated to a Feature"in {
    val description = s"""@start-tag @feature-tag @final-tag
    |$singleLineFeatureDescription""".stripMargin

    val feature = FeatureReader.read(description)
    feature.tags should be (Seq("@start-tag", "@feature-tag", "@final-tag"))
  }

  it should "Read the basic feature details from a file" in {
    val file = Source.fromInputStream(getClass.getResourceAsStream("/basic-feature.feature"))

    val feature = FeatureReader.read(file)
    feature.description should be ("The Feature Reader should be able to read basic feature files that have simple scenarios in.")
    feature.inOrderTo should be("be able to parse feature details from a file")
    feature.asA should be("person developing the library")
    feature.iWantTo should be("be able to read details from a file")
  }

  it should "Correctly identify the number of different scenarios present under the feature" in {
    val file = Source.fromInputStream(getClass.getResourceAsStream("/basic-feature.feature"))

    val feature = FeatureReader.read(file)
    feature.scenarios should have size 2
  }

  private[this] implicit def stringToLines(s: String): List[String] = Source.fromString(s).getLines().toList

  private[this] val simpleDescription = "A Simple feature that is described on a single line"
  private[this] val simpleInOrderTo = "test the running of the feature reader"
  private[this] val simpleAsA = "person who is writing the code"
  private[this] val simpleIWantTo = "test it with simple single line descriptions"

  private[this] val singleLineFeatureDescription = s"""Feature: $simpleDescription
                                        |In order to $simpleInOrderTo
                                        |As a $simpleAsA
                                        |I want to $simpleIWantTo""".stripMargin
}
