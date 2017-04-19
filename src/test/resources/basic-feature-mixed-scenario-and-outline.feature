@basic @feature @demo
Feature: The Feature Reader should be able to read basic feature files that have simple scenarios in.
  In order to be able to parse feature details from a file
  As a person developing the library
  I want to be able to read details from a file

  @scenario-tag-1
  Scenario: A simple scenario that has single line steps
    Given a precondition
    When I do something
    Then I get the result I expected


  @scenario-outline-tag-1
  Scenario Outline: A simple scenario outline that has single line steps
    Given a precondition <condition>
    When I do something
    Then I get the result I expected of <result>

    Examples:
      | condition | result   |
      | test 1    | result 1 |
