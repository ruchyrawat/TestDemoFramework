package com.impactqa.page.mobile;

import com.impactqa.utilities.PageObjectRepoHelper;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;


public class Calcu extends BasePage {
    private static final String PageObjectRepoFileName = "Cal.xml";

    public Calcu(AppiumDriver driver, PageObjectRepoHelper.PLATFORM platform) {
        super(driver, PageObjectRepoFileName, platform);
    }

    @Step("Is Begin Screen Displayed")
    public void tapOnFive()
    {
        appiumUtils.click("Five");
    }

    @Step("Tap on Begin Screen")
    public void tapOnAdd()
    {
        appiumUtils.click("Add");
    }

    @Step("Is Register Screen Displayed")
    public boolean isRegisterScreenDisplayed()
    {
        return appiumUtils.isElementDisplayed("register", 30);
    }
}
