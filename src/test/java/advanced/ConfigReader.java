package advanced;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//  Config reader class that reads configurations from config.properties file that is located in resources.
public class ConfigReader {

    private static Properties properties = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find config.properties");
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static String getBaseUrl() {
        return properties.getProperty("api.base.url");
    }
}
