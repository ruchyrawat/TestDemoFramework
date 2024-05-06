package com.impactqa.page.mobile;

import com.impactqa.utilities.AppiumUtils;
import com.impactqa.utilities.PageObjectRepoHelper;
import io.appium.java_client.AppiumDriver;

/**
 *
 * @since   2023-09-12
 * @description All page classes should be extended by this class. It will have handle common elements to all the pages
 */

public class BasePage {

    protected AppiumUtils appiumUtils;
    
    public BasePage(AppiumDriver driver, String pageObjectRepoFileName, PageObjectRepoHelper.PLATFORM platform)
    {
        this.appiumUtils = new AppiumUtils(driver, pageObjectRepoFileName, platform);
    }

    public AppiumUtils getAppiumUtils(){
        return appiumUtils;
    }

}
