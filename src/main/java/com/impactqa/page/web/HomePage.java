package com.impactqa.page.web;

import com.impactqa.utilities.SeleniumUtils;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;


/**
 * @author Kundan Kumar Sah
 * @description Implemented logic to handle validations
 */
public class HomePage extends BasePage {
    private static final String PageObjectRepoFileName = "HomePage.xml";

    public HomePage(WebDriver driver) {
        super(driver, PageObjectRepoFileName);
    }

    @Step("Click on Login button")
    public void clickOnLogIn() {
        seleniumUtils.isElementDisplayed("btnLogin", 70);
        seleniumUtils.click("btnLogin");
    }

    @Step("Click on Register button")
    public void clickOnRegister() {
        seleniumUtils.isElementDisplayed("btnRegister", 70);
        seleniumUtils.click("btnRegister");
        seleniumUtils.waitForPageToLoad();
        seleniumUtils.waitForLoader(maxWaitTimeInSec);
    }

    @Step("Get page title")
    public String getTitle() {
        return seleniumUtils.getDriver().getTitle();
    }

}
