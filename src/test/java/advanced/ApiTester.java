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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


// Demo test class that uses sendHttpRequest global test method in RestAssuredHelper class.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Feature("API Tests")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ApiTester {
    private static String token;

    // To specify base url of the API that will be tested.
    @BeforeAll
    public static void beforeAllSetup() {
        RestAssured.baseURI = ConfigReader.getBaseUrl();
    }

    // To add Allure filter to Rest Assured to generate Allure reports.
    @BeforeEach
    public void setup() {
        RestAssured.filters(new AllureRestAssured());
    }

    // This test method uses the token and gets the response.
    @Test
    @Story("Verify POST request")
    @Description("Validate POST request functionality with JWT token")
    public void testWithToken() {
        assertDoesNotThrow(() -> {
            Response a = RestAssuredHelper.sendHttpRequest(
                    HttpMethod.POST,
                    UserEndpoint.GET_NAME_LASTNAME,
                    token,
                    null,
                    null,
                    null,
                    HttpStatus.OK,
                    NameLastnameResponse.class
            );
        });
    }


    // This test method uses the body data from json file and test one by one.
    // @ParameterizedTest and @MethodSource are used for this operation.
    @Story("Verify Register request")
    @Description("Validate Register request functionality without JWT token")
    @ParameterizedTest
    @MethodSource("advanced.data.TestDataProvider#createUserRequestProvider")
    public void testWithoutToken(CreateUserRequest createUserRequest) {
        createUserRequest.setUsername(RandomString.getAlphaNumericString(6) + "@gmail.com");
        assertDoesNotThrow(() -> {
            Response a = RestAssuredHelper.sendHttpRequest(
                    HttpMethod.POST,
                    UserEndpoint.REGISTER,
                    null,
                    null,
                    null,
                    createUserRequest,
                    HttpStatus.OK,
                    null
            );
        });
    }

    // This test method uses the body data from json file and test one by one.
    // @ParameterizedTest and @MethodSource are used for this operation.
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
