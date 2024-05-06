package com.impactqa.test.web;

import com.impactqa.base.BaseTestWebClassContext;
import com.impactqa.listeners.TestAllureListener;
import com.impactqa.page.web.HomePage;
import com.impactqa.page.web.LoginPage;
import com.impactqa.page.web.SurveyPage;
import com.impactqa.utilities.CryptoUtils;
import com.impactqa.utilities.ExcelUtil;
import com.impactqa.utilities.FrameworkConfig;
import io.qameta.allure.*;
import io.qameta.allure.model.Status;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * @author Kundan Kumar Sah
 * @description Validate that user should be able to Log in.
 * Test data should be provided in the data sheet located
 * src/test/resources/TestData/testdat.xlsx DataID and Browser
 * should be passed from testng_web.xml
 */

@Epic("BitDelta")
@Feature("Service Provided")
@Listeners(TestAllureListener.class)
public class Login_Test extends BaseTestWebClassContext {

    private Map<String, String> testDataMap;

    @BeforeMethod
    @Parameters({"dataID"})
    @Description("Read test data with testID {0}")
    public void getTestData(String dataID) {
        ExcelUtil excel = new ExcelUtil();
        excel.setWorkbook(
                FrameworkConfig.getStringConfigProperty("TestDataFileLocation"),
                FrameworkConfig.getStringConfigProperty("TestDataSheetNameWeb")
        );
        testDataMap = excel.getRowDataMatchingDataId(dataID);
        System.out.println(testDataMap);
        if (testDataMap.size() < 1)
            Assert.fail("dataID '" + dataID + "' is valid the excel sheet. please check the test data sheet");
    }

    @Test(priority = 1, description = "Log In")
    @Story("verify login")
    @Description("Verify that user should be able to log in successfully")
    public void loginPage() throws Exception {
        HomePage homePage = new HomePage(driver);
        homePage.clickOnLogIn();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.enterEmail(FrameworkConfig.getStringConfigProperty("Email"));
        String password = CryptoUtils.decryptTheValue(FrameworkConfig.getStringConfigProperty("Password"));
        loginPage.enterPassword(password);
        loginPage.clickOnLogInButton();

        SurveyPage surveyPage = new SurveyPage(driver);
        surveyPage.clickOnSkipButton();
        String title = "Buy/Sell Crypto, Stocks and Derivatives Securely | BitDelta";
        if (homePage.getTitle().equalsIgnoreCase(title)) {
            Allure.step("User is successfully logged in", Status.PASSED);
        } else {
            Allure.step("User is not able login successfully", Status.FAILED);
            Assert.fail("Fail");
        }
    }
}
