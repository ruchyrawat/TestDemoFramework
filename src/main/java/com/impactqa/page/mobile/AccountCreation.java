package com.impactqa.page.mobile;

import com.impactqa.utilities.PageObjectRepoHelper;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;

import java.util.Random;

public class AccountCreation extends BasePage{

    private static final String PageObjectRepoFileName = "AccountCreation.xml";

    public AccountCreation(AppiumDriver driver, PageObjectRepoHelper.PLATFORM platform) {
        super(driver, PageObjectRepoFileName, platform);
    }

    Random randomGenerator = new Random();
    int randomInt = randomGenerator.nextInt(1000);
    String email = "username"+ randomInt +"@yopmail.com";

    @Step("Enter email address")
    public void enterEmail()
    {
        appiumUtils.isElementDisplayed("Email", 30);
        appiumUtils.enterText("Email", email);
    }

    @Step("Enter re-email address")
    public void reEnterEmail()
    {
        appiumUtils.enterText("ReEnterEmail", email);
    }

    @Step("Tap on Register Button")
    public void tapOnRegister()
    {
        appiumUtils.click("register");
    }

    @Step("Is Enter your code displayed")
    public boolean isEnterYourCodeDisplayed()
    {
        return appiumUtils.isElementDisplayed("yourCode", 30);
    }
}
