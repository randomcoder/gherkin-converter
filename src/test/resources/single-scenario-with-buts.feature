@basic @feature @demo
Feature: The Feature Reader should be able to read basic feature files that have simple scenarios in.
In order to be able to parse feature details from a file
As a person developing the library
I want to be able to read details from a file

  @scenario-and-tag-2
  Scenario: A simple scenario where all steps have a 'but'
    Given a simple precondition
    But another simple precondition
    When I do something easy
    But do something tricky
    Then I get the result I expected
    But nothing else happens
