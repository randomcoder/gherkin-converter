package uk.co.randomcoding.cucumber.generator.reader

import uk.co.randomcoding.cucumber.generator.gherkin.{Examples, ScenarioOutline}
import uk.co.randomcoding.cucumber.generator.{FeatureTestHelpers, FlatSpecTest}

import scala.language.implicitConversions

class CucumberScenarioOutlineReaderSpec extends FlatSpecTest with FeatureTestHelpers {

  behaviour of "A Feature Reader"

  it should "Read a Scenario Outline from a Feature that has a single Scenario" in {
    val feature = FeatureReader.read("/single-scenario-outline.feature")
    feature.scenarios should be(Seq(simpleScenarioOutline))
  }

  it should "Read a Scenario Outline from a Feature that has two simple Scenarios only one of which has tags" in {
    val feature = FeatureReader.read("/basic-feature-outline.feature")
    feature.scenarios should be(Seq(basicScenario1, basicScenario2))
  }

  it should "Read a Scenario Outline from a Feature that has a single Scenario with each step having 'Ands'" in {
    val feature = FeatureReader.read("/single-scenario-outline-with-ands.feature")
    feature.scenarios should be(Seq(simpleScenarioOutlineWithAnds))
  }

  it should "Read a Scenario Outline from a Feature that has a single Scenario with each step having 'Buts'" in {
    val feature = FeatureReader.read("/single-scenario-outline-with-buts.feature")
    feature.scenarios should be(Seq(simpleScenarioOutlineWithButs))
  }

  it should "Read a Scenario Outline from a Feature file that has tags on the scenario and examples" in {
    val feature = FeatureReader.read("/single-scenario-outline-with-tags-on-examples.feature")
    feature.scenarios should be(Seq(simpleScenarioOutlineWithExampleTags))
  }

  private[this] val simpleScenarioOutline = ScenarioOutline("A simple scenario outline that has single line steps", Seq("@scenario-outline-tag-1"),
    Seq("Given a simple precondition <condition>"), Seq("When I do something easy"), Seq("Then I get the result I expected of <result>"),
    Examples(Seq("condition", "result"), Seq(Seq("it's running", "it works")), Nil))

  private[this] val simpleScenarioOutlineWithExampleTags = ScenarioOutline("A simple scenario outline that has tags on the examples",
    Seq("@outline-tag-1"),
    Seq("Given a simple precondition <condition>"), Seq("When I do something easy"), Seq("Then I get the result I expected of <result>"),
    Examples(Seq("condition", "result"), Seq(Seq("it's running", "it works")), Seq("@example-tag-1",  "@example-tag-2")))

  private[this] val simpleScenarioOutlineWithAnds = ScenarioOutline("A simple scenario outline where all steps have an 'and'",
    Seq("@scenario-outline-and-tag-1"),
    Seq("Given a simple precondition <condition 1>", "And another simple precondition <condition 2>"),
    Seq("When I do something easy", "And do something tricky"),
    Seq("Then I get the result I expected of <result>", "And nothing else happens"),
    Examples(Seq("condition 1", "condition 2", "result"), Seq(Seq("test 1", "test 2", "hooray!")), Nil))

  private[this] val simpleScenarioOutlineWithButs = ScenarioOutline("A simple scenario outline where all steps have a 'but'",
    Seq("@scenario-outline-and-tag-2"),
    Seq("Given a simple precondition <condition 1>", "But another simple precondition <condition 2>"),
    Seq("When I do something easy", "But do something tricky"),
    Seq("Then I get the result I expected of <result>", "But nothing else happens"),
    Examples(Seq("condition 1", "condition 2", "result"), Seq(Seq("test 1", "test 2", "hooray!")), Nil))

  private[this] val basicScenario1 = ScenarioOutline("A simple scenario outline that has single line steps", Seq("@scenario-outline-tag-1"),
    Seq("Given a precondition <condition>"), Seq("When I do something"), Seq("Then I get the result I expected of <result>"),
    Examples(Seq("condition", "result"), Seq(Seq("test 1", "result 1")), Nil))

  private[this] val basicScenario2 = ScenarioOutline("Another simple scenario outline that has single line steps", Nil,
    Seq("Given a second precondition <condition 2>"), Seq("When I do something else"), Seq("Then I also get the result I expected of <result 2>"),
    Examples(Seq("condition 2", "result 2"), Seq(Seq("test 2", "result 2")), Nil))
}
