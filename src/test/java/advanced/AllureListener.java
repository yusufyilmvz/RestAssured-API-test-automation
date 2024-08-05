package advanced;

import io.qameta.allure.restassured.AllureRestAssured;
import org.testng.ITestListener;

public class AllureListener extends AllureRestAssured implements ITestListener {
    // Implement any necessary methods from ITestListener interface
    // This class could be used to add external operations for Allure reports.
}

