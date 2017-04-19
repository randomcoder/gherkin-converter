@basic @feature @demo
Feature: The Feature Reader should be able to read basic feature files that have simple scenarios outlines in.
  In order to be able to parse feature details from a file
  As a person developing the library
  I want to be able to read details from a file

  @scenario-outline-and-tag-1
  Scenario Outline: A simple scenario outline where all steps have an 'and'
    Given a simple precondition <condition 1>
    And another simple precondition <condition 2>
    When I do something easy
    And do something tricky
    Then I get the result I expected of <result>
    And nothing else happens

    Examples:
      | condition 1 | condition 2 | result  |
      | test 1      | test 2      | hooray! |
