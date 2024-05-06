package com.impactqa.page.web;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;


/**
 * @author Kundan Kumar Sah
 * @description Implemented logic to handle validations
 */
public class SurveyPage extends BasePage {
    private static final String PageObjectRepoFileName = "SurveyPage.xml";

    public SurveyPage(WebDriver driver) {
        super(driver, PageObjectRepoFileName);
    }

    @Step("Is Skip button displayed")
    public boolean isSkipButtonDisplay() {
        return seleniumUtils.isElementDisplayed("btnSkip", 70);
    }
    @Step("Click on Skip Button")
    public void clickOnSkipButton() {
        seleniumUtils.waitForPageToLoad();
        seleniumUtils.waitForLoader(maxWaitTimeInSec);
        if (seleniumUtils.isElementDisplayed("btnSkip", 90)) {
            seleniumUtils.click("btnSkip");
            seleniumUtils.waitForPageToLoad();
            seleniumUtils.waitForLoader(maxWaitTimeInSec);
            seleniumUtils.sleep(8000);
        }
    }

}
