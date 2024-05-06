package com.impactqa.base;

import com.google.common.collect.ImmutableMap;
import com.impactqa.utilities.*;
import io.appium.java_client.AppiumDriver;
import io.qameta.allure.Step;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.Set;


/**
 * @description All Test classes should be extended by this class. It will manage browser sessions before and after each test case execution
 * @since 22020-11-10
 */
public class BaseTestMobile {

    protected AppiumDriver driver;
    public static PageObjectRepoHelper.PLATFORM platform;
    protected Map<String, String> testDataMap;
    public static long startTime; // change
    public static long endTime; // change

    @BeforeTest(description = "Open new mobile session")
    @Parameters({"dataID"})
    public void openMobileSession(String dataID) throws MalformedURLException {
        //--------------- change

        startTime = System.currentTimeMillis();
        System.out.println("wfbiechigrbigvhbwekjgvhjkrgk 1"+startTime);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("startTime", startTime);
        try {
            FileWriter file = null;
            try {
                file = new FileWriter(System.getProperty("user.dir") + "/TestExecutionTime.json");
            } catch (IOException e) {
                e.printStackTrace();
            }
            file.write(jsonObject.toJSONString());
            file.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //--------------- change
        ExcelUtil excel = new ExcelUtil();
        excel.setWorkbook(FrameworkConfig.getStringConfigProperty("TestDataFileLocation"),
                FrameworkConfig.getStringConfigProperty("TestDataSheetName_mobile"));

        testDataMap = excel.getRowDataMatchingDataId(dataID);

        if (testDataMap.size() < 1)
            Assert.fail("dataID '" + dataID + "' is valid the excel sheet. please check the test data sheet");

        excel.setWorkbook(FrameworkConfig.getStringConfigProperty("TestDataFileLocation"),
                "MobileSessionDetails");

        Map<String, String> sessionDetails = excel.getRowDataMatchingDataId(testDataMap.get("MobileSessionID1"));

        if ("ios".equals(sessionDetails.get("platformName").toLowerCase())){
            platform = PageObjectRepoHelper.PLATFORM.IOS;
            AllureEnvironmentPropertyUtil.addProperty("PlatFormName", "iOS");
        }
        else{
            platform = PageObjectRepoHelper.PLATFORM.ANDROID;
            AllureEnvironmentPropertyUtil.addProperty("PlatFormName", "Android");
        }
        AllureEnvironmentPropertyUtil.addProperty("Environment", FrameworkConfig.getStringConfigProperty("ApplicationEnvironment"));
        driver = DriverProvider.createNewMobileSession(platform, sessionDetails);
    }

    public AppiumDriver getDriver() {
        return driver;
    }

    public void switchContext(String context)
    {
        System.out.println("Before Switching : "+driver.getContext());
        Set<String> con = driver.getContextHandles();
        System.out.println("Set : " + con);
        for(String c : con)
        {
            System.out.println("Available Context : "+c);
            if(c.contains(context))
            {
                driver.context(c);
                break;
            }
        }
        System.out.println("After Switching : "+driver.getContext());
    }

    @Step("Wait for {0}")
    public void waitForTrackToComplete(int minutes){
        int second = minutes*2;
        for(int i =0; i<second; i++) { // 30x30sec=15min
            try{
                Thread.sleep(30000);
            }
            catch(Exception e){
            } //every 30 sec
            getDriver().getPageSource();
        }
    }

    @AfterTest(description = "close the mobile session")
    public void teardownDriverInstance() {
        if (driver != null)
            driver.quit();
    }
}
