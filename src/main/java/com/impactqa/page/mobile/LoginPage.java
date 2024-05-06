package com.impactqa.page.mobile;

import com.impactqa.utilities.AllureEnvironmentPropertyUtil;
import com.impactqa.utilities.PageObjectRepoHelper;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;

public class LoginPage extends BasePage{
    private static final String PageObjectRepoFileName = "LoginScreenPage.xml";

    public LoginPage(AppiumDriver driver, PageObjectRepoHelper.PLATFORM platform) {
        super(driver, PageObjectRepoFileName, platform);
    }

    @Step("Tap on Already a Member? Login")
    public void tapOnAlreadyMemberForLogin()
    {
        appiumUtils.click("login");
    }

    @Step("Is Login Screen Displayed")
    public boolean isLoginScreenDisplayed()
    {
        return appiumUtils.isElementDisplayed("loginPage", 20);
    }

    @Step("Enter email address {0}")
    public void enterEmail(String email)
    {
        appiumUtils.isElementDisplayed("enterEmail", 30);
        appiumUtils.enterText("enterEmail", email);
    }

    @Step("Enter password {0}")
    public void enterPassword(String password)
    {
        appiumUtils.enterText("enterPassword", password);
    }

    @Step("Tap on Login Button")
    public void tapOnLogin()
    {
        appiumUtils.isElementDisplayed("loginButton", 20);
        appiumUtils.click("loginButton");
    }

    @Step("Tap on Select MindBreaksDev environment")
    public void tapOnMindBreaksDev()
    {
        appiumUtils.isElementDisplayed("MindBreaksDev", 50);
        appiumUtils.click("MindBreaksDev");
    }

    @Step("Tap on Select Button MindBreaks")
    public void tapOnSelect()
    {
        appiumUtils.isElementDisplayed("select", 50);
        appiumUtils.click("select");
    }
    @Step("Is Experiences Screen Displayed")
    public void isExperiencesDisplay()
    {
        appiumUtils.isElementDisplayed("Experiences", 200);
    }

    @Step("Tap on More Screen Displayed")
    public void tapOnMore()
    {
        appiumUtils.isElementDisplayed("More", 100);
        appiumUtils.click("More");
    }

    @Step("Get the version of application and set in the environment variable in the allure report.")
    public void getVersion(){
        AllureEnvironmentPropertyUtil.addProperty("Version", appiumUtils.getAttribute("Version", "name"));
    }

    @Step("Tap on logout")
    public void tapOnLogout()
    {
//        appiumUtils.isElementDisplayed("Logout", 30);
        appiumUtils.click("Logout");
    }
}
