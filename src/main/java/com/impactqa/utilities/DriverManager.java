package com.impactqa.utilities;

import org.openqa.selenium.WebDriver;


/**
 *
 * @version 1.0
 * @description This class maintains driver instances with ThreadSafety
 * @since 2021-03-20
 */
public class DriverManager {

    private static ThreadLocal<WebDriver> ThreadLocalWebDriver = new ThreadLocal<>();

    /**
     * This method is set the driver instance to corresponding to current local Thread
     *
     * @param driver WebDriver instance
     */
    public static void setWebDriver(WebDriver driver) {
        ThreadLocalWebDriver.set(driver);
    }


    /**
     * This method returns Web Driver instance related to the current local Thread
     *
     * @return WebDriver instance
     */
    public static WebDriver getWebDriver() {
        return ThreadLocalWebDriver.get();
    }
}
