package advanced.gherkin;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


// Cucumber Test Class
@RunWith(Cucumber.class)
// Gherkin scenarios and Cucumber path should be specified at @CucumberOptions annotation.
@CucumberOptions(
        features = "src/test/resources/features",
        glue = "advanced.gherkin",
        plugin = {"io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"}

)
public class GherkinTester {
}
