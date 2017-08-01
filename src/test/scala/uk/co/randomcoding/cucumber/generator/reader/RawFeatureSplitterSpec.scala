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

package uk.co.randomcoding.cucumber.generator.reader

import org.scalatest.Inside
import uk.co.randomcoding.cucumber.generator.{FeatureTestHelpers, FlatSpecTest}

import scala.io.Source

class RawFeatureSplitterSpec extends FlatSpecTest with FeatureTestHelpers with Inside {
  markup {
    """
      |# Raw Feature Splitter
      |The role of the splitter is to separate the distinct `Feature`, `Background`, `Scenario` or `Scenario Outline`
      |sections from an input `.feature` file. Each section is returned as a simple list of trimmed lines. No additional
      |processing is done.
      |
      |Its output should be a single `Feature` section followed by an optional `Background` and then each of the
      |`Scenario` and `Scenario Outline` sections in the order they are presented in the original input.
      |
      |The `Feature` section will include any tags, with any or all of the preamble (**In order to**, **I Want**, etc.)
      |as well as any additional descriptive comments that are added.
      |
      |The `Background` will include all the steps (probably `Given`s) but _should not_ have any tags
      |
      |Each `Scenario` or `Scenario Outline` will have any tags associated and all lines that make up the whole
      |`Scenario`/ `Scenario Outline`. Therefore a `Scenario` will stop at the end of the last step definition and a
      |`Scenario Outline` will stop at the end of the last `Example` section.
      |
      |Lines that are commented (start with `#`) are to be dropped
    """.stripMargin
  }

  behaviour of "Raw Feature Splitter"

  it should "read the Feature section as trimmed lines from an input with no tags" in {
    val featureText = Source.fromString(
      """|Feature: The Feature Reader should be able to read basic feature files that have simple scenarios in.
         |  In order to be able to parse feature details from a file
         |  As a person developing the library
         |  I want to be able to read details from a file
      """.stripMargin)

    inside(featureSplitter.split(featureText)) {
      case RawFeature(feature,_, _) => feature should contain inOrderOnly(
        "Feature: The Feature Reader should be able to read basic feature files that have simple scenarios in.",
        "In order to be able to parse feature details from a file",
        "As a person developing the library",
        "I want to be able to read details from a file"
      )
    }
  }

  it should "read the Feature section with tags as trimmed lines from an input with tags" in {
    val featureText = Source.fromString(
      """|@tag1 @tag2
         |Feature: The Feature Reader should be able to read basic feature files that have simple scenarios in.
         |  In order to be able to parse feature details from a file
         |  As a person developing the library
         |  I want to be able to read details from a file
      """.stripMargin)

    inside(featureSplitter.split(featureText)) {
      case RawFeature(feature,_, _) => feature should contain inOrderOnly(
        "@tag1 @tag2",
        "Feature: The Feature Reader should be able to read basic feature files that have simple scenarios in.",
        "In order to be able to parse feature details from a file",
        "As a person developing the library",
        "I want to be able to read details from a file"
      )
    }
  }

  it should "read a background section if it follows the feature section" in {
    val featureText = Source.fromString(
      """|Feature: The Feature Reader should be able to read basic feature files that have simple scenarios in.
         |  In order to be able to parse feature details from a file
         |  As a person developing the library
         |  I want to be able to read details from a file
         |
         |  Background:
         |    Given I want to do something for every test
         |    And I only want to write it once
      """.stripMargin)

    inside(featureSplitter.split(featureText)) {
      case RawFeature(_, background, _) => background should contain inOrderOnly(
        "Given I want to do something for every test",
        "And I only want to write it once"
      )
    }
  }

  it should "read the description section after the feature as part of the feature property, retaining any blank lines between the sections" in {
    val featureText = Source.fromString(
      """|Feature: The Feature Reader should be able to read basic feature files that have simple scenarios in.
         |  In order to be able to parse feature details from a file
         |  As a person developing the library
         |  I want to be able to read details from a file
         |
         |
         |
         |  This is an additional description of the feature adding more details
         |  just in case they are required to understand the purpose of it
      """.stripMargin)

    inside(featureSplitter.split(featureText)) {
      case RawFeature(feature,_, _) => feature should contain theSameElementsInOrderAs Seq(
        "Feature: The Feature Reader should be able to read basic feature files that have simple scenarios in.",
        "In order to be able to parse feature details from a file",
        "As a person developing the library",
        "I want to be able to read details from a file",
        "", "", "",
        "This is an additional description of the feature adding more details",
        "just in case they are required to understand the purpose of it"
      )
    }
  }

  it should "ignore any lines that start with a comment character '#'" in {
    val featureText = Source.fromString(
      """|Feature: The Feature Reader should be able to read basic feature files that have simple scenarios in.
         |  In order to be able to parse feature details from a file
         |  As a person developing the library
         |  # With a comment in the preamble
         |  I want to be able to read details from a file
         |
         |  # This is  a comment line and should be ignored. The following line is an empty comment line
         |  #
         |  This is an additional description of the feature adding more details
         |  # With another comment line inside the description
         |  just in case they are required to understand the purpose of it
      """.stripMargin)

    inside(featureSplitter.split(featureText)) {
      case RawFeature(feature,_, _) => feature should contain theSameElementsInOrderAs Seq(
        "Feature: The Feature Reader should be able to read basic feature files that have simple scenarios in.",
        "In order to be able to parse feature details from a file",
        "As a person developing the library",
        "I want to be able to read details from a file",
        "",
        "This is an additional description of the feature adding more details",
        "just in case they are required to understand the purpose of it"
      )
    }

    it should ""
  }

  def featureSplitter = new RawFeatureSplitter
}
