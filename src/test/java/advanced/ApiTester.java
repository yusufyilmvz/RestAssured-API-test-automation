package advanced;

import advanced.data.JsonFile;
import advanced.data.JsonFileReader;
import advanced.data.TestDataProvider;
import advanced.endpoint.UserEndpoint;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import testWithToken.RandomString;
import testWithToken.model.AuthUserRequest;
import testWithToken.model.CreateUserRequest;
import testWithToken.model.NameLastnameResponse;
import testWithToken.model.TokenResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Feature("API Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApiTester {
    private static String token;

    @BeforeAll
    public static void beforeAllSetup() {
        RestAssured.baseURI = ConfigReader.getBaseUrl();
    }

    @BeforeEach
    public void setup() {
        RestAssured.filters(new AllureRestAssured());
    }

    private static Stream<Arguments> testDataProvider() throws IOException {
        return TestDataProvider.provideTestData("C:\\Users\\GYYMM\\IdeaProjects\\restAssuredTry\\src\\test\\resources\\registerUser.json", CreateUserRequest.class);
    }

//    @Test
    @Story("Verify POST request")
    @Description("Validate POST request functionality with JWT token")
//    @ParameterizedTest
//    @MethodSource("testDataProvider")
    public void testWithToken() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");
        assertDoesNotThrow(() -> {
            Response a = RestAssuredHelper.sendHttpRequest(
                    HttpMethod.POST,
                    UserEndpoint.GET_NAME_LASTNAME,
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
        List<CreateUserRequest> users = JsonFileReader.readJsonFromFile(JsonFile.REGISTER_USER, CreateUserRequest.class);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        users.forEach(user -> {
            user.setUsername(RandomString.getAlphaNumericString(6) + "@gmail.com");
            assertDoesNotThrow(() -> {
                Response a = RestAssuredHelper.sendHttpRequest(
                        HttpMethod.POST,
                        UserEndpoint.REGISTER,
                        null,
                        headers,
                        null,
                        user,
                        HttpStatus.OK,
                        null
                );
            });
        });



    }

//    @Test
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
                    UserEndpoint.LOGIN,
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
