package advanced.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import testWithToken.model.CreateUserRequest;

import java.util.List;
import java.util.stream.Stream;

public class TestDataProvider {
    private static final ObjectMapper objectMapper = new ObjectMapper();

//    public static <T> Stream<Arguments> provideTestData(String filePath, Class<T> type) throws IOException {
//        List<T> data = objectMapper.readValue(new File(filePath), new TypeReference<List<T>>() {});
//        return data.stream().map(Arguments::of);
//    }

    public static Stream<CreateUserRequest> createUserRequestProvider() {
        List<CreateUserRequest> users = JsonFileReader.readJsonFromFile(JsonFile.REGISTER_USER, CreateUserRequest.class);
        return users.stream();
    }
}
