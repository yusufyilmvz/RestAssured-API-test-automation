package advanced;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.http.HttpStatus;
import testWithToken.*;
import testWithToken.model.AuthUserRequest;
import testWithToken.model.CreateUserRequest;
import testWithToken.model.NameLastnameResponse;
import testWithToken.model.TokenResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Feature("API Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Deneme {
    private String baseURI = "http://localhost:8082";
    private static String token;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = baseURI;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.filters(new AllureRestAssured());
    }

    @Test
    @Story("Verify POST request")
    @Description("Validate POST request functionality with JWT token")
    public void testWithToken() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        assertDoesNotThrow(() -> {
            Response a = RestAssuredHelper.sendHttpRequest(
                    HttpMethod.POST,
                    baseURI + "/user/getNameLastname",
                    token,
                    headers,
                    null,
                    null,
                    HttpStatus.OK,
                    NameLastnameResponse.class
            );
        });
    }


    @Test
    @Story("Verify Register request")
    @Description("Validate Register request functionality without JWT token")
    public void testWithoutToken() {

        String randomString = RandomString.getAlphaNumericString(5);
        CreateUserRequest createUserRequest = new CreateUserRequest(
                randomString,
                randomString,
                randomString + "@gmail.com",
                "123",
                "Male",
                Set.of("ROLE_USER")
        );
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        // jenkins trigger try
        assertDoesNotThrow(() -> {
            Response a = RestAssuredHelper.sendHttpRequest(
                    HttpMethod.POST,
                    baseURI + "/auth/registerUser",
                    null,
                    headers,
                    null,
                    createUserRequest,
                    HttpStatus.OK,
                    null
            );
        });

    }

    @Test
    @Story("login - get token")
    @Description("Acquire token by using log in endpoint")
    @Order(1)
    public void getToken() {

        AuthUserRequest authUserRequest = new AuthUserRequest(
                "yusuf@gmail.com",
                "123"
        );

        assertDoesNotThrow(() -> {
            Response a = RestAssuredHelper.sendHttpRequest(
                    HttpMethod.POST,
                    baseURI + "/auth/generateToken",
                    null,
                    null,
                    null,
                    authUserRequest,
                    HttpStatus.OK,
                    TokenResponse.class
            );
            token = a.jsonPath().getString("token");
        });
    }
}
