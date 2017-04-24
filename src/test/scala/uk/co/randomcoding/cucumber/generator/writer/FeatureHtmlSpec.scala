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

import org.scalatest.StreamlinedXmlNormMethods
import uk.co.randomcoding.cucumber.generator.FlatSpecTest
import uk.co.randomcoding.cucumber.generator.gherkin.Feature

import scala.xml.NodeSeq

class FeatureHtmlSpec extends FlatSpecTest with StreamlinedXmlNormMethods {

  behaviour of "Html Feature Writer"

  it should "Create an Html page with the title set to the feature's description" in {
    val feature = Feature("Sample feature for test", "", "", "", Nil, Nil)
    val page = FeatureHtml(feature)
    (page \ "head" \ "title").text should be(s"Feature: ${feature.description}")
  }

  it should "put the feature description into a div with the id 'feature_description'" in {
    val feature = Feature("Sample feature for test", "", "", "", Nil, Nil)
    FeatureHtml(feature).divWithId("feature_description").text.trim should be(s"Feature: ${feature.description}")
  }

  it should "put the As A text into a div with the id 'as_a'" in {
    val feature = Feature("", "", "big old tester", "", Nil, Nil)
    FeatureHtml(feature).divWithId("as_a").text.trim should be(s"As a ${feature.asA}")
  }

  it should "put the I Want text into a div with the id 'i_want'" in {
    val feature = Feature("", "", "", "this to be right", Nil, Nil)
    FeatureHtml(feature).divWithId("i_want").text.trim should be(s"I want ${feature.iWantTo}")
  }

  it should "put the In Order To text into a div with the id 'in_order_to'" in {
    val feature = Feature("", "make nice gherkins", "", "", Nil, Nil)
    FeatureHtml(feature).divWithId("in_order_to").text.trim should be(s"In order to ${feature.inOrderTo}")
  }

  it should "not have a row for the feature tags if the feature has no tags" in {
    val feature = Feature("", "", "", "", Nil, Nil)
    FeatureHtml(feature).divWithId("feature_tags") should be('empty)
  }

  it should "display the feature tags in a div with the id 'feature_tags' if the feature has tags" in {
    val feature = Feature("", "", "", "", Seq("@tag1", "@tag2"), Nil)
    FeatureHtml(feature).divWithId("feature_tags").text.trim should be("@tag1 @tag2")
  }

  implicit class RichHtml(val html: NodeSeq) extends AnyRef {
    def divWithId(id: String) = (html \\ "div").filter(_.attribute("id").exists(_.text == id))
  }
}
