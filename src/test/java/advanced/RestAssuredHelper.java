package advanced;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;

// The Rest Assured class that globalize all the test operations.
public class RestAssuredHelper {

    private static final Logger logger = LogManager.getLogger(RestAssuredHelper.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Main test method that gets required parameters for testing operation.
    // in case fail, trows error and logs the problem in test.
    // in case success, returns the response and logs test information.
    public static <T> Response sendHttpRequest(HttpMethod httpMethod, String endpoint,
                                               String token, Map<String, String> headers,
                                               Map<String, ?> queryParams, Object requestBody,
                                               HttpStatus expectedStatusCode, Class<T> responseClass) {

        // This part is used to acquire the name of the method that is call this method.
        String testMethodName = "";
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length > 2) {
            StackTraceElement caller = stackTrace[2];
            String className = caller.getClassName();
            String methodName = caller.getMethodName();
            int startingIndex = methodName.indexOf('$');
            int endIndex = methodName.lastIndexOf('$');
            methodName = methodName.substring(startingIndex + 1, endIndex);

            testMethodName = className + "." + methodName;
        }

        // To get right json request body for logging.
        String jsonRequestBody;
        try {
            jsonRequestBody = objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            jsonRequestBody = "{}";
        }
        // General log information pattern.
        String configurationMessage = String.format("Test Method: %s, Endpoint: %s, Method: %s, Headers: %s, Params: %s, Body: %s", testMethodName, RestAssured.baseURI + endpoint, httpMethod, headers, queryParams, jsonRequestBody);

        if (requestBody == null) {
            requestBody = Map.of();
        }

        // This part sends the request according to given http method. After that, gives the response of the dent request.
        Response response;
        RequestSpecification requestSpecification;
        try {
            requestSpecification = requestFactory(endpoint, token, headers, queryParams, requestBody);
            response = switch (httpMethod) {
                case GET -> requestSpecification
                        .get();
                case POST -> requestSpecification
                        .post();
                case DELETE -> requestSpecification
                        .delete();
                case PATCH -> requestSpecification
                        .patch();
                case PUT -> requestSpecification
                        .put();
                default -> {
                    String message = "Unsupported HTTP method: " + httpMethod;
                    logger.error("{} --> {}", configurationMessage, message);
                    throw new IllegalArgumentException(message);
                }
            };
        } catch (Exception exception) {
            logger.error("{} --> {}", configurationMessage, exception.getMessage());
            throw exception;
        }

        // This part compares the expected status code and response status code.
        if (response.getStatusCode() != expectedStatusCode.value()) {
            String message = "Expected status code " + expectedStatusCode +
                    " but got " + response.getStatusCode() + "\tResponse body: " + response.getBody().asString();
            logger.error("{} --> {}", configurationMessage, message);
            throw new RuntimeException(message);
        }

        // This part checks the structure of the body according to expected model class.
        String responseBody = response.getBody().asString();
        try {
            if (responseClass == null && !responseBody.isEmpty()) {
                throw new RuntimeException();
            } else if (responseClass != null) {
                if (isJsonArray(responseBody)) {
                    objectMapper.readValue(responseBody, objectMapper.getTypeFactory()
                            .constructCollectionType(List.class, responseClass));
                } else {
                    objectMapper.readValue(responseBody, responseClass);
                }
            }
        } catch (Exception e) {
            String responseClassName = responseClass == null ? "null" : responseClass.getSimpleName();
            String message = "Failed to parse JSON response into " + responseClassName;
            logger.error("{} --> {}", configurationMessage, message);
            throw new RuntimeException(message);
        }
        if (responseBody.isEmpty()) {
            responseBody = "{}";
        }
        logger.info("{} --> {}", configurationMessage, responseBody);
        // if every test is passed, returns the response of the sent request.
        return response;
    }

    // Factory method that initialize the request according to its parameters.
    private static RequestSpecification givenRequestSpec(String endpoint,
                                                         String token,
                                                         Map<String, String> headers,
                                                         Map<String, ?> queryParams) {
        RequestSpecification requestSpec = given()
                .baseUri(RestAssured.baseURI + endpoint)
                .contentType(ContentType.JSON);

        if (token != null) {
            requestSpec.header("Authorization", "Bearer " + token);
        }
        if (headers != null) {
            headers.forEach(requestSpec::header);
        }
        if (queryParams != null) {
            requestSpec.queryParams(queryParams);
        }
        return requestSpec;
    }

    // Checks the json data contains array or single json object
    private static boolean isJsonArray(String content) throws IOException {
        ObjectMapper tempMapper = new ObjectMapper();
        var rootNode = tempMapper.readTree(content);
        return rootNode.isArray();
    }

    private static RequestSpecification requestFactory(String endpoint,
                              String token,
                              Map<String, String> headers,
                              Map<String, ?> queryParams,
                              Object requestBody) {
        return givenRequestSpec(endpoint, token, headers, queryParams)
                .body(requestBody)
                .when();
    }
}