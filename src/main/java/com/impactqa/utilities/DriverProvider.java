package com.impactqa.utilities;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * @version 1.0
 * @description This class creates Appium Driver session
 * @since 2021-03-20
 */

public class DriverProvider {

    /**
     * This method create AppiumDriver for Local and Cloud devices
     * <p>
     * Takes value of <b>RunTestInMobileCloud=(true/false) from config.properties </b>
     * <p>
     * if RunTestInMobileCloud=true
     * <i> it return browser session for Cloud devices</i>
     * <p>
     * if RunTestInMobileCloud=false
     * <i> it return browser session for local devices</i>
     *
     * @param platform       - Type of platform (IOS/Android/Web)
     * @param sessionDetails - Session/Capabilities Details
     */
    @Step("Mobile Session")
    public static AppiumDriver createNewMobileSession(PageObjectRepoHelper.PLATFORM platform, Map<String, String> sessionDetails) throws MalformedURLException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();

        for (String key : sessionDetails.keySet()) {
            if (sessionDetails.get(key) != null && !sessionDetails.get(key).trim().isEmpty()) {

                if (key.equals("appFileName")) {
                    String appFileName = sessionDetails.get(key);
                    String appPath = null;
                    if (appFileName.contains("/") || appFileName.contains("\\") || appFileName.equals("settings") || appFileName.equals("com.android.settings") || appFileName.startsWith("PRIVATE:"))
                        appPath = appFileName;
                    else
                        appPath = new File(FrameworkConfig.getStringConfigProperty("MobileApppath") + "/" + appFileName).getAbsolutePath();
                    desiredCapabilities.setCapability("app", appPath);

                } else
                    desiredCapabilities.setCapability(key, sessionDetails.get(key).trim());
            }
        }
        URL remoteUrl = null;

        if (FrameworkConfig.getStringConfigProperty("RunTestInMobileCloud").equals("true")) {
            remoteUrl = new URL("https://" + FrameworkConfig.getStringConfigProperty("remoteCloudMobileDriverHubHost") + "/wd/hub");
            desiredCapabilities.setCapability("securityToken", FrameworkConfig.getStringConfigProperty("MobileCloudSecurityKey"));
        } else {
            remoteUrl = new URL("http://" + FrameworkConfig.getStringConfigProperty("remoteMobileDriverHubHost") + "/wd/hub");
        }
        AppiumDriver driver = null;
        if (platform == PageObjectRepoHelper.PLATFORM.ANDROID) {
            driver = new AndroidDriver(remoteUrl, desiredCapabilities);
        } else if (platform == PageObjectRepoHelper.PLATFORM.IOS) {
            driver = new IOSDriver(remoteUrl, desiredCapabilities);
        }
        Allure.step("Capabilities: " + driver.getCapabilities());
        return driver;
    }

    /**
     * This method create WebDriver for Local and Remote Browsers
     *
     * @param browserName Web Browser Name
     *                    This method create Browser session for Locally
     */
    public static WebDriver createNewBrowserSession(String browserName) {
        WebDriver driver = null;
        if (FrameworkConfig.getStringConfigProperty("RunTestInLocalBrowser").equals("true"))
            driver = createNewLocalBrowserSession(browserName);
        else
            driver = createNewRemoteBrowserSession(browserName);

        switch (FrameworkConfig.getStringConfigProperty("WebEnvironment")) {
            case "QA":
                System.out.println("You are in QA env");
                driver.get(FrameworkConfig.getStringConfigProperty("ApplicationURL_QA"));
                break;
            case "Dev":
                driver.get(FrameworkConfig.getStringConfigProperty("ApplicationURL_Dev"));
                break;
            case "UAT":
                driver.get(FrameworkConfig.getStringConfigProperty("ApplicationURL_UAT"));
                break;
            case "Prod":
                driver.get(FrameworkConfig.getStringConfigProperty("ApplicationURL_Prod"));
                break;
            default:
                throw new RuntimeException(FrameworkConfig.getStringConfigProperty("WebEnvironment") + "Environment Does not exist");
        }
        return driver;
    }


    /**
     * This method create WebDriver for Local Browsers
     *
     * @param browserName Web Browser Name
     *                    This method create Browser session for Locally
     */
    public static WebDriver createNewLocalBrowserSession(String browserName) {
        WebDriver driver = null;
        switch (browserName.toLowerCase()) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "\\driver\\chromedriver.exe");
                driver = new ChromeDriver();
                break;
            case "firefox":
                driver = new FirefoxDriver();
                break;
            default:
                driver = new ChromeDriver();
                break;
        }
        DriverManager.setWebDriver(driver);
        driver.manage().window().maximize();
        return driver;
    }

    /**
     * This method create Browser session for Remote server
     *
     * @param BrowserName Web browser name
     */
    private static WebDriver createNewRemoteBrowserSession(String BrowserName) {
        WebDriver driver = null;
        URL remoteUrl = null;
        DesiredCapabilities capabilities = null;
        switch (BrowserName) {
            case "chrome":
                capabilities = DesiredCapabilities.chrome();
                break;
            case "firefox":
                capabilities = DesiredCapabilities.firefox();
                break;
            case "edge":
                capabilities = DesiredCapabilities.edge();
                break;
            default:
                Assert.fail("Options :\"" + BrowserName + "\" Dose not exist");
        }

        try {
            remoteUrl = new URL("http://" +
                    FrameworkConfig.getStringConfigProperty("RemoteWebDriverHubHost") + "/wd/hub");
        } catch (Exception e) {
            Assert.fail(FrameworkConfig.getStringConfigProperty("RemoteWebDriverHubHost") + ": Error Occurred While launching new session ");
        }
        driver = new RemoteWebDriver(remoteUrl, capabilities);
        DriverManager.setWebDriver(driver);
        driver.manage().window().maximize();
        return driver;
    }


}
