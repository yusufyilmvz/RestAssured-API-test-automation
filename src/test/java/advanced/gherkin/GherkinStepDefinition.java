package advanced.gherkin;

import advanced.ConfigReader;
import advanced.HttpMethod;
import advanced.RestAssuredHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class GherkinStepDefinition {
    private final static ObjectMapper mapper = new ObjectMapper();


    private Response response;

    private HttpMethod httpMethod;
    private String endpoint;
    private String token;
    private Map<String, String> headers = new HashMap<>();
    private Map<String, ?> queryParams = new HashMap<>();
    private Object requestBody;
    private HttpStatus expectedStatusCode;
    private Class<?> responseClass;

    @Given("I send a {word} request to {string} with token {string}, headers {string}, and body {string} and query parameters {string}")
    public void i_send_a_request_to_with_token_and_headers_and_query_parameters_and_body(
            String method, String endpoint, String token, String headers,
            String body, String queryParams) throws IOException {

        this.httpMethod = HttpMethod.valueOf(method.toUpperCase());
        this.endpoint = endpoint;
        this.token = token.isEmpty() ? null : token;
        this.headers = headers.isEmpty() ? null : convertJsonToMap(headers);
        this.queryParams = queryParams.isEmpty() ? null : convertJsonToMap(queryParams);
        this.requestBody = body.isEmpty() ? null : convertJsonToMap(body);
        System.out.println();
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(int statusCode) {
        this.expectedStatusCode = HttpStatus.valueOf(statusCode);
    }

    @And("the response body should be of type {string}")
    public void the_response_body_should_be_of_type(String responseType) {
        responseClass = getResponseClass(responseType);
    }

    private Class<?> getResponseClass(String className) {
        try {
            if (className.isEmpty())
                return null;
            return Class.forName("testWithToken.model." + className); // Adjust package as needed
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Response class not found: " + className, e);
        }
    }

    @After
    public void afterScenario() {
        RestAssured.baseURI = ConfigReader.getBaseUrl();
        assertDoesNotThrow(() -> {
            Response a = RestAssuredHelper.sendHttpRequest(
                    httpMethod,
                    endpoint,
                    token,
                    headers,
                    queryParams,
                    requestBody,
                    expectedStatusCode,
                    responseClass
            );
        });

    }

    public static Map<String, String> convertJsonToMap(String json) throws IOException {
        return mapper.readValue(json, new TypeReference<Map<String, String>>(){});
    }
}

