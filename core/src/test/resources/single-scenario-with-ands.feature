@basic @feature @demo
Feature: The Feature Reader should be able to read basic feature files that have simple scenarios in.
In order to be able to parse feature details from a file
As a person developing the library
I want to be able to read details from a file

  @scenario-and-tag-1
  Scenario: A simple scenario where all steps have an 'and'
    Given a simple precondition
    And another simple precondition
    When I do something easy
    And do something tricky
    Then I get the result I expected
    And nothing else happens
