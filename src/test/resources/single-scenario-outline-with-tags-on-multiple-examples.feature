@basic @feature @demo
Feature: The Feature Reader should be able to read basic feature files that have simple scenario outlines with examples tagged in.
  In order to be able to parse feature details from a file
  As a person developing the library
  I want to be able to read details from a file

  @outline-tag-1
  Scenario Outline: A simple scenario outline that has tags on multiple examples
    Given a simple precondition <condition>
    When I do something easy
    Then I get the result I expected of <result>

  @example-tag-1 @example-tag-2
    Examples:
      | condition    | result   |
      | it's running | it works |

  @example-tag-3 @example-tag-4
    Examples:
      | condition        | result         |
      | it's not running | it still works |
