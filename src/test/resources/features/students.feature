Feature: Students

  Scenario: adding student to database returns 201 CREATED response
    Given I create a student with parameters:
      | enrolled     | true                   |
      | number       | 1                      |
      | classes      | math, history, science |
      | name         | Andrew Samii           |
      | dob          | 07/11/1989             |
      | addressLine1 | 1 Street Pl.           |
      | city         | San Diego              |
      | state        | CA                     |
      | zip          | 92131                  |
      | phoneNumber  | 5551234567             |
    When I add the student
    Then the status code should be 201

  Scenario: adding two students with the same ID should throw an error
    Given I create a student with parameters:
      | enrolled     | true                   |
      | number       | 1                      |
      | classes      | math, history, science |
      | name         | Andrew Samii           |
      | dob          | 07/11/1989             |
      | addressLine1 | 1 Street Pl.           |
      | city         | San Diego              |
      | state        | CA                     |
      | zip          | 92131                  |
      | phoneNumber  | 5551234567             |
    When I add the student
    And I add the student expecting an error
    Then the status code should be 400

  Scenario: Getting a student should include all parameters that were POSTed
    Given I create a student with parameters:
      | enrolled     | true                   |
      | number       | 1                      |
      | classes      | math, history, science |
      | name         | Andrew Samii           |
      | dob          | 07/11/1989             |
      | addressLine1 | 1 Street Pl.           |
      | city         | San Diego              |
      | state        | CA                     |
      | zip          | 92131                  |
      | phoneNumber  | 5551234567             |
    When I add the student
    And I get the student
    Then the status code should be 200
    And the response matches the student

  Scenario: Adding a student should increase the student number.
    Given I create a student with parameters:
      | enrolled     | true                   |
      | number       | 1                      |
      | classes      | math, history, science |
      | name         | Andrew Samii           |
      | dob          | 07/11/1989             |
      | addressLine1 | 1 Street Pl.           |
      | city         | San Diego              |
      | state        | CA                     |
      | zip          | 92131                  |
      | phoneNumber  | 5551234567             |
    When I add the student
    And I get the student
    And I store the last response as temp
    And I create a student with parameters:
      | enrolled     | true                   |
      | number       | 1                      |
      | classes      | math, history, science |
      | name         | Andrew Samii           |
      | dob          | 07/11/1989             |
      | addressLine1 | 1 Street Pl.           |
      | city         | San Diego              |
      | state        | CA                     |
      | zip          | 92131                  |
      | phoneNumber  | 5551234567             |
    And I add the student
    And I get the student
    Then the status code should be 200
    And the last response should have a number one higher than the one in temp
    And the student number should be seven characters long

  Scenario: Adding an enrolled student with fewer than three classes throws an error
    Given I create a student with parameters:
      | enrolled     | true                   |
      | number       | 1                      |
      | classes      | math, history          |
      | name         | Andrew Samii           |
      | dob          | 07/11/1989             |
      | addressLine1 | 1 Street Pl.           |
      | city         | San Diego              |
      | state        | CA                     |
      | zip          | 92131                  |
      | phoneNumber  | 5551234567             |
    When I add the student expecting an error
    Then the status code should be 400

  Scenario: Adding an unenrolled student with any classes throws an error
    Given I create a student with parameters:
      | enrolled     | false                  |
      | number       | 1                      |
      | classes      | math, history, science |
      | name         | Andrew Samii           |
      | dob          | 07/11/1989             |
      | addressLine1 | 1 Street Pl.           |
      | city         | San Diego              |
      | state        | CA                     |
      | zip          | 92131                  |
      | phoneNumber  | 5551234567             |
    When I add the student expecting an error
    Then the status code should be 400

  Scenario: Updating a student should update the database
    Given I create a student with parameters:
      | enrolled     | true                   |
      | number       | 1                      |
      | classes      | math, history, science |
      | name         | Andrew Samii           |
      | dob          | 07/11/1989             |
      | addressLine1 | 1 Street Pl.           |
      | city         | San Diego              |
      | state        | CA                     |
      | zip          | 92131                  |
      | phoneNumber  | 5551234567             |
    And I add the student
    And I update the student with parameters:
      | name | Kian Samii |
      | city | Santa Cruz |
    And I get the student
    And the response does not match the student
    And I update the student on the server
    And I get the student
    Then the response matches the student

  Scenario: Updating the student number does not change the number in the database
    Given I create a student with parameters:
      | enrolled     | true                   |
      | number       | 1                      |
      | classes      | math, history, science |
      | name         | Andrew Samii           |
      | dob          | 07/11/1989             |
      | addressLine1 | 1 Street Pl.           |
      | city         | San Diego              |
      | state        | CA                     |
      | zip          | 92131                  |
      | phoneNumber  | 5551234567             |
    And I add the student
    And I update the student with parameters:
      | number | 2 |
    And I get the student
    And I store the last response as temp
    And I update the student on the server
    And I get the student
    And the last response should have a number the same as the one in temp


  Scenario: Deleting the student removes the row from the database
    Given I create a student with parameters:
      | enrolled     | true                   |
      | number       | 1                      |
      | classes      | math, history, science |
      | name         | Andrew Samii           |
      | dob          | 07/11/1989             |
      | addressLine1 | 1 Street Pl.           |
      | city         | San Diego              |
      | state        | CA                     |
      | zip          | 92131                  |
      | phoneNumber  | 5551234567             |
    And I add the student
    And I get the student
    And the response matches the student
    And I delete the student
    And the status code should be 204
    And I get the student
    And the status code should be 404


    Scenario: Not supplying the name does not allow you to add a student
      Given I create a student with parameters:
        | enrolled     | true                   |
        | number       | 1                      |
        | classes      | math, history, science |
        | dob          | 07/11/1989             |
        | addressLine1 | 1 Street Pl.           |
        | city         | San Diego              |
        | state        | CA                     |
        | zip          | 92131                  |
        | phoneNumber  | 5551234567             |
      And I add the student expecting an error
      And the status code should be 400

  Scenario: Supplying a future date does not allow you to add a student
    Given I create a student with parameters:
      | enrolled     | true                   |
      | number       | 1                      |
      | classes      | math, history, science |
      | name         | Andrew Samii           |
      | dob          | 07/11/2017             |
      | addressLine1 | 1 Street Pl.           |
      | city         | San Diego              |
      | state        | CA                     |
      | zip          | 92131                  |
      | phoneNumber  | 5551234567             |
    And I add the student expecting an error
    And the status code should be 400


  Scenario: Supplying an invalid uuid will not allow you to add a student
    Given I create a student with parameters:
      | enrolled     | true                   |
      | number       | 1                      |
      | id           | BAD_UUID               |
      | classes      | math, history, science |
      | name         | Andrew Samii           |
      | dob          | 07/11/1989             |
      | addressLine1 | 1 Street Pl.           |
      | city         | San Diego              |
      | state        | CA                     |
      | zip          | 92131                  |
      | phoneNumber  | 5551234567             |
    And I add the student expecting an error
    And the status code should be 400
