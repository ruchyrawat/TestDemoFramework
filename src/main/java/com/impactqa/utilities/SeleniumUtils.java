package com.impactqa.utilities;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import com.impactqa.utilities.PageObjectRepoHelper.PLATFORM;

import java.io.ByteArrayInputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.impactqa.page.web.BasePage.implicitWaitSec;
import static com.impactqa.page.web.BasePage.maxWaitTimeInSec;

/**
 * @description This class is Util class to interact with browser
 * @since 2024-03-20
 */

public class SeleniumUtils {
    private WebDriver driver;
    private PageObjectRepoHelper pageObjectRepoHelper;
    private static final int defaltWaitTimeForPageLoad = FrameworkConfig.getNumberConfigProperty("DefaultWaitTimeForPageLoad");
    private static final int defaltWaitTimeForElement = FrameworkConfig.getNumberConfigProperty("DefaultWaitTimeForElement");
    public SeleniumUtils() {
    }

    public SeleniumUtils(WebDriver driver, String pageObjectRepoFileName) {
        this.driver = driver;
        pageObjectRepoHelper = new PageObjectRepoHelper(pageObjectRepoFileName, PLATFORM.WEB);
    }

    /**
     * Waits till element @locatorName is displayed. With the timeout defaultWaitTimeForElement
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     *
     * @param locatorName - Element name defined in the page Object repository file
     * @return WebElement object
     */
    @Step("Wait For Element '{0}' To Display")
    public WebElement waitForElementToDisplay(String locatorName) {
        By locBy = pageObjectRepoHelper.getObject(locatorName);
        return new WebDriverWait(driver, defaltWaitTimeForElement).until(ExpectedConditions.visibilityOfElementLocated(locBy));
    }

    /**
     * Waits till element @locatorName is displayed. With the timeout defaultWaitTimeForElement
     * DefaltWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     * <p>
     * The final element will be created based upon the values @locatorName and @replaceKeyVal
     * Let's say the Locator type is xpath like //*[@id='$$countryName$$'] and the key replaced value is Map.Of("$$countryName$$","USA")
     * then the final xpath will be created like this //*[@id='USA'] and the dynamic element will be created in run time.
     *
     * @param locatorName   - Element name defined in the page Object repository file
     * @param customName    - Name in the report
     * @param replaceKeyVal - A Dictionary/Map that contains the patterns (Keys) to be replaced by respective values
     * @return WebElement
     */
    @Step("Wait For Element '{1}' To Display")
    public WebElement waitForElementToDisplay(String locatorName, String customName, Map<String, String> replaceKeyVal) {
        By locBy = pageObjectRepoHelper.getObject(locatorName, replaceKeyVal);
        return new WebDriverWait(driver, defaltWaitTimeForElement).until(ExpectedConditions.visibilityOfElementLocated(locBy));
    }

    /**
     * Waits till element @locatorName is not displayed. With the timeout defaultWaitTimeForElement
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     *
     * @param locatorName - Element name defined in the page Object repository file
     */
    @Step("Wait For Element '{0}' To Be Not Display")
    public void waitForElementToBeNotDisplayed(String locatorName) {
        try {
            By locBy = pageObjectRepoHelper.getObject(locatorName);
            new WebDriverWait(driver, defaltWaitTimeForElement).until(ExpectedConditions.invisibilityOfElementLocated(locBy));
        } catch (Exception e) {

        }
    }


    /**
     * Waits till element @locatorName is to be present.  With the timeout defaultWaitTimeForElement
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     *
     * @param locatorName - Element name defined in the page Object repository file
     * @return WebElement Object
     */
    @Step("Wait For Element '{0}' To Present")
    public WebElement waitForElementToPresent(String locatorName) {
        By locBy = pageObjectRepoHelper.getObject(locatorName);
        return new WebDriverWait(driver, defaltWaitTimeForElement).until(ExpectedConditions.presenceOfElementLocated(locBy));
    }

    /**
     * Waits till element @locatorName is to be present. With the timeout defaultWaitTimeForElement
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     * <p>
     * The final element will be created based upon the values @locatorName and @replaceKeyVal
     * Let's say the Locator type is xpath like //*[@id='$$countryName$$'] and the key replaced value is Map.Of("$$countryName$$","USA")
     * then the final xpath will be created like this //*[@id='USA'] and the dynamic element will be created in run time.
     *
     * @param locatorName   - Element name defined in the page Object repository file
     * @param customName    - Name in the report
     * @param replaceKeyVal - A Dictionary/Map that contains the patterns (Keys) to be replaced by respective values
     * @return WebElement
     */
    @Step("Wait For Element '{1}' To Present")
    public WebElement waitForElementToPresent(String locatorName, String customName, Map<String, String> replaceKeyVal) {
        By locBy = pageObjectRepoHelper.getObject(locatorName, replaceKeyVal);
        return new WebDriverWait(driver, defaltWaitTimeForElement).until(ExpectedConditions.presenceOfElementLocated(locBy));
    }


    /**
     * Waits till element @locatorName is to be clickable. With the timeout defaultWaitTimeForElement
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     *
     * @param locatorName - Element name defined in the page Object repository file
     * @return WebElement Object
     */
    @Step("Wait For Element '{0}' To Be Clickable")
    public WebElement waitForElementToBeClickable(String locatorName) {
        By locBy = pageObjectRepoHelper.getObject(locatorName);
        WebElement element = waitForElementToDisplay(locatorName);
        return new WebDriverWait(driver, defaltWaitTimeForElement).until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Waits till element @locatorName is to be clickable. With the timeout defaultWaitTimeForElement
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     * <p>
     * The final element will be created based upon the values @locatorName and @replaceKeyVal
     * Let's say the Locator type is xpath like //*[@id='$$countryName$$'] and the key replaced value is Map.Of("$$countryName$$","USA")
     * then the final xpath will be created like this //*[@id='USA'] and the dynamic element will be created in run time.
     *
     * @param locatorName   - Element name defined in the page Object repository file
     * @param customName    - Name in the report
     * @param replaceKeyVal - A Dictionary/Map that contains the patterns (Keys) to be replaced by respective values
     * @return WebElement
     */
    @Step("Wait For Element '{0}' To Be Clickable")
    public WebElement waitForElementToBeClickable(String locatorName, String customName, Map<String, String> replaceKeyVal) {
        By locBy = pageObjectRepoHelper.getObject(locatorName, replaceKeyVal);
        WebElement element = waitForElementToDisplay(locatorName, customName, replaceKeyVal);
        return new WebDriverWait(driver, defaltWaitTimeForElement).until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * This method will return the status of the element whether it is displayed or not.
     * It will wait for the element to be displayed up to @waitTime in seconds before returning the status.
     *
     * @param locatorName - Element name defined in the page Object repository file
     * @param waitTime    - Time to wait for the element in seconds before returning the status.
     * @return true/false - ture:Displayed | false:Not Displayed
     */
    @Step("is Element '{0}' Displayed")
    public boolean isElementDisplayed(String locatorName, int waitTime) {
        By locBy = pageObjectRepoHelper.getObject(locatorName);
        try {
            WebElement ele = new WebDriverWait(driver, waitTime).until(ExpectedConditions.visibilityOfElementLocated(locBy));
            return ele.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * This method will return the status of the element whether it is displayed or not.
     * It will wait for the element to be displayed up to @waitTime in seconds before returning the status.
     * <p>
     * The final element will be created based upon the values @locatorName and @replaceKeyVal
     * Let's say the Locator type is xpath like //*[@id='$$countryName$$'] and the key replaced value is Map.Of("$$countryName$$","USA")
     * then the final xpath will be created like this //*[@id='USA'] and the dynamic element will be created in run time.
     *
     * @param locatorName   - Element name defined in the page Object repository file
     * @param waitTime      - Time to wait for the element in seconds before returning the status.
     * @param replaceKeyVal - A Dictionary/Map that contains the patterns (Keys) to be replaced by respective values
     * @return true/false - ture:Displayed | false:Not Displayed
     */
    @Step("is Element '{2}' Displayed")
    public boolean isElementDisplayed(String locatorName, int waitTime, Map<String, String> replaceKeyVal) {
        By locBy = pageObjectRepoHelper.getObject(locatorName, replaceKeyVal);
        try {
            WebElement ele = new WebDriverWait(driver, waitTime).until(ExpectedConditions.visibilityOfElementLocated(locBy));
            return ele.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * This method enters text in to @locatorName. To find element it calls waitForElementToDisplay With the timeout defaultWaitTimeForElement
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     * <p>
     * Checks for @value to be Valid string.
     * Checks WebElement is enabled or disabled.
     *
     * @param locatorName - Element name defined in the page Object repository file
     * @param value       - Value that has to set in Text box
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
     * This method enters text in to @locatorName. To find element it calls waitForElementToDisplay With the timeout defaultWaitTimeForElement
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     * <p>
     * Checks for @value to be Valid string.
     * Checks WebElement is enabled or disabled.
     *
     * @param value       - Value that has to set in Text box
     */
    @Step("Enter the value '{1}'")
    public void enterTextWithAction(String value) {
        if (!CommonUtil.isValidString(value))
            Assert.fail("Value to be enter should not be null or empty");
        Actions actions = new Actions(driver);
        actions.sendKeys(value).build().perform();
    }

    /**
     * This method enters text in to @locatorName. To find element it calls waitForElementToDisplay With the timeout defaultWaitTimeForElement
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     * <p>
     * The final element will be created based upon the values @locatorName and @replaceKeyVal
     * Let's say the Locator type is xpath like //*[@id='$$countryName$$'] and the key replaced value is Map.Of("$$countryName$$","USA")
     * then the final xpath will be created like this //*[@id='USA'] and the dynamic element will be created in run time.
     * <p>
     * Checks for @value to be Valid string.
     * Checks WebElement is enabled or disabled.
     *
     * @param locatorName   - Element name defined in the page Object repository file
     * @param value         - Value that has to set in Text box
     * @param customName    - Name in the report
     * @param replaceKeyVal - A Dictionary/Map that contains the patterns (Keys) to be replaced by respective values
     */
    @Step("Enter '{1}' with the value '{3}'")
    public void enterText(String locatorName, String customName, Map<String, String> replaceKeyVal, String value) {
        if (!CommonUtil.isValidString(value))
            Assert.fail("Value to be enter should not be null or empty");
        WebElement element = waitForElementToDisplay(locatorName, customName, replaceKeyVal);
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
     * This method enters Date in to @locatorName. To find element it calls waitForElementToDisplay With the timeout defaultWaitTimeForElement
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     * <p>
     * This method is applicable for the HTML element <input type="date"/>
     * Converts @date in format of (yyyy-MM-dd)
     * Then it calls JavaScriptExecutor and sets the Date
     *
     * @param locatorName - Element name defined in the page Object repository file
     * @param date        - Date that has to be set in Date Field
     */
    @Step("Set Date Field {0}'")
    public void setDateField(String locatorName, Calendar date) {
        WebElement element = waitForElementToDisplay(locatorName);
        if ("input".equals(element.getTagName()) && "date".equals(element.getAttribute("type"))) {
            String dateString = new SimpleDateFormat("yyyy-MM-dd").format(date.getTime());
            ((JavascriptExecutor) driver).executeScript("arguments[0].value='" + dateString + "'", element);
        } else {
            Assert.fail("The element should be <input type=\"date\"/>");
        }
    }

    /**
     * This method selects value from Select Drop Down with @locatorName.
     * To find element it calls waitForElementToDisplay With the timeout defaultWaitTimeForElement
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     * <p>
     * Checks for @value to be Valid string.
     * Checks WebElement is enabled or disabled.
     *
     * @param locatorName - Element name defined in the page Object repository file
     * @param value       - Value that has to select from Select Drop Down
     */
    @Step("Select dropdown '{0}' with the value '{1}'")
    public void selectDropdown(String locatorName, String value) {
        if (!CommonUtil.isValidString(value))
            Assert.fail("Option to be selected should not be null or empty");
        WebElement element = waitForElementToDisplay(locatorName);
        if (!element.isEnabled())
            Assert.fail("Element " + locatorName + " is disabled");
        Select select = new Select(element);
        select.selectByVisibleText(value);
    }


    /**
     * This method Selects Checkbox with @locatorName.
     * To find element it calls waitForElementToDisplay With the timeout defaultWaitTimeForElement
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     *
     * @param locatorName  - Element name defined in the page Object repository file
     * @param checkUncheck - Value should be "check" or "uncheck"
     */
    @Step("'{1}' the checkbox '{0}'")
    public void selectCheckbox(String locatorName, String checkUncheck) {
        if (!CommonUtil.isValidString(checkUncheck))
            Assert.fail("Value to be enter should not be null or empty");
        else if (!(("check".equals(checkUncheck)) || "uncheck".equals(checkUncheck)))
            Assert.fail("Value to be check or uncheck");

        WebElement element = waitForElementToDisplay(locatorName);
        if (!element.isSelected() && "check".equals(checkUncheck))
            element.click();
        else if (element.isSelected() && "uncheck".equals(checkUncheck))
            element.click();

    }

    /**
     * This method Selects Checkbox using JavaScriptExecutor with @locatorName.
     * To find element it calls waitForElementToDisplay With the timeout defaultWaitTimeForElement
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     * <p>
     * Calls JavaScriptExecutor to check or uncheck
     *
     * @param locatorName  - Element name defined in the page Object repository file
     * @param checkUncheck - Value should be "check" or "uncheck"
     */
    @Step("'{1}' the checkbox '{0}'")
    public void selectCheckboxJS(String locatorName, String checkUncheck) {
        if (!CommonUtil.isValidString(checkUncheck))
            Assert.fail("Value to be enter should not be null or empty");
        else if (!(("check".equals(checkUncheck)) || "uncheck".equals(checkUncheck)))
            Assert.fail("Value to be check or uncheck");

        WebElement element = waitForElementToPresent(locatorName);
        if (!element.isSelected() && "check".equals(checkUncheck))
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
        else if (element.isSelected() && "uncheck".equals(checkUncheck))
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /**
     * This method clicks the element
     * Waits till element @locatorName to be clickable before performing the action.
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     * <p>
     *
     * @param locatorName - Element name defined in the page Object repository file
     */
    @Step("Click '{0}'")
    public void click(String locatorName) {
        WebElement element = waitForElementToBeClickable(locatorName);
        element.click();
    }


    /**
     * This method clicks the dynamic element
     * Waits till element @locatorName to be clickable before performing the action.
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     * <p>
     * The final element will be created based upon the values @locatorName and @replaceKeyVal
     * Let's say the Locator type is xpath like //*[@id='$$countryName$$'] and the key replaced value is Map.Of("$$countryName$$","USA")
     * then the final xpath will be created like this //*[@id='USA'] and the dynamic element will be created in run time.
     *
     * @param locatorName   - Element name defined in the page Object repository file
     * @param customName    - Name in the report
     * @param replaceKeyVal - A Dictionary/Map that contains the patterns (Keys) to be replaced by respective values
     * @return WebElement
     */
    @Step("Click '{1}'")
    public void click(String locatorName, String customName, Map<String, String> replaceKeyVal) {
        WebElement element = waitForElementToBeClickable(locatorName, customName, replaceKeyVal);
        element.click();
    }


    /**
     * This method click the element using JavaScriptExecutor
     * Waits till element @locatorName to be clickable before performing the action.
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     * <p>
     *
     * @param locatorName - Element name defined in the page Object repository file
     */
    @Step("JS Click Element {0}'")
    public void javaScriptClick(String locatorName) {
        WebElement element = waitForElementToPresent(locatorName);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /**
     * This method clicks the dynamic element using JavaScriptExecutor
     * Waits till element @locatorName to be clickable before performing the action.
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     * <p>
     * The final element will be created based upon the values @locatorName and @replaceKeyVal
     * Let's say the Locator type is xpath like //*[@id='$$countryName$$'] and the key replaced value is Map.Of("$$countryName$$","USA")
     * then the final xpath will be created like this //*[@id='USA'] and the dynamic element will be created in run time.
     *
     * @param locatorName   - Element name defined in the page Object repository file
     * @param customName    - Name in the report
     * @param replaceKeyVal - A Dictionary/Map that contains the patterns (Keys) to be replaced by respective values
     * @return WebElement
     */
    @Step("Click '{1}'")
    public void javaScriptClick(String locatorName, String customName, Map<String, String> replaceKeyVal) {
        WebElement element = waitForElementToBeClickable(locatorName, customName, replaceKeyVal);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    /**
     * This Method waits alert to be present and accepts the alert
     */
    @Step("Accept the Alert")
    public void acceptAlert() {
        new WebDriverWait(driver, defaltWaitTimeForElement)
                .until(ExpectedConditions.alertIsPresent())
                .accept();
    }


    /**
     * This Method waits alert to be present and dismiss the alert
     */
    @Step("Accept the Alert")
    public void dismissAlert() {
        new WebDriverWait(driver, defaltWaitTimeForElement)
                .until(ExpectedConditions.alertIsPresent())
                .dismiss();
    }

    /**
     * This Method waits alert to be present and reads the value from alert
     * <p>
     *
     * @return String
     */
    @Step("Accept the Alert")
    public String getTextFromAlert() {
        String value = new WebDriverWait(driver, defaltWaitTimeForElement)
                .until(ExpectedConditions.alertIsPresent())
                .getText();
        return value;
    }

    /**
     * This Method waits alert to be present and dismiss the alert
     *
     * @param value - value that has to be set in the alert
     */
    @Step("Accept the Alert")
    public void setTextToAlert(String value) {
        new WebDriverWait(driver, defaltWaitTimeForElement)
                .until(ExpectedConditions.alertIsPresent()).sendKeys(value);
    }


    /**
     * This method gets the text from Element.
     * Waits till element @locatorName to be displayed before performing the action.
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     * <p>
     *
     * @param locatorName - Element name defined in the page Object repository file
     * @return String
     */
    @Step("Get text from element '{0}'")
    public String getText(String locatorName) {
        WebElement element = waitForElementToDisplay(locatorName);
        return element.getText();
    }

    /**
     * This method return the text value of the dynamic element
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     * <p>
     * The final element will be created based upon the values @locatorName and @replaceKeyVal
     * Let's say the Locator type is xpath like //*[@id='$$columnName$$'] and the key replaced value is Map.Of("$$columnName$$","Location")
     * then the final xpath will be created like this //*[@id='Location'] and the dynamic element will be created in run time.
     *
     * @param locatorName   - Element name defined in the page Object repository file
     * @param customName    - Name in the report
     * @param replaceKeyVal - A Dictionary/Map that contains the patterns (Keys) to be replaced by respective values
     * @return value
     */
    @Step("Get text from element '{0}'")
    public String getText(String locatorName, String customName, Map<String, String> replaceKeyVal) {
        WebElement element = waitForElementToDisplay(locatorName, customName, replaceKeyVal);
        return element.getText();
    }

    /**
     * It will get attribute value of the element.
     * Waits till element @locatorName is to be Displayed before performing the action.
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     * <p>
     *
     * @param locatorName   - Element name defined in the page Object repository file
     * @param attributeName - Attribute name
     * @return String
     */
    @Step("Get attribute {1} from element '{0}'")
    public String getAttribute(String locatorName, String attributeName) {
        WebElement element = waitForElementToDisplay(locatorName);
        return element.getAttribute(attributeName);
    }

    /**
     * This method return the attribute value of the dynamic element
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     * <p>
     * The final element will be created based upon the values @locatorName and @replaceKeyVal
     * Let's say the Locator type is xpath like //*[@id='$$columnName$$'] and the key replaced value is Map.Of("$$columnName$$","Location")
     * then the final xpath will be created like this //*[@id='Location'] and the dynamic element will be created in run time.
     *
     * @param locatorName   - Element name defined in the page Object repository file
     * @param customName    - Name in the report
     * @param replaceKeyVal - A Dictionary/Map that contains the patterns (Keys) to be replaced by respective values
     * @return true/false - ture:Enable | false:Disable
     */
    @Step("Get attribute from element '{0}'")
    public String getAttribute(String locatorName, String customName, Map<String, String> replaceKeyVal, String attributeName) {
        WebElement element = waitForElementToDisplay(locatorName, customName, replaceKeyVal);
        return element.getAttribute(attributeName);
    }

    /**
     * This method verifies Text. it reads the value from @locatorName by calling getText and compares with @expectedText
     * Waits till element @locatorName to be displayed before performing the action.
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     *
     * @param locatorName  - Element name defined in the page Object repository file
     * @param expectedText - Expected Text
     */
    @Step("Verify text of element '{0}'")
    public void verifyText(String locatorName, String expectedText) {
        if (!CommonUtil.isValidString(expectedText))
            Assert.fail("Expected Text should not be null or empty");
        String actualText = getText(locatorName).trim();
        Assert.assertTrue(actualText.equalsIgnoreCase(expectedText.trim()), "text not matched. ActualText: " + actualText + "'. Expected: '" + expectedText + "'\n");
    }

    /**
     * This method verifies Text partially. it read's value from @locatorName by calling getText and partially compares with @expectedText
     * Waits till element @locatorName to be displayed before performing the action.
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     *
     * @param locatorName  - Element name defined in the page Object repository file
     * @param expectedText - Expected Text
     */
    @Step("Verify partial text of element '{0}'")
    public void verifyPartialText(String locatorName, String expectedText) {
        if (!CommonUtil.isValidString(expectedText))
            Assert.fail("Expected Text should not be null or empty");
        String actualText = getText(locatorName).trim();
        Assert.assertTrue(actualText.contains(expectedText.trim()), "partial text not matched. ActualText: '" + actualText + "'. Expected(Partial): '" + expectedText + "'\n");
    }

    /**
     * This method takes the screen Shot of the current Screen and save in Allure report
     *
     * @param name - Screen shot attachment name
     */
    public void attachScreenShotToTheReport(String name) {
        TakesScreenshot tk = (TakesScreenshot) driver;
        byte[] b = tk.getScreenshotAs(OutputType.BYTES);
        Allure.addAttachment(name, "image/png", new ByteArrayInputStream(b), "png");
    }

    /**
     * This method waits till page gets loaded properly.
     * It uses WebDriverWait and with condition of WebPage Javascript property "document.readyState"="complete"
     */
    public void waitForThePageLoad() {
        new WebDriverWait(driver, defaltWaitTimeForPageLoad).until(
                webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
    }

    /**
     * This method waits according to provided milliSec.
     * Internally it calls Thread.sleep() Method
     */
    public void sleep(long milliSec) {
        try {
            Thread.sleep(milliSec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method return the selected Option from @locatorName. To find element it calls waitForElementToDisplay With the timeout defaultWaitTimeForElement
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     *
     * @param locatorName - Element name defined in the page Object repository file
     * @return String
     */
    @Step("Verify partial text of element '{0}'")
    public String getSelectedOptionFromDropDown(String locatorName) {
        WebElement element = waitForElementToDisplay(locatorName);
        Select select = new Select(element);
        WebElement firstSelectedOption = select.getFirstSelectedOption();
        return firstSelectedOption.getText();
    }


    /**
     * This method checks whether check box is  isCheckBoxSelected in to @locatorName. To find element it calls waitForElementToDisplay With the timeout defaultWaitTimeForElement
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     *
     * @param locatorName - Element name defined in the page Object repository file
     * @return boolean    - true : Selected | false : Not Selected
     */
    @Step("Checking is Check Box Selected in element '{0}'")
    public boolean isCheckBoxSelected(String locatorName) {
        WebElement element = waitForElementToDisplay(locatorName);
        return element.isSelected();
    }


    /**
     * This method performs the <b>MouseOver</b> event in @locatorName.
     * To find element it calls waitForElementToDisplay With the timeout defaultWaitTimeForElement
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     *
     * @param locatorName - Element name defined in the page Object repository file
     */
    @Step("Performing MouseOver event in element '{0}'")
    public void mouseHover(String locatorName) {
        WebElement element = waitForElementToDisplay(locatorName);
        Actions ac = new Actions(driver);
        ac.moveToElement(element).perform();
    }

    /**
     * This method Scrolls Up By 255 pixel. To find element it calls executeJavaScript
     * Send the script to execute code
     */
    @Step("Scrolling Up by 255px")
    public void scrollUp() {
        executeJavaScript("window.scrollBy(0,-250)");
    }

    /**
     * This method Scrolls Down By 255 pixel. To find element it calls executeJavaScript
     * Send the script to execute code
     */
    @Step("Scrolling Up by 255px")
    public void scrollDown() {
        executeJavaScript("window.scrollBy(0,255)");
    }

    /**
     * This method Scrolls Down By 500 pixel. To find element it calls executeJavaScript
     * Send the script to execute code
     */
    @Step("Scrolling Up by 500px")
    public void scrollPage() {
        executeJavaScript("window.scrollBy(0,500)");
    }

    /**
     * This method scrolls into view with @locatorName
     * To find element it calls waitForElementToDisplay With the timeout defaultWaitTimeForElement
     * Send the script to execute code
     */
    @Step("Scrolling into element : '{0}'")
    public void scrollIntoView(String locatorName) {
        WebElement element = waitForElementToPresent(locatorName);
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView();", element);
    }


    /**
     * This method executes Java script code By calling JavaScriptExecutor.executeScript
     */
    @Step("Executing script : '{0}'")
    public void executeJavaScript(String script) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript(script);
    }

    /**
     * This method returns all opened tabs id with @Set<String>. it calls driver.getWindowHandles();
     *
     * @return @Set<String>
     */
    @Step("Getting all window ID")
    public Set<String> getWindowHandels() {
        return driver.getWindowHandles();
    }

    /**
     * This method returns current tab window id. It calls get driver.getWindowHandle();
     *
     * @return String
     */
    @Step("Getting current window ID ")
    public String getWindowHandel() {
        return driver.getWindowHandle();
    }

    /**
     * This method closes the current. This method calls driver.close();
     */
    @Step("Closing current tab")
    public void closeCurrentTab() {
        driver.close();
    }

    /**
     * This method return selenium driver;
     */
    @Step("Get the selenium driver")
    public WebDriver getDriver() {
        return driver;
    }

    /**
     * This method returns all elements id with @List<String>. it calls driver.findElements(By.xpath(locatorName));
     *
     * @param locatorName - Element name defined in the page Object repository file
     * @return @List<String>
     */
    @Step("Getting all elements ID : '{0}'")
    public List<WebElement> getElements(String locatorName) {
        By locBy = pageObjectRepoHelper.getObject(locatorName);
        return driver.findElements(locBy);
    }

    /**
     * This method returns all elements id with @List<String>. it calls driver.findElements(By.xpath(locatorName));
     * <p>
     * The final element will be created based upon the values @locatorName and @replaceKeyVal
     * Let's say the Locator type is xpath like //*[@id='$$countryName$$'] and the key replaced value is Map.Of("$$countryName$$","USA")
     * then the final xpath will be created like this //*[@id='USA'] and the dynamic element will be created in run time.
     *
     * @param locatorName   - Element name defined in the page Object repository file
     * @param replaceKeyVal - A Dictionary/Map that contains the patterns (Keys) to be replaced by respective values
     * @return @List<String>
     */
    @Step("Getting all elements ID : '{0}'")
    public List<WebElement> getElements(String locatorName, Map<String, String> replaceKeyVal) {
        By locBy = pageObjectRepoHelper.getObject(locatorName, replaceKeyVal);
        return driver.findElements(locBy);
    }

    /**
     * This method will return the status of the element whether it is enable or not.
     * It will wait for the element to be enable up to @waitTime in seconds before returning the status.
     *
     * @param locatorName - Element name defined in the page Object repository file
     * @param waitTime    - Time to wait for the element in seconds before returning the status.
     * @return true/false - ture:Enable | false:Disable
     */
    @Step("is Element '{0}' Enable")
    public boolean isElementEnable(String locatorName, int waitTime) {
        By locBy = pageObjectRepoHelper.getObject(locatorName);
        try {
            WebElement ele = new WebDriverWait(driver, waitTime).until(ExpectedConditions.visibilityOfElementLocated(locBy));
            return ele.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * This method check the dynamic element is enable or not
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     * <p>
     * The final element will be created based upon the values @locatorName and @replaceKeyVal
     * Let's say the Locator type is xpath like //*[@id='$$columnName$$'] and the key replaced value is Map.Of("$$columnName$$","Location")
     * then the final xpath will be created like this //*[@id='Location'] and the dynamic element will be created in run time.
     *
     * @param locatorName   - Element name defined in the page Object repository file
     * @param customName    - Name in the report
     * @param replaceKeyVal - A Dictionary/Map that contains the patterns (Keys) to be replaced by respective values
     * @param waitTime    - Time to wait for the element in seconds before returning the status.
     * @return true/false - ture:Enable | false:Disable
     */
    @Step("Is Element '{1}' enable/disable")
    public boolean isElementEnable(String locatorName, String customName, Map<String, String> replaceKeyVal, int waitTime) {
        By locBy = pageObjectRepoHelper.getObject(locatorName, replaceKeyVal);
        try {
            WebElement ele = new WebDriverWait(driver, waitTime).until(ExpectedConditions.visibilityOfElementLocated(locBy));
            return ele.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * This method check the dynamic element is selected or not
     * DefaultWaitTimeForElement should be set in the config file located at src/test/resources/config.properties
     * <p>
     * The final element will be created based upon the values @locatorName and @replaceKeyVal
     * Let's say the Locator type is xpath like //*[@id='$$columnName$$'] and the key replaced value is Map.Of("$$columnName$$","Location")
     * then the final xpath will be created like this //*[@id='Location'] and the dynamic element will be created in run time.
     *
     * @param locatorName   - Element name defined in the page Object repository file
     * @param customName    - Name in the report
     * @param replaceKeyVal - A Dictionary/Map that contains the patterns (Keys) to be replaced by respective values
     * @param waitTime    - Time to wait for the element in seconds before returning the status.
     * @return true/false - ture:Selected | false:NotSelected
     */
    @Step("Is Element '{1}' Selected/NotSelected")
    public boolean isElementSelected(String locatorName, String customName, Map<String, String> replaceKeyVal, int waitTime) {
        By locBy = pageObjectRepoHelper.getObject(locatorName, replaceKeyVal);
        try {
            WebElement ele = new WebDriverWait(driver, waitTime).until(ExpectedConditions.visibilityOfElementLocated(locBy));
            return ele.isSelected();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Wait for Page to load
     */
    public void waitForPageToLoad() {
        boolean jsReady = false;
        int counter = 0;
        try {
            Thread.sleep(500);
            JavascriptExecutor jsExec = (JavascriptExecutor) getDriver();
            while (counter < 30) {
                jsReady = jsExec.executeScript(
                                "return ((document.readyState == 'complete' || document.readyState == 'interactive') && jQuery.active == 0)")
                        .equals(true);
                if (counter >= 15 || jsReady) {
                    break;
                }
                Thread.sleep(500);
                counter++;
            }
        } catch (Exception ex) {
            // do nothing
        }
    }

    /**
     * Switch Tab By Index
     *
     * @author Kundan Kumar Sah
     * @return
     */
    public WebDriver switchTabByIndex(int tabIndex) {
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        return driver.switchTo().window(tabs.get(tabIndex));
    }

    @FindAll({ @FindBy(xpath = "//div[@class='jqx-grid-load']"), @FindBy(xpath = "//div[@id='layoutSpinner']"),
            @FindBy(xpath = "//div[@class='dp-spinner']") })
    public List<WebElement> circleLoader;

    /** overlay Stripes loader */
    @FindAll({ @FindBy(xpath = "//div[@class='blockUI blockOverlay'] | (//div[@id='skn-blocker']/img)[1]"),
            @FindBy(xpath = "//div[@id='blocker']"), @FindBy(xpath = "//div[@class='skn-blocker']//img") })
    public List<WebElement> stripesLoader;

    /** spinner loader */
    @FindBy(xpath = "//i[@class='fa-li fa fa-spinner fa-spin']")
    public List<WebElement> spinnerLoader;

    public void waitForLoader(WebDriver driver, List<WebElement> Loader, int timeInSecs) {
        if (Loader != null) {
            try {
                driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
                /*
                 * if(timeInSecs>500) { timeInSecs=500; }
                 */
                WebDriverWait wait = new WebDriverWait(driver, timeInSecs);
                wait.until(ExpectedConditions.invisibilityOfAllElements(Loader));
            } catch (Exception ex) {

            } finally {
                driver.manage().timeouts().implicitlyWait(implicitWaitSec, TimeUnit.SECONDS);
            }
        }
    }

    /**
     * Wait for Loaders
     *
     * @return
     */
    public void waitForLoader() {
        this.waitForLoader(maxWaitTimeInSec);
    }

    /**
     * Wait for Loaders
     *
     * @return
     */
    public void waitForLoader(int timeInSecs) {
        sleep(500);
        waitForLoader(driver, circleLoader, timeInSecs);
        waitForLoader(driver, stripesLoader, timeInSecs);
        waitForLoader(driver, spinnerLoader, timeInSecs);
    }


    public String randomAlphabetic (int count){
        String allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder randomText = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            randomText.append(allowedChars.charAt(random.nextInt(allowedChars.length())));
        }
        return randomText.toString();
    }
    public String randomAlphanumeric (int count){
        String allowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder randomText = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            randomText.append(allowedChars.charAt(random.nextInt(allowedChars.length())));
        }
        return randomText.toString();
    }
    public String randomNumeric (int count){
        String allowedChars = "0123456789";
        StringBuilder randomText = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < count; i++) {
            randomText.append(allowedChars.charAt(random.nextInt(allowedChars.length())));
        }
        return randomText.toString();
    }

    public void pressEnterKey(String locatorName) {
        WebElement element = waitForElementToDisplay(locatorName);
        element.sendKeys(Keys.ENTER);

    }
}



/*
 * dismissAlert--
 * getTextFromAlert --
 * setTextToAlert --
 * getSelectedOptionFromDropDown --
 * isCheckBoxSelected --
 * mouseHover --
 * scrollIntoViewLeft --
 * scrollIntoViewRight --
 * scrollUp --
 * scrollDown--
 * getWindowHandel --
 * getWindowHandels --
 * closeCurrentTab --
 * executeJavaScript --
 */
