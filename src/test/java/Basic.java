import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Basic {
    private String baseURI = "https://api.restful-api.dev";
    private static String objectId;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = baseURI;
    }

//    @Test
    @Order(1)
    public void listOfAllObjects() {
        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/objects")
                .then()
                .statusCode(200)
                .extract()
                .response();

        // Log the content in the response body
        System.out.println("Get All Response Body:");
        System.out.println(response.getBody().asString());
    }

    @Test
    @Order(1)
    public void listOfObjectsByIds() {
        Response response = given()
                .contentType(ContentType.JSON)
                .queryParam("id", 3)
                .queryParam("id", 5)
                .queryParam("id", 10)
                .when()
                .get("/objects")
                .then()
                .statusCode(200) // Assert status code is 200 OK
                .extract()
                .response(); // Extract response object

        System.out.println("Partially Get Response Body:");
        System.out.println(response.getBody().asString());
    }

//    @Test
    @Order(1)
    public void addObjectTest() {
        addObject(new TestEntity("Apple MacBook Pro 16", new Data(
                2019, 1849.99, "Intel Core i9", "1 TB"
        )));
    }

    public void addObject(Object object) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(object)
                .when()
                .post("/objects")
                .then()
                .statusCode(404)
                .extract()
                .response();

        System.out.println("Inserted Object Response Body:");
        System.out.println(response.getBody().asString());
        objectId = response.jsonPath().getString("id");
    }

//    @Test
    @Order(2)
    public void updateObjectTest() {
        TestEntity testEntity = new TestEntity(
                "Apple MacBook Pro 16", new Data(
                2019, 2049.99, "Intel Core i9", "1 TB", "silver"
        )
        );
        updateObject(testEntity, objectId);
    }

    public void updateObject(Object object, String id) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(object)
                .when()
                .put("/objects/{id}", objectId)
                .then()
                .statusCode(200) // Assert HTTP 200 OK
                .extract()
                .response(); // Extract response object

        System.out.println("Updated Object Response Body:");
        System.out.println(response.getBody().asString());
    }

//    @Test
    @Order(3)
    public void deleteObject() {
        Response response = given()
                .when()
                .delete("/objects/{id}", objectId)
                .then()
                .statusCode(200) // Assert HTTP 200 OK
                .extract()
                .response(); // Extract response object

        System.out.println("Deleted Object Response:");
        System.out.println(response.getBody().asString());
    }
}
