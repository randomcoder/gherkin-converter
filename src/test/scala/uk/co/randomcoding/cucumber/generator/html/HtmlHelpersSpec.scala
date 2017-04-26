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

import org.scalacheck.Gen
import org.scalatest.Inspectors.{forAll => forAllIn}
import org.scalatest.StreamlinedXmlNormMethods
import org.scalatest.prop.PropertyChecks
import uk.co.randomcoding.cucumber.generator.FlatSpecTest

class HtmlHelpersSpec extends FlatSpecTest with PropertyChecks with StreamlinedXmlNormMethods {
  markup {
    """
      |# Html Helpers
      |Defined in the `html` package object these provide common functionality for html generation classes
      |
      |## `linkTextFromName`
      |This prettifies a file or directory name such that it can be used as the text for a link or title. This is done by:
      |1. Take all characters up to but not including the first `.`
      |    * This turns `MyFunkyFeature.feature.html` into `MyFunkyFeature`
      |1. Insert a space before each capital letter if it has a lowercase letter after it
      |1. Then insert a space before each capital letter if it has a lowercase letter before it
      |    * `MyFunkyFeature` becomes `My Funky Feature`
      |    * `MyFunkyXMLFeature` becomes `My Funky XML Feature`
      |    * `MyFeatureWithATest` becomes `My Feature With A Test`
      |
      |## `RichHtml`
      |An implicit class that add functionality to html `NodeSeq`s
      |
      |### `divWithId`
      |Returns the `NodeSeq` of the `<div>` that has a specific value for its `id` attribute. If there is no div with that
      |`id` or the `id` exists on a non `<div>` element then it returns an empry result
    """.stripMargin
  }

  behaviour of "linkTextFromName"

  it should "Remove all characters from the first '.' onwards" in {
    forAll(Gen.alphaNumStr) { inputString =>
      linkTextFromName(inputString + ".some.parts.after.dots") should not endWith ".some.parts.after.dots"
    }
  }

  it should "Put a space before each capital letter that has a lower case letter before it" in {
    forAllIn(Map("MyFeature" -> "My Feature", "TheOtherTextEntry" -> "The Other Text Entry")) {
      case (original, expected) => linkTextFromName(original + ".an.extension") should be(expected)
    }
  }

  it should "Put a space before each capital letter if it has a lower case letter before it" in {
    forAllIn(Map("MyXMLFeature" -> "My XML Feature", "TheFeatureOnlyAMotherWouldLove" -> "The Feature Only A Mother Would Love")) {
      case (original, expected) => linkTextFromName(original + ".an.extension") should be(expected)
    }
  }

  behaviour of "RichHtml#divWithId(String)"

  it should "Provide access to a div with a specific id in the html document" in {
    val veryInnerDiv = <div id="very inner">Text in very inner</div>
    val innerDiv = <div id="inner">Inner Text{veryInnerDiv}</div>
    val outerDiv = <div id="outer">Outer Test{innerDiv}</div>

    val html = {
      <html>
        <head>
          <title>test html</title>
        </head>
        <body>
          {outerDiv}
        </body>
      </html>
    }

    html.divWithId("very inner").toString should be(veryInnerDiv.toString)
    html.divWithId("inner").toString should be(innerDiv.toString)
    html.divWithId("outer").toString should be(outerDiv.toString)
  }

  it should "Return an empty result if the html does not contain a div with the given id" in {
    val html = {
      <html>
        <head>
          <title>test html</title>
        </head>
        <body>
          <div id="only_div">The only div</div>
        </body>
      </html>
    }

    html.divWithId("different_id") should be(empty)
  }

  it should "Return an empty result if the html contains an element with the given id that is not a div" in {
    val html = {
      <html>
        <head>
          <title>test html</title>
        </head>
        <body>
          <div id="only_div">The only div</div>
          <p id="para_id">Text in a paragraph</p>
        </body>
      </html>
    }

    html.divWithId("para_id") should be(empty)
  }
}
