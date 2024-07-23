package advanced;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import testWithToken.AuthUserRequest;
import testWithToken.CreateUserRequest;
import testWithToken.NameLastnameResponse;

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
                "randoemString" + "@gmail.com",
                "123",
                "Male",
                Set.of("ROLE_USER")
        );
        Response a  = RestAssuredHelper.sendHttpRequest(
            HttpMethod.POST,
                baseURI + "/user/getNameLastname",
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJLdnI5REFDMHBwM1U3Wm12YlE1VHV3PT0iLCJpYXQiOjE3MjE2NDc1NjMsImV4cCI6MTcyMTY1NjU2M30.C3AIae2GEv9lnsFHTlo5qnSHsobwu7Woeic5g8zDL7g",
                null,
                null,
                null,
                HttpStatus.OK,
                NameLastnameResponse.class
        );
        System.out.println();
    }

}
