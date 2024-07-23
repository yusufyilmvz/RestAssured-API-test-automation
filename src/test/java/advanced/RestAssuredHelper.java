package advanced;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static io.restassured.RestAssured.given;


public class RestAssuredHelper {

    public static <T> Response sendHttpRequest(HttpMethod httpMethod, String endpoint,
                                               String token, Map<String, String> headers,
                                               Map<String, ?> queryParams, Object requestBody,
                                               HttpStatus expectedStatusCode, Class<T> responseClass) {

        if (requestBody == null) {
            requestBody = Map.of();
        }

        Response response = switch (httpMethod) {
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
            default -> throw new IllegalArgumentException("Unsupported HTTP method: " + httpMethod);
        };

        if (response.getStatusCode() != expectedStatusCode.value()) {
            throw new RuntimeException("Expected status code " + expectedStatusCode +
                    " but got " + response.getStatusCode() + "\nResponse body: " + response.getBody().asString());
        }

        if (responseClass != null) {
            try {
                String responseBody = response.getBody().asString();
                ObjectMapper objectMapper = new ObjectMapper();
                T responseObject = objectMapper.readValue(responseBody, responseClass);
            } catch (Exception e) {
                throw new RuntimeException("Failed to parse JSON response into " + responseClass.getSimpleName(), e);
            }
        }
        return response;
    }


    private static RequestSpecification givenRequestSpec(String endpoint,
                                                         String token,
                                                         Map<String, String> headers,
                                                         Map<String, ?> queryParams) {
        RequestSpecification requestSpec = given()
                .baseUri(endpoint)
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

//    public static void main(String[] args) {
//        // Example usage:
//        String token = "your_auth_token_here";
//        String endpoint = "https://api.example.com/delete";
//        int expectedStatusCode = 200;
//
//        // Example DELETE request
//        Response deleteResponse = sendHttpRequest("DELETE", endpoint, token, null, null, null, expectedStatusCode);
//
//        // Handling the response
//        int statusCode = deleteResponse.statusCode();
//        System.out.println("Status code: " + statusCode);
//        String responseBody = deleteResponse.getBody().asString();
//        System.out.println("Response body: " + responseBody);
//    }
}

