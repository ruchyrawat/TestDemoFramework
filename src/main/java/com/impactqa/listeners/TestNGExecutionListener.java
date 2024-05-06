package com.impactqa.listeners;

import com.impactqa.utilities.FrameworkConfig;
import com.impactqa.utilities.PostExecutionClass;
import org.apache.commons.io.FileUtils;
import org.testng.IExecutionListener;

import java.io.File;

public class TestNGExecutionListener implements IExecutionListener {
    @Override
    public void onExecutionStart() {
        if (FrameworkConfig.getStringConfigProperty("DeleteAllureResultsInStartOfTestExecution").equals("true")) {
            try {
                File file = new File(System.getProperty("user.dir") + "/allure-results");
                if (file.exists())
                    FileUtils.deleteDirectory(file);
                System.out.println("Allure result deleted..");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onExecutionFinish() {
//        PostExecutionClass.createReportVersionWithDateTime();
//        PostExecutionClass.sendEmails();
    }
}
