package com.impactqa.base;

import com.impactqa.utilities.DriverProvider;
import com.impactqa.utilities.SeleniumUtils;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;

import java.util.Random;

/**
 * @since 22020-11-10
 * @description All Test classes should be extended by this class. It will
 *              manage browser sessions before and after each test case
 *              execution
 */
public class BaseTestWebClassContext {

	protected static SeleniumUtils seleniumUtils = new SeleniumUtils();
	protected WebDriver driver;
	protected String browser;

	@BeforeClass(description = "Open new browser session")
	@Parameters({ "browser" })
	public void setupDriverInstance(String browser) {
		this.browser = browser;
		driver = DriverProvider.createNewBrowserSession(browser);
	}

	@AfterClass(description = "close the browser session")
	public void teardownDriverInstance() {
		if (driver != null)
			driver.quit();
	}

	public WebDriver getDriver() {
		return driver;
	}

}
