package advanced.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import testWithToken.model.AuthUserRequest;
import testWithToken.model.CreateUserRequest;

import java.util.List;
import java.util.stream.Stream;

// This class provides test data body from json files. Every method receive its custom data from specific file
public class TestDataProvider {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Returns the register body data from json file.
    public static Stream<CreateUserRequest> createUserRequestProvider() {
        List<CreateUserRequest> users = JsonFileReader.readJsonFromFile(JsonFile.REGISTER_USER, CreateUserRequest.class);
        return users.stream();
    }

    // Returns the login body data from json file.
    public static Stream<AuthUserRequest> loginProvider() {
        List<AuthUserRequest> users = JsonFileReader.readJsonFromFile(JsonFile.LOGIN, AuthUserRequest.class);
        return users.stream();
    }

    // There could be other data methods according to the requirements.
}
