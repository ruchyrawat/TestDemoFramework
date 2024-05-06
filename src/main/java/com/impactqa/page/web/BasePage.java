package com.impactqa.page.web;

import com.impactqa.utilities.SeleniumUtils;
import org.openqa.selenium.WebDriver;

/**
 * @author Mandeep Kumar
 * @description All page classes should be extended by this class. It will have handle common elements to all the pages
 */
public class BasePage {

    protected SeleniumUtils seleniumUtils;
    public static final int maxWaitTimeInSec = 180;
    public static final int implicitWaitSec=15;

    public BasePage(WebDriver driver, String pageObjectRepoFileName) {
        this.seleniumUtils = new SeleniumUtils(driver, pageObjectRepoFileName);
    }

    public SeleniumUtils getSeleniumUtils() {
        return seleniumUtils;
    }
}
