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


public class RestAssuredHelper {

    private static final Logger logger = LogManager.getLogger(RestAssuredHelper.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static <T> Response sendHttpRequest(HttpMethod httpMethod, String endpoint,
                                               String token, Map<String, String> headers,
                                               Map<String, ?> queryParams, Object requestBody,
                                               HttpStatus expectedStatusCode, Class<T> responseClass) {

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

        String jsonRequestBody;
        try {
            jsonRequestBody = objectMapper.writeValueAsString(requestBody);
        } catch (JsonProcessingException e) {
            jsonRequestBody = "{}";
        }
        String configurationMessage = String.format("Test Method: %s, Endpoint: %s, Method: %s, Headers: %s, Params: %s, Body: %s", testMethodName, RestAssured.baseURI + endpoint, httpMethod, headers, queryParams, jsonRequestBody);

        if (requestBody == null) {
            requestBody = Map.of();
        }

        Response response;
        try {
            response = switch (httpMethod) {
                case GET -> givenRequestSpec(endpoint, token, headers, queryParams)
                        .body(requestBody)
                        .when()
                        .get();
                case POST -> givenRequestSpec(endpoint, token, headers, queryParams)
                        .body(requestBody)
                        .when()
                        .post();
                case DELETE -> givenRequestSpec(endpoint, token, headers, queryParams)
                        .body(requestBody)
                        .when()
                        .delete();
                case PATCH -> givenRequestSpec(endpoint, token, headers, queryParams)
                        .body(requestBody)
                        .when()
                        .patch();
                case PUT -> givenRequestSpec(endpoint, token, headers, queryParams)
                        .body(requestBody)
                        .when()
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

        if (response.getStatusCode() != expectedStatusCode.value()) {
            String message = "Expected status code " + expectedStatusCode +
                    " but got " + response.getStatusCode() + "\tResponse body: " + response.getBody().asString();
            logger.error("{} --> {}", configurationMessage, message);
            throw new RuntimeException(message);
        }
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
        return response;
    }

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

    private static boolean isJsonArray(String content) throws IOException {
        ObjectMapper tempMapper = new ObjectMapper();
        var rootNode = tempMapper.readTree(content);
        return rootNode.isArray();
    }
}

