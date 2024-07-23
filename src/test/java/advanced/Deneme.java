package advanced;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import testWithToken.AuthUserRequest;
import testWithToken.CreateUserRequest;
import testWithToken.NameLastnameResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Deneme {
    private String baseURI = "http://localhost:8082";

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = baseURI;
    }

    @Test
    public void test() {
        CreateUserRequest createUserRequest = new CreateUserRequest(
                "randomString",
                "randomString",
                "randoemStrisnsssttxxgs" + "@gmail.com",
                "123",
                "Male",
                Set.of("ROLE_USER")
        );
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        Response a = RestAssuredHelper.sendHttpRequest(
                HttpMethod.POST,
                baseURI + "/user/getNameLastname",
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJLdnI5REFDMHBwM1U3Wm12YlE1VHV3PT0iLCJpYXQiOjE3MjE3MjAyMzcsImV4cCI6MTcyMTcyOTIzN30.f4Vuyib8ZBBCT0CCrYYz64CtTlg6dboNMifykGwu4jA",
                headers,
                null,
                null,
                HttpStatus.OK,
                NameLastnameResponse.class
        );
        System.out.println();
    }

}
