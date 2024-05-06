package com.impactqa.page.web;

import com.impactqa.page.web.BasePage;
import com.impactqa.utilities.GMailAPI;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @author Kundan Kumar Sah
 * @description Implemented logic to handle validations
 */
public class LoginPage extends BasePage {
    private static final String PageObjectRepoFileName = "LoginPage.xml";

    public LoginPage(WebDriver driver) {
        super(driver, PageObjectRepoFileName);
    }

    @Step("Enter email address {0}")
    public void enterEmail(String email) {
        seleniumUtils.isElementDisplayed("txtEmail", 30);
        seleniumUtils.enterText("txtEmail", email);
    }

    @Step("Enter password {0}")
    public void enterPassword(String password) {
        seleniumUtils.enterText("txtPassword", password);
    }

    @Step("Click on Login Button")
    public void clickOnLogInButton() {
        seleniumUtils.click("btnLogin");
        seleniumUtils.waitForPageToLoad();
        seleniumUtils.waitForLoader(maxWaitTimeInSec);
    }

    @Step("Get page title")
    public String getTitle() {
       return seleniumUtils.getDriver().getTitle();
    }
}
