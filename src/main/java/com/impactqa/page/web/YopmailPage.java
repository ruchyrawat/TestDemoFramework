package com.impactqa.page.web;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;

import java.util.Iterator;
import java.util.Set;


/**
 * @author Kundan Kumar Sah
 * @description Implemented logic to handle validations
 */
public class YopmailPage extends BasePage {
    private static final String PageObjectRepoFileName = "YopmailPage.xml";

    public YopmailPage(WebDriver driver) {
        super(driver, PageObjectRepoFileName);
    }


    @Step("Enter email address {0}")
    public void navigateToYopMailInNewTab(String mailId) {
        seleniumUtils.waitForPageToLoad();
        // Open new window
        seleniumUtils.executeJavaScript("window.open('','_blank');");
        String mainWindowHandle = seleniumUtils.getDriver().getWindowHandle();
        Set<String> currentWindows = seleniumUtils.getDriver().getWindowHandles();
        Iterator<String> iterator = currentWindows.iterator();
        while (iterator.hasNext()) {
            String ChildWindow = iterator.next();
            if (!mainWindowHandle.equalsIgnoreCase(ChildWindow)) {
                seleniumUtils.getDriver().switchTo().window(ChildWindow);
            }
        }
        seleniumUtils.getDriver().get("https://yopmail.com/");
        seleniumUtils.waitForPageToLoad();
        seleniumUtils.enterText("txtEmailAddress", mailId);
        seleniumUtils.pressEnterKey("txtEmailAddress");
    }

    @Step("Refresh email")
    public void refreshMail() {
        seleniumUtils.waitForPageToLoad();
        seleniumUtils.waitForElementToDisplay("icnRefresh");
        seleniumUtils.waitForElementToBeClickable("icnRefresh");
        seleniumUtils.click("icnRefresh");
        seleniumUtils.waitForLoader(maxWaitTimeInSec);
        seleniumUtils.waitForPageToLoad();
    }
    @Step("Is OTP mail received")
        public boolean isOtpMailReceived() {
            try {
                seleniumUtils.waitForPageToLoad();
                seleniumUtils.waitForLoader();
                refreshMail();
                seleniumUtils.getDriver().switchTo().frame("ifinbox");
                return seleniumUtils.isElementDisplayed("lnkNewMail", maxWaitTimeInSec);
            } catch (Exception ex) {
                return false;
            }
        }

    @Step("Navigate to newly received OTP Mail")
    public void navigateToNewReceivedOtpMail() {
        seleniumUtils.waitForPageToLoad();
        seleniumUtils.waitForElementToBeClickable("lnkNewMail");
        seleniumUtils.javaScriptClick("lnkNewMail");
        seleniumUtils.getDriver().switchTo().defaultContent();
    }


    @Step("Get OTP")
    public String getOTP() {
        seleniumUtils.waitForPageToLoad();
        seleniumUtils.waitForLoader();
        seleniumUtils.getDriver().switchTo().frame("ifmail");
        if (seleniumUtils.isElementDisplayed("txtOTP", 90)) {
            return seleniumUtils.getText("txtOTP").trim();
        } else {
            return null;
        }
    }
}
