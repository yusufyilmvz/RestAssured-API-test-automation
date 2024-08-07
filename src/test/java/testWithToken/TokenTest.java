package testWithToken;

import io.restassured.RestAssured;
import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class TokenTest {

//    private static String token;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost:8082";
    }

//    public void testUserManagementFlow() {
//        loginAndGetToken();
//
//        String userId = createUser();
//
//        getUser(userId);
//
//        updateUser(userId);
//
//        deleteUser(userId);
//    }

//    @Test
    @Order(1)
    public void testRegisterAndLogin() {
        TokenTestUtil.getInstance().testRegisterGetToken("/auth/registerUser", "/auth/generateToken");
    }

//    @Test
    @Order(2)
    public void testGetNameLastname() {
        TokenTestUtil.getInstance().testGetNameLastname("/user/getNameLastname");
    }

//    @Test
    @Order(2)
    public void testPatchPasswordUpdate() {
        TokenTestUtil.getInstance().testUpdatePassword("/user/updatePassword");
    }

//    @Test
    @Order(3)
    public void testDeleteUser() {
        TokenTestUtil.getInstance().testDeleteUser("/user/delete");
    }

//    private void loginAndGetToken() {
//        Map<String, String> credentials = new HashMap<>();
//        credentials.put("username", "testuser");
//        credentials.put("password", "testpass");
//
//        Response response = given()
//                .contentType(ContentType.JSON)
//                .body(credentials)
//                .when()
//                .post("/api/login")
//                .then()
//                .statusCode(200)
//                .extract()
//                .response();
//
//        token = response.jsonPath().getString("token");
//    }
//
//    private String createUser() {
//        Map<String, Object> newUser = new HashMap<>();
//        newUser.put("username", "newuser");
//        newUser.put("email", "newuser@example.com");
//
//        Response response = given()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer " + token)
//                .body(newUser)
//                .when()
//                .post("/api/users")
//                .then()
//                .statusCode(201)
//                .extract()
//                .response();
//
//        return response.jsonPath().getString("id");
//    }
//
//    private void getUser(String userId) {
//        given()
//                .header("Authorization", "Bearer " + token)
//                .when()
//                .get("/api/users")
//                .then()
//                .statusCode(200)
//                .body("username", equalTo("newuser"))
//                .body("email", equalTo("newuser@example.com"));
//    }
//
//    private void updateUser(String userId) {
//        Map<String, Object> updatedUser = new HashMap<>();
//        updatedUser.put("email", "updateduser@example.com");
//
//        given()
//                .contentType(ContentType.JSON)
//                .header("Authorization", "Bearer " + token)
//                .body(updatedUser)
//                .when()
//                .patch("/api/users")
//                .then()
//                .statusCode(200)
//                .body("email", equalTo("updateduser@example.com"));
//    }
//
//    private void deleteUser(String userId) {
//        given()
//                .header("Authorization", "Bearer " + token)
//                .when()
//                .delete("/api/users")
//                .then()
//                .statusCode(204);
//    }
}
