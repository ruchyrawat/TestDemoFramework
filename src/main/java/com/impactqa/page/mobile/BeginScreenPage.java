package com.impactqa.page.mobile;

import com.impactqa.utilities.PageObjectRepoHelper;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;


public class BeginScreenPage extends BasePage {
    private static final String PageObjectRepoFileName = "BeginScreenPage.xml";

    public BeginScreenPage(AppiumDriver driver, PageObjectRepoHelper.PLATFORM platform) {
        super(driver, PageObjectRepoFileName, platform);
    }

    @Step("Is Begin Screen Displayed")
    public boolean isBeginScreenDisplayed()
    {
        return appiumUtils.isElementDisplayed("begin", 100);
    }

    @Step("Tap on Begin Screen")
    public void tapOnBegin()
    {
        appiumUtils.click("begin");
    }

    @Step("Is Register Screen Displayed")
    public boolean isRegisterScreenDisplayed()
    {
        return appiumUtils.isElementDisplayed("register", 30);
    }
}
