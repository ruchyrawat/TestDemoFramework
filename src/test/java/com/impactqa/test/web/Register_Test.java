package com.impactqa.test.web;

import com.impactqa.base.BaseTestWebClassContext;
import com.impactqa.listeners.TestAllureListener;
import com.impactqa.page.web.*;
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
 * @description Validate that user should be able to Register.
 * Test data should be provided in the data sheet located
 * src/test/resources/TestData/testdat.xlsx DataID and Browser
 * should be passed from testng_web.xml
 */

@Epic("BitDelta")
@Feature("Service Provided")
@Listeners(TestAllureListener.class)
public class Register_Test extends BaseTestWebClassContext {
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

    @Test(priority = 1, description = "Create New User")
    @Story("Register New User")
    @Description("Verify that user should successfully able to create a new user ")
    public void createNewUser() throws Exception {
        HomePage homePage = new HomePage(driver);
        homePage.clickOnRegister();

        RegisterPage registerPage = new RegisterPage(driver);
        String email = "abc" + seleniumUtils.randomAlphabetic(5).toLowerCase() + "@yopmail.com";
        registerPage.enterEmail(email);
        registerPage.enterPassword("Test@123" + seleniumUtils.randomAlphanumeric(5));
        registerPage.acceptTermsCondition();
        registerPage.clickOnRegister();

        YopmailPage yopmailPage = new YopmailPage(driver);
        yopmailPage.navigateToYopMailInNewTab(email);
        if (yopmailPage.isOtpMailReceived()) {
            Allure.step("User received OTP mail successfully", Status.PASSED);
        } else {
            Allure.step("User does not receive OTP mail", Status.FAILED);
            Assert.fail("Fail");
        }
        yopmailPage.navigateToNewReceivedOtpMail();
        String otp = yopmailPage.getOTP();
        seleniumUtils.switchTabByIndex(0);
        registerPage.enterOtp(otp);
        registerPage.clickOnVerify();

    }

}
