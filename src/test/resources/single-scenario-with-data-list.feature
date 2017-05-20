@basic @feature @demo
Feature: The Feature Reader should be able to read basic feature files that have simple scenarios in.
In order to be able to parse feature details from a file
As a person developing the library
I want to be able to read details from a file

  @scenario-tag-1
  Scenario: A simple scenario that has single line steps with data lists
    Given a simple precondition with a list: item1, item2, item with spaces
    When I do something easy
    Then I get the result I expected has all of: item 1, item 2, item with spaces
