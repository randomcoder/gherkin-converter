@basic @feature @demo
Feature: The Feature Reader should be able to read basic feature files that have simple scenario outlines in.
In order to be able to parse feature details from a file
As a person developing the library
I want to be able to read details from a file

  @scenario-outline-and-tag-2
  Scenario Outline: A simple scenario outline where all steps have a 'but'
    Given a simple precondition <condition 1>
    But another simple precondition <condition 2>
    When I do something easy
    But do something tricky
    Then I get the result I expected of <result>
    But nothing else happens

  Examples:
  | condition 1 | condition 2 | result  |
  | test 1      | test 2      | hooray! |
