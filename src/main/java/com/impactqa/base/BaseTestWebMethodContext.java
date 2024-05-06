package com.impactqa.base;

import com.impactqa.utilities.DriverManager;
import com.impactqa.utilities.DriverProvider;
import com.impactqa.utilities.FrameworkConfig;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;

import java.io.ByteArrayInputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Maqdoom Sharief
 * @since 22020-11-10
 * @description All Test classes should be extended by this class. It will
 *              manage browser sessions before and after each test case
 *              execution
 */
public class BaseTestWebMethodContext {

	@Step("Open new browser session")
	public WebDriver setupDriverInstance(String browser) throws MalformedURLException {
		WebDriver driver = DriverProvider.createNewBrowserSession(browser);
		return driver;
	}
	@AfterMethod
	@Description("Get Screenshot for failed cases")
	public void afterMethodFailed(ITestResult result) {
		WebDriver driver = DriverManager.getWebDriver();
		if(driver!=null)
			if(ITestResult.FAILURE ==result.getStatus()){
				TakesScreenshot tk = (TakesScreenshot) driver;
				byte[] b = tk.getScreenshotAs(OutputType.BYTES);
				Allure.addAttachment("Screenshot", "image/png", new ByteArrayInputStream(b), "png");
			}
	}
	//	Added for reference. If you don't want screenshots for passed statements, then set enabled flag as false

	@AfterMethod(enabled = true)
	@Description("Get Screenshot for passed cases")
	public void afterMethodPassed(ITestResult result) {
		WebDriver driver = DriverManager.getWebDriver();
		if(driver!=null)
			if(ITestResult.SUCCESS ==result.getStatus()){
				TakesScreenshot tk = (TakesScreenshot) driver;
				byte[] b = tk.getScreenshotAs(OutputType.BYTES);
				Allure.addAttachment("Screenshot", "image/png", new ByteArrayInputStream(b), "png");
			}
	}

	@AfterMethod(alwaysRun = true)
	@Description("close the browser session")
	public void teardownDriverInstance()
	{
		WebDriver driver = DriverManager.getWebDriver();
		if(driver!=null) {
			System.out.println("teardownDriverInstance");
			driver.quit();
		}
	}
}
