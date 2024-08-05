package advanced.data;

import advanced.RestAssuredHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

// Provides json data from json files.
public class JsonFileReader {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LogManager.getLogger(RestAssuredHelper.class);

    // To read json data from file(s).
    public static <T> List<T> readJsonFromFile(String filePath, Class<T> entityClass) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(filePath)));

            List<T> entities;
            if (isJsonArray(content)) {
                entities = objectMapper.readValue(content, objectMapper.getTypeFactory()
                        .constructCollectionType(List.class, entityClass));
            } else {
                T entity = objectMapper.readValue(content, entityClass);
                entities = Collections.singletonList(entity);
            }

            return entities;
        } catch (IOException e) {
            String errorMessage = "Error reading JSON from file: " + e.getMessage();
            logger.error(errorMessage);
            return Collections.emptyList();
        }
    }

    // Auxiliary method that checks the json format contains array format or not.
    private static boolean isJsonArray(String content) throws IOException {
        ObjectMapper tempMapper = new ObjectMapper();
        var rootNode = tempMapper.readTree(content);
        return rootNode.isArray();
    }
}
