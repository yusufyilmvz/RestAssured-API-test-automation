package advanced;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import testWithToken.CreateUserRequest;
import testWithToken.NameLastnameResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Feature("API Tests")
public class Deneme {
    private String baseURI = "http://localhost:8082";

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
        Response a = RestAssuredHelper.sendHttpRequest(
                HttpMethod.POST,
                baseURI + "/user/getNameLastname",
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJLdnI5REFDMHBwM1U3Wm12YlE1VHV3PT0iLCJpYXQiOjE3MjE3Mzk3MjksImV4cCI6MTcyMTc0ODcyOX0.ZP1_Pr7VNummEIRRH_ZmvTBa8a7_tLzIjMFY9Q_O1TM",
                headers,
                null,
                null,
                HttpStatus.OK,
                NameLastnameResponse.class
        );
    }


    @Test
    @Story("Verify Register request")
    @Description("Validate Register request functionality without JWT token")
    public void testWithoutToken() {

        CreateUserRequest createUserRequest = new CreateUserRequest(
                "randomString",
                "randomString",
                "randoemStrisnssssssttxxgs" + "@gmail.com",
                "123",
                "Male",
                Set.of("ROLE_USER")
        );
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        Response a = RestAssuredHelper.sendHttpRequest(
                HttpMethod.POST,
                baseURI + "/auth/registerUser",
                null,
                headers,
                null,
                createUserRequest,
                HttpStatus.OK,
                NameLastnameResponse.class
        );
    }

}
