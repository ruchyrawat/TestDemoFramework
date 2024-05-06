package com.impactqa.utilities;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.impactqa.base.BaseTestMobile;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import org.apache.commons.io.FileUtils;
import org.aspectj.util.FileUtil;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Test;

import static com.impactqa.utilities.SystemUtils.FILE_SEPARATOR;
import static com.impactqa.utilities.SystemUtils.PROJECT_DIRECTORY;


/**
 * @version 1.0
 * @description This class Provides operation which are need to perform after execution of test Cases.
 * i.e. Sending mail, Maintaining report history
 */

public class PostExecutionClass {

    private static final List<String> additionalAttachments = new LinkedList<>();
    /**
     * Allure report will be copied under {Workspace}/ArchivedReports/Report_{yyyy-MM-dd_HH-mm-ss}
     */
    public static void createReportVersionWithDateTime() {

        List<String> reportFileList = new LinkedList<>();

        reportFileList.add(PROJECT_DIRECTORY + FILE_SEPARATOR + "allure-results");
        reportFileList.add(PROJECT_DIRECTORY + FILE_SEPARATOR + "GenerateAllureReport.bat");
        reportFileList.add(PROJECT_DIRECTORY + FILE_SEPARATOR + "GenerateAllureReport.sh");

        String archiveReportFolderDirectoryPath = PROJECT_DIRECTORY + FILE_SEPARATOR +"ArchivedReports"+ FILE_SEPARATOR +"Report_" + SystemUtils.getDateTime();

        File archiveReportFolderDirectory = new File(archiveReportFolderDirectoryPath);
        try {
            archiveReportFolderDirectory.mkdirs();

            for(String filepath : reportFileList){
                File fromFile = new File(filepath);
                if(fromFile.exists()){
                    if(fromFile.isDirectory()) {
                        File toDir = new File(archiveReportFolderDirectory.getAbsoluteFile() + FILE_SEPARATOR + fromFile.getName());
                        toDir.mkdirs();
                        FileUtil.copyDir(fromFile, toDir);
                    }
                    else if(fromFile.isFile())
                        FileUtil.copyFile(fromFile, archiveReportFolderDirectory);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while copy files into folder : " + archiveReportFolderDirectoryPath);
        }

        System.out.println("Report has been Archived to the directory : " + archiveReportFolderDirectoryPath);
    }

    /**
     * This method will send summary report to recipient
     * recipient mail id should be defined in the config.properties
     * EmailAttachmentPath
     */
    public static void sendEmails() {
        EmailSSLUtils emailUtil = new EmailSSLUtils();
        if (emailUtil.startSession())
        {
            emailUtil.addBodyPart(FrameworkConfig.getStringConfigProperty("EmailBody"), FrameworkConfig.getStringConfigProperty("ContentType"));

            addFilesMentionedInConfigFileToEmail(emailUtil);
            addAdditionalFilesToEmail(emailUtil);

            String toEmails = FrameworkConfig.getStringConfigProperty("ToEmailAddresses");
            String ccEmails = FrameworkConfig.getStringConfigProperty("ToEmailAddresses_CC");
            emailUtil.sendEmail(toEmails, ccEmails, FrameworkConfig.getStringConfigProperty("EmailSubject"));
        } else {
            throw new RuntimeException("Wrong Email credential");
        }
    }


    private static void attachFileToEmail(String filePath, EmailSSLUtils emailUtil){

        try {
            File fileO = new File(filePath);
            if(fileO.exists()){
                if(fileO.isFile()){
                    emailUtil.addAttachments(filePath);
                }
                else if(fileO.isDirectory()){
                    System.out.println("Zipping the directory "+filePath+" to "+"./temp/"+fileO.getName()+".zip");
                    new File("./temp").mkdirs();
                    String tempFileName = "./temp/" + fileO.getName() + ".zip";

                    if(new File(tempFileName).exists()){
                        new File(tempFileName).delete();
                    }

                    ZipParameters zipParameters = new ZipParameters();
                    zipParameters.setCompressionLevel(CompressionLevel.FASTEST);
                    new ZipFile(tempFileName).addFolder(fileO, zipParameters);
//                    new ZipFile(tempFileName).addFolder(fileO);
                    System.out.println("Zipping completed");
//                    emailUtil.addAttachments("./temp/"+fileO.getName()+".zip");
                }
            }
        } catch (IOException e) {
            System.out.println("Skipping file : "+filePath+" Error: "+e.getMessage());
        }

    }

    private static void addAdditionalFilesToEmail(EmailSSLUtils emailUtil){
        for(String additionalAttachment : additionalAttachments){
            attachFileToEmail(additionalAttachment, emailUtil);
        }
    }

    private static void addFilesMentionedInConfigFileToEmail(EmailSSLUtils emailUtil){
        String filesPaths = FrameworkConfig.getStringConfigProperty("EmailAttachmentPaths");
        if(CommonUtil.isValidString(filesPaths))
            for(String additionalAttachment : filesPaths.split("\\|")){
                attachFileToEmail(additionalAttachment, emailUtil);
            }
    }

    public static void addAdditionalAttachmentInList(String filePath){
        additionalAttachments.add(filePath);
    }

    public static void main(String[] args) {
//        if (FrameworkConfig.getStringConfigProperty("SendEmail").equals("true"))
//            sendEmails();
        if (FrameworkConfig.getStringConfigProperty("CreateAllureReportVersionWithDateTime").equals("true"))
            createReportVersionWithDateTime();
    }

}
