package advanced.gherkin;

import io.cucumber.java.Before;
import io.restassured.RestAssured;
import io.qameta.allure.restassured.AllureRestAssured;

public class CucumberHooks {
    @Before
    public void setup() {
        RestAssured.filters(new AllureRestAssured());
    }
}
