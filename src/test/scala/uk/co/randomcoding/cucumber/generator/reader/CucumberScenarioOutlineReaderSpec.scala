package uk.co.randomcoding.cucumber.generator.reader

import uk.co.randomcoding.cucumber.generator.gherkin.ScenarioOutline
import uk.co.randomcoding.cucumber.generator.{FeatureTestHelpers, FlatSpecTest}

import scala.language.implicitConversions

class CucumberScenarioOutlineReaderSpec extends FlatSpecTest with FeatureTestHelpers {

  behaviour of "A Feature Reader"

  it should "Read a Scenario from a Feature that has a single Scenario" in {
    val feature = FeatureReader.read("/single-scenario-outline.feature")
    feature.scenarios should be(Seq(simpleScenarioOutline))
  }

//  it should "Read a Scenario from a Feature that has two simple Scenarios only one of which has tags" in {
//    val feature = FeatureReader.read("/basic-feature-outline.feature")
//    feature.scenarios should be(Seq(basicScenario1, basicScenario2))
//  }
//
//  it should "Read a Scenario from a Feature that has a single Scenario with each step having 'Ands'" in {
//    val feature = FeatureReader.read("/single-scenario-outline-with-ands.feature")
//    feature.scenarios should be(Seq(simpleScenarioWithAnds))
//  }
//
//  it should "Read a Scenario from a Feature that has a single Scenario with each step having 'Buts'" in {
//    val feature = FeatureReader.read("/single-scenario-outline-with-buts.feature")
//    feature.scenarios should be(Seq(simpleScenarioWithButs))
//  }

  private[this] val simpleScenarioOutline = ScenarioOutline("A simple scenario outline that has single line steps", Seq("@scenario-outline-tag-1"),
    Seq("Given a simple precondition <condition>"), Seq("When I do something easy"), Seq("Then I get the result I expected of <result>"), Seq(Seq("condition", "result"), Seq("it's running", "it works")))

//  private[this] val simpleScenarioWithAnds = Scenario("A simple scenario outline where all steps have an 'and'", Seq("@scenario-outline-and-tag-1"),
//    Seq("Given a simple precondition", "And another simple precondition"),
//    Seq("When I do something easy", "And do something tricky"),
//    Seq("Then I get the result I expected", "And nothing else happens"))
//
//  private[this] val simpleScenarioWithButs = Scenario("A simple scenario where all steps have a 'but'", Seq("@scenario-and-tag-2"),
//    Seq("Given a simple precondition", "But another simple precondition"),
//    Seq("When I do something easy", "But do something tricky"),
//    Seq("Then I get the result I expected", "But nothing else happens"))
//
//  private[this] val basicScenario1 = Scenario("A simple scenario that has single line steps", Seq("@scenario-tag-1"),
//    Seq("Given a precondition"), Seq("When I do something"), Seq("Then I get the result I expected"))
//
//  private[this] val basicScenario2 = Scenario("Another simple scenario that has single line steps", Nil,
//    Seq("Given a second precondition"), Seq("When I do something else"), Seq("Then I also get the result I expected"))
}
