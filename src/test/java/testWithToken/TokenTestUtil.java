package testWithToken;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.Set;

import static io.restassured.RestAssured.given;

public class TokenTestUtil {
    private static String token;
    private static String username;
    private static TokenTestUtil single_instance = null;

    public static synchronized TokenTestUtil getInstance()
    {
        if (single_instance == null)
            single_instance = new TokenTestUtil();

        return single_instance;
    }

    public void testRegisterGetToken(String registerDirectory, String loginDirectory) {
        String randomString = RandomString.getAlphaNumericString(5);
        CreateUserRequest createUserRequest = new CreateUserRequest(
                randomString,
                randomString,
                randomString + "@gmail.com",
                "123",
                "Male",
                Set.of("ROLE_USER")
        );
        username = createUserRequest.getUsername();

        AuthUserRequest authUserRequest = new AuthUserRequest(
                createUserRequest.getUsername(),
                createUserRequest.getPassword()
        );


        given()
                .contentType(ContentType.JSON)
                .body(createUserRequest)
                .when()
                .post(registerDirectory)
                .then()
                .statusCode(200)
                .extract()
                .response();

        Response response = given()
                .contentType(ContentType.JSON)
                .body(authUserRequest)
                .when()
                .post(loginDirectory)
                .then()
                .statusCode(200)
                .extract()
                .response();

        token = response.jsonPath().getString("token");
        System.out.println("Register and get token test:\nToken: ");
        System.out.println(token);
        System.out.println("Random String: " + randomString);
    }

    public void testGetNameLastname(String directory) {
        Response response = given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .when()
                .post(directory)
                .then()
                .statusCode(200)
                .extract()
                .response();

        System.out.println("\nGet Name and Lastname Test:");
        System.out.println(response.getBody().asString());
    }

    public void testUpdatePassword(String directory) {
        PasswordChangeRequest pcr = new PasswordChangeRequest(
                "123",
                "1234",
                "1234"
        );

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .body(pcr)
                .when()
                .patch(directory)
                .then()
                .statusCode(200)
                .extract()
                .response();

        System.out.println("\nPassword Change Test");
    }

    public void testDeleteUser(String directory) {
        given()
                .contentType(ContentType.JSON)
                .header("Authorization", "Bearer " + token)
                .queryParam("username", username)
                .when()
                .delete(directory)
                .then()
                .statusCode(200)
                .extract()
                .response();

        System.out.println("\nUser Delete Test");
    }

    public void deneme() {

    }


}
