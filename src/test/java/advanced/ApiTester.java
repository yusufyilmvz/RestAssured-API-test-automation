package advanced;

import advanced.endpoint.UserEndpoint;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import testWithToken.RandomString;
import testWithToken.model.AuthUserRequest;
import testWithToken.model.CreateUserRequest;
import testWithToken.model.NameLastnameResponse;
import testWithToken.model.TokenResponse;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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


    @Story("Verify Register request")
    @Description("Validate Register request functionality without JWT token")
    @ParameterizedTest
    @MethodSource("advanced.data.TestDataProvider#createUserRequestProvider")
    public void testWithoutToken(CreateUserRequest createUserRequest) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("Accept", "application/json");

        createUserRequest.setUsername(RandomString.getAlphaNumericString(6) + "@gmail.com");
        assertDoesNotThrow(() -> {
            Response a = RestAssuredHelper.sendHttpRequest(
                    HttpMethod.POST,
                    UserEndpoint.REGISTER,
                    null,
                    headers,
                    null,
                    createUserRequest,
                    HttpStatus.OK,
                    null
            );
        });

    }

    //    @Test
    @Story("login - get token")
    @Description("Acquire token by using log in endpoint")
    @Order(1)
    @ParameterizedTest
    @MethodSource("advanced.data.TestDataProvider#loginProvider")
    public void getToken(AuthUserRequest authUserRequest) {
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
