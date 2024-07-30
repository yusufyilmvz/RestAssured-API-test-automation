package advanced.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.params.provider.Arguments;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

public class TestDataProvider {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> Stream<Arguments> provideTestData(String filePath, Class<T> type) throws IOException {
        List<T> data = objectMapper.readValue(new File(filePath), new TypeReference<List<T>>() {});
        return data.stream().map(Arguments::of);
    }
}
