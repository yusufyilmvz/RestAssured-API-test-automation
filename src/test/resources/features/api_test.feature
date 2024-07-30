Feature: API Test

#  Scenario: Verify GET request response
#    Given I send a GET request to "/api/v1/resource" with token "" and no headers or query parameters
#    Then the response status code should be 200
#    And the response body should be of type "Class"

  Scenario: Verify POST request with body
    Given I send a POST request to "/auth/generateToken" with token "", headers "{\"Content-Type\": \"application/json\"}", and body "{ \"username\": \"yusuf@gmail.com\",\"password\": \"123\" }" and query parameters ""
    Then the response status code should be 200
    And the response body should be of type "TokenResponse"
