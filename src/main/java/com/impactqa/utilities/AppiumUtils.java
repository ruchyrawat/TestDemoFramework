package com.impactqa.utilities;

import com.google.common.collect.ImmutableMap;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.qameta.allure.model.Status;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import com.impactqa.utilities.PageObjectRepoHelper.PLATFORM;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.*;


/**
 * @version 1.0
 * @description This class is Util class to interact with Mobile devices
 * @since 2021-03-20
 */
public class AppiumUtils {

    private AppiumDriver driver;
    private PageObjectRepoHelper pageObjectRepoHelper;
    private PLATFORM platform;
    private static final int defaultWaitTimeForPageLoad = FrameworkConfig.getNumberConfigProperty("DefaultWaitTimeForElement");
    private static final int defaultWaitTimeForElement = FrameworkConfig.getNumberConfigProperty("DefaultWaitTimeForElement");

    public AppiumUtils(AppiumDriver driver, String pageObjectRepoFileName, PLATFORM platform) {
        this.driver = driver;
        this.platform = platform;
        pageObjectRepoHelper = new PageObjectRepoHelper(pageObjectRepoFileName, platform);
    }

    /**
     * Will get the text from element @locatorName
     * <p>
     * It will check whether element is displayed with the timeout waitForElementToDisplay before performing the action
     * waitForElementToDisplay should be set in the config file located at src/test/resources/config.properties
     *
     * @param locatorName -  Element name defined in the page Object repository file
     * @return String value from element
     */
    @Step("Get text from element '{0}'")
    public String getText(String locatorName) {

        WebElement element = waitForElementToDisplay(locatorName);
        return element.getText();
    }

    /**
     * Will get the all the texts from element @locatorName
     * <p>
     * It will check whether element is displayed with the timeout defaultWaitTimeForElement before performing the action
     * waitForElementToDisplay should be set in the config file located at src/test/resources/config.properties
     *
     * @param locatorName -  Element name defined in the page Object repository file
     * @return list of strings from element
     */
    @Step("Get text from list of elements '{0}'")
    public List<String> getTextOfListElements(String locatorName) {
        List<String> textList = new LinkedList<>();
        By locBy = pageObjectRepoHelper.getObject(locatorName);
        List<WebElement> elements = new WebDriverWait(driver, defaultWaitTimeForElement).until(ExpectedConditions.presenceOfAllElementsLocatedBy(locBy));
        for (WebElement element : elements)
            textList.add(element.getText().trim());
        return textList;
    }

    /**
     * Will get the all the texts from element @locatorName
     * <p>
     * It will check whether element is displayed with the timeout defaultWaitTimeForElement before performing the action
     * waitForElementToDisplay should be set in the config file located at src/test/resources/config.properties
     *
     * @param locatorName -  Element name defined in the page Object repository file
     * @return number of element presents
     */
    @Step("Get number of elements")
    public int getNumberOfElements(String locatorName) {
        By locBy = pageObjectRepoHelper.getObject(locatorName);
        List<WebElement> elements = new WebDriverWait(driver, defaultWaitTimeForElement).until(ExpectedConditions.presenceOfAllElementsLocatedBy(locBy));
        return elements.size();
    }

    /**
     * Will get the attribute value from the @locatorName using @attributeName
     * <p>
     * It will check whether the element is displayed with the timeout waitForElementToDisplay before performing the action
     * waitForElementToDisplay should be set in the config file located at src/test/resources/config.properties
     *
     * @param locatorName -  Element name defined in the page Object repository file
     * @param attributeName - The attribute name of the element
     * @return a String - value of the particular attribute name
     */
    @Step("Get attribute '{1}' from element '{0}'")
    public String getAttribute(String locatorName, String attributeName) {
        if(!CommonUtil.isValidString(attributeName))
            Assert.fail("Attribute name should not be null or empty");
        WebElement element = waitForElementToPresent(locatorName);
        return element.getAttribute(attributeName);
    }

    /**
     * Verify the expected and the actual value that are matching exactly
     * <p>
     * It will check whether element is displayed with the timeout waitForElementToDisplay before performing the action
     * waitForElementToDisplay should be set in the config file located at src/test/resources/config.properties
     *
     * @param locatorName -  Element name defined in the page Object repository file
     * @param expectedText - The text which is expected to match
     */
    @Step("Verify text of element '{0}'")
    public void verifyText(String locatorName, String expectedText) {
        if(!CommonUtil.isValidString(expectedText))
            Assert.fail("Expected text should not be Null or Empty");
        String actualText = getText(locatorName).trim();
        Assert.assertTrue(actualText.equalsIgnoreCase(expectedText.trim()), "text not matched. ActualText: " + actualText + "'. Expected: '" + expectedText + "'\n");
    }

    /**
     * Will verify the expected and the actual value with the help of assert class
     * <p>
     * It will check whether element is displayed with the timeout waitForElementToDisplay before performing the action
     * waitForElementToDisplay should be set in the config file located at src/test/resources/config.properties
     *
     * @param locatorName -  Element name defined in the page Object repository file
     * @param expectedText - The text which is expected to partially match
     */
    @Step("Verify partial text of element '{0}'")
    public void verifyPartialText(String locatorName, String expectedText) {
        if(!CommonUtil.isValidString(expectedText))
            Assert.fail("Expected text should not be Null or Empty");
        String actualText = getText(locatorName).trim();
        Assert.assertTrue(actualText.contains(expectedText.trim()), "partial text not matched. ActualText: '" + actualText + "'. Expected(Partial): '" + expectedText + "'\n");
    }

    /**
     * Will click the element @locatorName
     * <p>
     * It will check whether element is displayed and clickable with the timeout waitForElementToDisplay before performing the action
     * waitForElementToDisplay should be set in the config file located at src/test/resources/config.properties
     *
     * @param locatorName - the name of the element in the repository file
     */
    @Step("Click '{0}'")
    public void click(String locatorName) {
        WebElement element = waitForElementToBeClickable(locatorName);
        element.click();
    }

    /**
     * Waits till the dynamic element @locatorName is to be clickable.
     * <p>
     * The dynamic button or element has been waited to be displayed and then clicked.
     *
     * The final element will be created based upon the values @locatorName and @replaceKeyVal
     * Let's say the Locator type is xpath like //*[@id='$$countryName$$'] and the key replaced value is Map.Of("$$countryName$$","USA")
     * then the final xpath will be created like this //*[@id='USA'] and the dynamic element will be created in run time.
     *
     * @param locatorName -  Element name defined in the page Object repository file
     * @param customName    - Name in the report
     * @param replaceKeyVal - A Dictionary/Map that contains the patterns (Keys) to be replaced by respective values
     */
    @Step("Click '{1}'")
    public void click(String locatorName, String customName, Map<String, String> replaceKeyVal) {
        WebElement element = waitForElementToBeClickable(locatorName, customName, replaceKeyVal);
        element.click();
    }

    /**
     * Will long click the element @locatorName
     * <p>
     * It will wait for the element to be clickable with the timeout defaultWaitTimeForElement before performing the action
     * defaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     *
     * @param locatorName -  Element name defined in the page Object repository file
     */
    @Step("longClick '{0}'")
    public void longClick(String locatorName) {
        WebElement element = waitForElementToBeClickable(locatorName);
        TouchAction action = new TouchAction(driver);
        action.longPress(LongPressOptions.longPressOptions().withElement(ElementOption.element(element)));
        action.release().perform();
    }

    /**
     * Will drag element @from and drop to @to
     * <p>
     * It will wait for the element to be clickable with the timeout defaultWaitTimeForElement before performing the action
     * defaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     *
     * @param from -  Element name from where to drag
     * @param to - Element name where to drop
     *
     */
    @Step("Will drag element {0} from and drop to {1} to")
    public void dragAndDrop(String from, String to) {
        WebElement fromElement = waitForElementToBeClickable(from);
        WebElement toElement = waitForElementToDisplay(to);
        Actions builder = new Actions(driver);
        Allure.step("Drag element from " + fromElement + " and drop " + toElement);
        builder.dragAndDrop(fromElement, toElement).perform();
    }

    @Step("Drag And Drop")
    public void dragAndDrop2(String elementDrag, String elementDrop){

//        WebElement elem1 = waitForElementToDisplay(elementDrag);
//        WebElement elem2 = waitForElementToDisplay(elementDrop);
        WebElement elem1 = waitForElementToPresent(elementDrag);
        WebElement elem2 = waitForElementToPresent(elementDrop);
        TouchAction touchAction = new TouchAction(driver);
        touchAction.press(ElementOption.element(elem1))
                .waitAction(WaitOptions.waitOptions(Duration.ofSeconds(2)))
                .moveTo(ElementOption.element(elem2))
                .release()
                .perform();
    }

    /**
     * It will wait for the dynamic element to be clickable and click it with the timeout defaultWaitTimeForElement before performing the action
     * defaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     *
     * The final element will be created based upon the values @locatorName and @replaceKeyVal
     * Let's say the Locator type is xpath like //*[@id='$$countryName$$'] and the key replaced value is Map.Of("$$countryName$$","USA")
     * then the final xpath will be created like this //*[@id='USA'] and the dynamic element will be created in run time.
     *
     * @param locatorName -  Element name defined in the page Object repository file
     * param customName    - Name in the report
     * @param replaceKeyVal - A Dictionary/Map that contains the patterns (Keys) to be replaced by respective values
     */
    @Step("longClick '{1}'")
    public void longClick(String locatorName, String customName, Map<String, String> replaceKeyVal) {
        WebElement element = waitForElementToBeClickable(locatorName, customName, replaceKeyVal);
        TouchAction action = new TouchAction(driver);
        action.longPress(LongPressOptions.longPressOptions().withElement(ElementOption.element(element)));
        action.release().perform();
    }

    /**
     * It will click at the co-ordinates that you are given
     *
     * @param x          - x axis value
     * @param y          - y axis value
     * @param customName - Name in the report
     */
    @Step("Click '{2}' using Coordinates x:'{0}' y:'{1}'")
    public void clickAtCoordinates(int x, int y, String customName) {
        TouchAction ta = new TouchAction<>(driver);
        ta.tap(new PointOption().withCoordinates(x, y));
        ta.perform();
    }

    /**
     * It will get the particular Mobile element with the help of the @locatorName
     * It will wait for the dynamic element to be clickable with the timeout defaultWaitTimeForElement before performing the action
     * defaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     *
     * @param locatorName -  Element name defined in the page Object repository file
     * @return MobileElement
     */
    @Step("Get MobileElement '{0}'")
    public MobileElement getMobileElement(String locatorName) {
        WebElement element = waitForElementToDisplay(locatorName);
        return ((MobileElement) element);
    }

    /**
     * Activates the given app if it installed, but not running or if it is running in the background.
     * @param packageOrBundleID – the bundle identifier (or app id) of the app to activate.
     */
    @Step("Activate App '{0}'")
    public void activateApp(String packageOrBundleID) {
        driver.activateApp(packageOrBundleID);
    }

    /**
     * It will wait for @miliseconds milliseconds
     *
     * @param miliseconds - time in milliseconds
     */
    @Step("Sleep for '{0}' milliseconds")
    public void sleepForMiliseconds(long miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
        }
    }

    /**
     * It will verify whether the app is installed in the device or not and gives true if app is there
     *
     * @param packageOrBundleID – the bundle identifier (or app id) of the app to activate.
     * @return true if the app is installed else false
     */
    @Step("Is App Installed '{0}'")
    public boolean isAppInstalled(String packageOrBundleID) {
        return driver.isAppInstalled(packageOrBundleID);
    }

    /**
     * It will install the app in our mobile device
     *
     * @param appPath - location the application package(apk/ipa) in our mobile device
     */
    @Step("Install app '{0}'")
    public void installApp(String appPath) {
        driver.installApp(appPath);
    }

    /**
     * Launches the app, which was provided in the capabilities at session creation, and (re)starts the session.
     */
    @Step("Launch Main App")
    public void launchApp() {
        driver.launchApp();
    }

    /**
     * It will uninstall the app with the package or bundle id @packageOrBundleID
     *
     * @param packageOrBundleID – the bundle identifier (or app id) of the app to uninstall.
     */
    @Step("Uninstall the App '{0}'")
    public void unInstallApp(String packageOrBundleID) {
        driver.removeApp(packageOrBundleID);
    }

    /**
     * It will pull the particular file from the path @remoteFilepath
     *
     * For Eg: In android the path will be something like /sdcard/Download/test.txt
     *         In IOS the path will look like @com.apple.Keynote:documents/test.txt
     *
     * @param remoteFilepath - The location of the file in the device that has to be pulled.
     * @return - String value of the local path of the file
     */
    @Step("pull file from path {0}")
    public String pullFile(String remoteFilepath) {
        byte[] fileContent = driver.pullFile(remoteFilepath);
        String filename = new File(remoteFilepath).getName();
        String localFilePath = "./temp/" + filename;
        try {
            FileUtils.writeByteArrayToFile(new File("./temp/" + filename), fileContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return localFilePath;
    }

    /**
     * It will push the file from @localFilePath to @remotePath(device)
     *
     * For Eg: In android the path will be something like /sdcard/Download/test.txt
     *         In IOS the path will look like @com.apple.Keynote:documents/test.txt
     *
     * @param remotePath    - The location of the file in the device that has to be pushed.
     * @param localFilePath - the location of the file in our local path
     */
    @Step("push file from path {0}")
    public void pushFile(String remotePath, String localFilePath) {

        try {
            if (platform == PLATFORM.IOS)
                ((IOSDriver) driver).pushFile(remotePath, new File(localFilePath));
            if (platform == PLATFORM.ANDROID)
                ((AndroidDriver) driver).pushFile(remotePath, new File(localFilePath));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * It will delete all the files from the download folder in the sd card
     *
     * This is applicable only for Android
     */
//    @Step("Delete Files In Download Folder Android")
//    public void deleteFilesInDownloadFolderAndroid() {
//
//        try {
//            if (platform == PLATFORM.ANDROID) {
//                Map<String, Object> args = Map.of("command", "rm",
//                        "args", List.of("/sdcard/Download/*")
//                );
//                driver.executeScript("mobile: shell", args);
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    /**
     * It will accept any alert which appears on the page which helps getting over all the alerts
     */
    @Step("Accept If Any Alert Pops")
    public void acceptIfAnyAlerts() {
        try {
            driver.switchTo().alert().accept();
        } catch (Exception e) {
        }
    }

    /**
     * It will get the screen size of the mobile device/remote
     *
     * @return Dimension - Dimension of the mobile device
     */
    @Step("Get Screen Size")
    public Dimension getScreenSize() {
        Dimension windowSize = driver.manage().window().getSize();
        Allure.step("width: '" + windowSize.width + "' . height: '" + windowSize.height + "'", Status.PASSED);
        return windowSize;
    }

    /**
     * It will scroll the screen downwards
     */
    @Step("scrollDown")
    public void scrollDown(int yStaringPercentage, int xEndingPercentage) {
        Dimension windowSize = driver.manage().window().getSize();
        PointOption startPoint = PointOption.point(CommonUtil.getPortionOfTheNumber(windowSize.width, 50),
                CommonUtil.getPortionOfTheNumber(windowSize.height, yStaringPercentage));
        PointOption endPoint = PointOption.point(CommonUtil.getPortionOfTheNumber(windowSize.width, 50),
                CommonUtil.getPortionOfTheNumber(windowSize.width, xEndingPercentage));
        TouchAction touchAction = new TouchAction(driver);
        touchAction.press(startPoint)
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(200)))
                .moveTo(endPoint)
                .release().perform();
    }

    /**
     * It will scroll the screen upwards
     */
    @Step("scrollUp")
    public void scrollUp() {
        Dimension windowSize = driver.manage().window().getSize();
        PointOption startPoint = PointOption.point(CommonUtil.getPortionOfTheNumber(windowSize.width, 50),
                CommonUtil.getPortionOfTheNumber(windowSize.width, 20));
        PointOption endPoint = PointOption.point(CommonUtil.getPortionOfTheNumber(windowSize.width, 50),
                CommonUtil.getPortionOfTheNumber(windowSize.height, 90));
        TouchAction touchAction = new TouchAction(driver);
        touchAction.press(startPoint)
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(200)))
                .moveTo(endPoint)
                .release().perform();
    }

    /**
     * It will scroll and search the element with the text "@text"
     * <p>
     * if exact search is true it will match for the exact value else partial search will happen
     *
     * @param exactSearch - Give true if you want to match the words exactly and false for partial matching
     * @param text        - Text that have to be compared with the actual value
     * @return WebElement
     */
    @Step("scrollAndSearchElementWithText '{0}'")
    public WebElement scrollAndSearchElementWithText(String text, boolean exactSearch) {
        if(!CommonUtil.isValidString(text))
            Assert.fail("Search Text should not be Null or Empty!");
        if (platform == PLATFORM.ANDROID) {
            if (exactSearch)
                return ((AndroidDriver) driver).findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textMatches(\"" + text + "\").instance(0))");
            else
                return ((AndroidDriver) driver).findElementByAndroidUIAutomator("new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView(new UiSelector().textContains(\"" + text + "\").instance(0))");
        } else if (platform == PLATFORM.IOS) {
            // scroll to item
            HashMap scrollObject = new HashMap<>();
            String elementID = null;
            if (exactSearch)
                elementID = ((RemoteWebElement) driver.findElement(MobileBy.iOSNsPredicateString("label == '" + text + "'"))).getId();
            else
                elementID = ((RemoteWebElement) driver.findElement(MobileBy.iOSNsPredicateString("label CONTAINS '" + text + "'"))).getId();
            scrollObject.put("element", elementID);
            scrollObject.put("toVisible", "not an empty string");
            return (WebElement) driver.executeScript("mobile: scroll", scrollObject);
        }
        return null;

    }

    /**
     * It will press the physical home button of the device
     */
    @Step("Press Physical Home Button")
    public void pressDeviceHomeButton() {
        if (platform == PLATFORM.IOS) {
            driver.executeScript("mobile: pressButton", ImmutableMap.of("name", "home"));
            //other supported button actions - volumeup, volumedown
        } else if (platform == PLATFORM.ANDROID) {
            ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.HOME));
        }
    }

    @Step("Swipe Right To Left with in element {0}")
    public void swipeRightToLeft(String locatorName) {
//        WebElement element = waitForElementToDisplay(locatorName);
        WebElement element = waitForElementToPresent(locatorName);
        Dimension elementSize = element.getSize();
        PointOption startPoint = PointOption.point(element.getLocation().x+getPortionOfTheNumber(elementSize.width, 70),
                element.getLocation().y+getPortionOfTheNumber(elementSize.height, 50));
        PointOption endPoint = PointOption.point(element.getLocation().x+getPortionOfTheNumber(elementSize.width, 30),
                element.getLocation().y+getPortionOfTheNumber(elementSize.height, 50));


        TouchAction touchAction = new TouchAction(driver);
        touchAction.press(PointOption.point(337, 112))
                .waitAction(WaitOptions.waitOptions(Duration.ofMillis(800)))
                .moveTo(PointOption.point(191, 108))
                .release().perform();

    }

    /**
     *
     * Waits till the element @locatorName is clickable. With the timeout defaultWaitTimeForElement
     * defaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     *
     * @param locatorName -  Element name defined in the page Object repository file
     * @return WebElement
     */
    @Step("Wait For Element '{0}' To Be Clickable")
    public WebElement waitForElementToBeClickable(String locatorName) {
        By locBy = pageObjectRepoHelper.getObject(locatorName);
        WebElement element = waitForElementToDisplay(locatorName);
        return new WebDriverWait(driver, defaultWaitTimeForElement).until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Waits till the dynamic element @locatorName is clickable. With the timeout defaultWaitTimeForElement
     * defaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     *
     * The final element will be created based upon the values @locatorName and @replaceKeyVal
     * Let's say the Locator type is xpath like //*[@id='$$countryName$$'] and the key replaced value is Map.Of("$$countryName$$","USA")
     * then the final xpath will be created like this //*[@id='USA'] and the dynamic element will be created in run time.
     *
     * @param locatorName   - Element name defined in the page Object repository file
     * @param customName    - Name in the report
     * @param replaceKeyVal - A Dictionary/Map that contains the patterns (Keys) to be replaced by respective values
     * @return WebElement
     */
    @Step("Wait For Element '{1}' To Be Clickable")
    public WebElement waitForElementToBeClickable(String locatorName, String customName, Map<String, String> replaceKeyVal) {
        By locBy = pageObjectRepoHelper.getObject(locatorName, replaceKeyVal);
        WebElement element = waitForElementToDisplay(locatorName, customName, replaceKeyVal);
        return new WebDriverWait(driver, defaultWaitTimeForElement).until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Waits till element @locatorName is displayed. With the timeout   defaultWaitTimeForElement
     * defaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     *
     * @param locatorName -  Element name defined in the page Object repository file
     * @return WebElement object
     */
    @Step("Wait For Element '{0}' To Display")
    public WebElement waitForElementToDisplay(String locatorName) {
        By locBy = pageObjectRepoHelper.getObject(locatorName);
        return new WebDriverWait(driver, defaultWaitTimeForElement).until(ExpectedConditions.visibilityOfElementLocated(locBy));
    }

    /**
     * Waits till the dynamic element @locatorName is displayed. With the timeout defaultWaitTimeForElement
     * defaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     * <p>
     * The final element will be created based upon the values @locatorName and @replaceKeyVal
     * Let's say the Locator type is xpath like //*[@id='$$countryName$$'] and the key replaced value is Map.Of("$$countryName$$","USA")
     * then the final xpath will be created like this //*[@id='USA'] and the dynamic element will be created in run time.
     *
     * @param locatorName -  Element name defined in the page Object repository file
     * @param customName    - Name in the report
     * @param replaceKeyVal - A Dictionary/Map that contains the patterns (Keys) to be replaced by respective values
     * @return WebElement object
     */
    @Step("Wait For Element '{1}' To Display")
    public WebElement waitForElementToDisplay(String locatorName, String customName, Map<String, String> replaceKeyVal) {
        By locBy = pageObjectRepoHelper.getObject(locatorName, replaceKeyVal);
        return new WebDriverWait(driver, defaultWaitTimeForElement).until(ExpectedConditions.visibilityOfElementLocated(locBy));
    }


    /**
     * This method will return the status of the element whether it is displayed or not.
     * It will wait for the element to be displayed till @waitTimeInSeconds in seconds before returning the status.
     *
     * @param locatorName - Element name defined in the page Object repository file
     * @param waitTimeInSeconds    - Time to wait for the element in seconds before returning the status.
     * @return true/false - ture:Displayed | false:Not Displayed
     */
    @Step("is Element '{0}' Displayed")
    public boolean isElementDisplayed(String locatorName, int waitTimeInSeconds) {
        By locBy = pageObjectRepoHelper.getObject(locatorName);
        try {
            WebElement ele = new WebDriverWait(driver, waitTimeInSeconds).until(ExpectedConditions.visibilityOfElementLocated(locBy));
            return ele.isDisplayed();
        } catch (Exception e) {
            return false;
        }

    }

    /**
     * It will wait for the element to be displayed till @waitTime in seconds before returning the status.
     *
     * The final dynamic element will be created based upon the values @locatorName and @replaceKeyVal
     * Let's say the Locator type is xpath like //*[@id='$$countryName$$'] and the key replaced value is Map.Of("$$countryName$$","USA")
     * then the final xpath will be created like this //*[@id='USA'] and the dynamic element will be created in run time.
     *
     * @param locatorName - Element name defined in the page Object repository file
     * @param waitTime    - Time to wait for the element in seconds before returning the status.
     * @param customName    - Name in the report
     * @param replaceKeyVal - A Dictionary/Map that contains the patterns (Keys) to be replaced by respective values
     * @return true/false - ture:Displayed | false:Not Displayed
     */
    @Step("is Element '{2}' Displayed")
    public boolean isElementDisplayed(String locatorName, int waitTime, String customName, Map<String, String> replaceKeyVal) {
        By locBy = pageObjectRepoHelper.getObject(locatorName, replaceKeyVal);
        try {
            WebElement ele = new WebDriverWait(driver, waitTime).until(ExpectedConditions.visibilityOfElementLocated(locBy));
            return ele.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Waits till the element at @locatorName to be present with the timeout of defaultWaitTimeForElement
     * defaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     *
     * @param locatorName -  Element name defined in the page Object repository file
     * @return WebElement object
     */
    @Step("Wait For Element '{0}' To Present")
    public WebElement waitForElementToPresent(String locatorName) {
        By locBy = pageObjectRepoHelper.getObject(locatorName);
        return new WebDriverWait(driver, defaultWaitTimeForElement).until(ExpectedConditions.presenceOfElementLocated(locBy));
    }

    /**
     * Waits till the dynamic element @locatorName to be present with the timeout of defaultWaitTimeForElement
     * defaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     *
     * The final dynamic element will be created based upon the values @locatorName and @replaceKeyVal
     * Let's say the Locator type is xpath like //*[@id='$$countryName$$'] and the key replaced value is Map.Of("$$countryName$$","USA")
     * then the final xpath will be created like this //*[@id='USA'] and the dynamic element will be created in run time.
     *
     * @param locatorName -  Element name defined in the page Object repository file
     * @param customName    - Name in the report
     * @param replaceKeyVal - A Dictionary/Map that contains the patterns (Keys) to be replaced by respective values
     * @return WebElement object
     */
    @Step("Wait For Element '{1}' To Present")
    public WebElement waitForElementToPresent(String locatorName, String customName, Map<String, String> replaceKeyVal) {
        By locBy = pageObjectRepoHelper.getObject(locatorName, replaceKeyVal);
        return new WebDriverWait(driver, defaultWaitTimeForElement).until(ExpectedConditions.presenceOfElementLocated(locBy));
    }

    /**
     * Gets the Device Model Name of the device tha we are using
     *
     * @return String - The name of the Device Model
     */
    public String getDeviceModelName() {
        String ret = "";
        Capabilities cap = driver.getCapabilities();
        if (cap.getCapability("deviceModel") != null)
            ret = (String) cap.getCapability("deviceModel");
        return ret;
    }

    /**
     * Get the screenshot and attach to the report with the name @name
     *
     * @param name - Name of the attachment in the report
     */
    public void attachScreenShotToTheReport(String name) {
        TakesScreenshot tk = (TakesScreenshot) driver;
        byte[] b = tk.getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment(name, "image/png", new ByteArrayInputStream(b), "png");
    }

    /**
     * Get the screenshot and attach to the report with the name @name
     */
    @Step("Press Back Button")
    public void pressBackButton() {
        driver.navigate().back();
    }

    /**
     * Get the screenshot and attach to the report with the name @name
     */
    @Step("Press Enter Key")
    public void pressEnterKey() {
        driver.getKeyboard().pressKey(Keys.ENTER);
    }

    /**
     * This method enters the text at @locatorName. To find the element it calls waitForElementToDisplay With the timeout defaultWaitTimeForElement.
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     * <p>
     * Checks for @value to be Valid string.
     * Checks WebElement is enabled or disabled.
     *
     * @param locatorName - Element name defined in the page Object repository file
     * @param value       - Value that has to set in the Text box
     */
    @Step("Enter '{0}' with the value '{1}'")
    public void enterText(String locatorName, String value) {
        if (!CommonUtil.isValidString(value))
            Assert.fail("Value to be enter should not be null or empty");
        WebElement element = waitForElementToDisplay(locatorName);
        if (!element.isEnabled())
            Assert.fail("Element " + locatorName + " is disabled");
        element.clear();
        element.sendKeys(value);
    }

    /**
     * This method enters encrypted text in to @locatorName. To find element it calls waitForElementToDisplay With the timeout defaultWaitTimeForElement
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     * <p>
     * Decrypts the @value and then enters in the Text Box
     * Checks for @value to be Valid string.
     * Checks WebElement is enabled or disabled.
     *
     * @param locatorName - Element name defined in the page Object repository file
     * @param value       - Encrypted value that has to set in Text box
     */
    @Step("Enter '{0}' with the value ******")
    public void enterEncryptedText(String locatorName, String value) {
        String password = null;
        try {
            password = CryptoUtils.decryptTheValue(value);
        } catch (Exception e) {
            throw new RuntimeException("Error Occurred while decrypting the password", e);
        }
        if (!CommonUtil.isValidString(password))
            Assert.fail("Value to be enter should not be null or empty");
        WebElement element = waitForElementToDisplay(locatorName);
        if (!element.isEnabled())
            Assert.fail("Element " + locatorName + " is disabled");
        element.sendKeys(password);
    }

    /**
     * @return Current Platform
    * */
    public PLATFORM getPlatform() {
        return platform;
    }

    private static int getPortionOfTheNumber(int input, int percentange) {

        double inputD = Double.valueOf(input);
        double percentangeD = Double.valueOf(percentange);

        return (int) (inputD * (percentangeD / 100.0));
    }
}
