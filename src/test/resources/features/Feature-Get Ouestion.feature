Feature: Fetch question/questions
  Retrieve questions from the question bank

  Scenario: Fetch an existing question by ID
    Given the REST API service "/question/1"
    And the HTTP method is GET
    When make the REST API call without body
    Then the REST API response has status as 200
    And the REST API response contains question data
      | id | question_text                                        | option1 | option2 | option3 | option4   | right_answer | difficulty_level | category |
      | 1  | Which keyword is used to define a constant variable? | static  | final   | const   | immutable | final        | Medium           | JAVA     |

  Scenario: Fetch a question which doesn't exist
    Given the REST API service "/question/1000"
    And the HTTP method is GET
    When make the REST API call without body
    Then the REST API response has status as 404
    And the REST API response contains errorMessage as "Can't find question with questionId 1000."

  Scenario: Fetch a question with invalid questionId
    Given the REST API service "/question/-1"
    And the HTTP method is GET
    When make the REST API call without body
    Then the REST API response has status as 400
    And the REST API response contains errorMessage as "[Question id must be a positive number.]"