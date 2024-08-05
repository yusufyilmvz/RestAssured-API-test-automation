package advanced.gherkin;

import io.cucumber.java.Before;
import io.restassured.RestAssured;
import io.qameta.allure.restassured.AllureRestAssured;

// To specify some hooks for Cucumber. This class used to add Rest Assured filter to acquire Allure reports.
public class CucumberHooks {
    @Before
    public void setup() {
        RestAssured.filters(new AllureRestAssured());
    }
}
