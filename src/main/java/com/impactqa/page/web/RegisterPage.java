package com.impactqa.page.web;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;


/**
 * @author Kundan Kumar Sah
 * @description Implemented logic to handle validations
 */
public class RegisterPage extends BasePage {
    private static final String PageObjectRepoFileName = "RegisterPage.xml";

    public RegisterPage(WebDriver driver) {
        super(driver, PageObjectRepoFileName);
    }


    @Step("Enter email address {0}")
    public void enterEmail(String email) {
        seleniumUtils.isElementDisplayed("txtEmail", 70);
        seleniumUtils.enterText("txtEmail", email);
        seleniumUtils.waitForLoader(implicitWaitSec);
    }

    @Step("Enter password {0}")
    public void enterPassword(String password) {
        seleniumUtils.isElementDisplayed("txtPassword", 70);
        seleniumUtils.enterText("txtPassword", password);
        seleniumUtils.waitForLoader(implicitWaitSec);
    }

    @Step("Accept Terms & Condition")
    public void acceptTermsCondition() {
        seleniumUtils.isElementDisplayed("chkAcceptTerms", 70);
        seleniumUtils.click("chkAcceptTerms");
        seleniumUtils.waitForPageToLoad();
        seleniumUtils.waitForLoader();
    }

    @Step("Click on Register button")
    public void clickOnRegister() {
        seleniumUtils.isElementDisplayed("btnRegister", 70);
        seleniumUtils.click("btnRegister");
        seleniumUtils.waitForPageToLoad();
        seleniumUtils.waitForLoader();
        seleniumUtils.isElementDisplayed("txtOTP", 90);
        seleniumUtils.sleep(8000);
    }

    @Step("Enter otp {0}")
    public void enterOtp(String otp) {
        seleniumUtils.isElementDisplayed("txtOTP", 70);
        seleniumUtils.enterText("txtOTP", otp);
        seleniumUtils.waitForLoader(implicitWaitSec);
    }

    @Step("Click on Verify button")
    public void clickOnVerify() {
        seleniumUtils.isElementDisplayed("btnVerify", 90);
        seleniumUtils.click("btnVerify");
        seleniumUtils.waitForPageToLoad();
        seleniumUtils.waitForLoader();
    }

}
