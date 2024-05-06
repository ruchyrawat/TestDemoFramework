package com.impactqa.utilities;

import com.impactqa.base.BaseTestMobile;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * @version 1.0
 * @description This class sends Email and for secure emailing from framework it uses SSL security layer
 * @since 2021-03-18
 * */

public class EmailSSLUtils {
    private Session session;
    private MimeMessage message;
    private Multipart multipart;
    private Properties properties;

    static String pass;
    static String fail;
    static String skip;

    public EmailSSLUtils() {
        multipart = new MimeMultipart();
    }

    /**
     * It will create a session with SMTP server with authentication
     * Email server connection parameters and credential should be maintained in the config file
     *
     * Parameters related to email
     * SendEmail
     * SMTPHost
     * SMTPSocketFactoryPort
     * SMTPSocketFactoryClass
     * SMTPAuth
     * SMTPPort
     * FromEmailAddress
     * EmailPassword
     */
    public boolean startSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", FrameworkConfig.getStringConfigProperty("SMTPHost"));
        props.put("mail.smtp.socketFactory.port", FrameworkConfig.getStringConfigProperty("SMTPSocketFactoryPort"));
        props.put("mail.smtp.socketFactory.class", FrameworkConfig.getStringConfigProperty("SMTPSocketFactoryClass"));
        props.put("mail.smtp.auth", FrameworkConfig.getStringConfigProperty("SMTPAuth"));
        props.put("mail.smtp.port", FrameworkConfig.getStringConfigProperty("SMTPPort"));

        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                try {
                    String password = CryptoUtils.decryptTheValue(FrameworkConfig.getStringConfigProperty("EmailPassword"));
                    return new PasswordAuthentication(FrameworkConfig.getStringConfigProperty("FromEmailAddress"), password);
                } catch (Exception e) {
                    throw new RuntimeException("Error Occurred while Authenticating Email credentials ", e);
                }
            }
        };

        session = Session.getDefaultInstance(props, auth);
        setHeaders();
        return true;
    }

    /**
     * This method sets headers to session and defines information about the email content type
     * */

    public void setHeaders() {
        try {
            message = new MimeMessage(session);
            message.addHeader("Content-type", FrameworkConfig.getStringConfigProperty("ContentType"));
            message.addHeader("format", FrameworkConfig.getStringConfigProperty("EmailFormat"));
            message.addHeader("Content-Transfer-Encoding", FrameworkConfig.getStringConfigProperty("ContentTransferEncoding"));
            message.setFrom(new InternetAddress(FrameworkConfig.getStringConfigProperty("InternetAddress"), FrameworkConfig.getStringConfigProperty("EmailHeaders")));
            message.setReplyTo(InternetAddress.parse(FrameworkConfig.getStringConfigProperty("InternetAddress"), true));
            message.setSentDate(new Date());
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * @param body (Email Content has to be sent)
     * @description This method adds Email content to Body Part of email
     * */
    public void addBodyPart(String body, String contentType) {

        //        -------------------------Change
        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(System.getProperty("user.dir") + "/TestExecutionTime.json"));
            BaseTestMobile.startTime = (Long) jsonObject.get("startTime");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        System.out.println("wfbiechigrbigvhbwekjgvhjkrgk 2"+BaseTestMobile.startTime);
        BaseTestMobile. endTime= System.currentTimeMillis();
        System.out.println("wfbiechigrbigvhbwekjgvhjkrgk 2"+BaseTestMobile.endTime);
        long differenceInMilliSeconds
                = Math.abs(BaseTestMobile.endTime - BaseTestMobile.startTime);
        long differenceInHours
                = (differenceInMilliSeconds / (60 * 60 * 1000))
                % 24;
        long differenceInMinutes
                = (differenceInMilliSeconds / (60 * 1000)) % 60;
        long differenceInSeconds
                = (differenceInMilliSeconds / 1000) % 60;
        JSONParser jsonParser2 = new JSONParser();
        try {
            //Parsing the contents of the JSON file
            JSONObject jsonObject = (JSONObject) jsonParser2.parse(new FileReader(System.getProperty("user.dir") + "/TestResult.json"));
            pass = (String) jsonObject.get("pass");
            fail = (String) jsonObject.get("fail");
            skip = (String) jsonObject.get("skip");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }  catch (org.json.simple.parser.ParseException e) {
            e.printStackTrace();
        }

        String str = "Hi, Below are the some stats of Executed test suite " + "\n" + "\n";


        str = str.concat("Environment: " + FrameworkConfig.getStringConfigProperty("ApplicationEnvironment") + "\n");

        str = str.concat("Execution Time: "+ differenceInHours + " hours "
                + differenceInMinutes + " minutes "
                + differenceInSeconds + " Seconds " +"\n");


        int totalCount = Integer.parseInt(pass)+Integer.parseInt(fail)+Integer.parseInt(skip);

        str = str.concat("Total Test Case count: "+totalCount+"\n");

        str = str.concat("Passed Test Case Count: " + pass + "\n" +
                "Failed Test Case Count: " + fail + "\n" +
                "Skipped Test Case Count: " + skip + "\n");


        float testCaseExecutedPer = (float) (Integer.parseInt(pass) + Integer.parseInt(fail)) / totalCount * 100;
        float testCasePassPer = (float) Integer.parseInt(pass) / totalCount * 100;
        float testCaseFailPer = (float) Integer.parseInt(fail) / totalCount * 100;
        float testCaseSkipPer = (float) Integer.parseInt(skip) / totalCount * 100;

        // Prepare of HTML message
        String htmlMessage = prepareHTMLMessage(Long.toString(differenceInMilliSeconds), totalCount, Integer.parseInt(pass), Integer.parseInt(fail), Integer.parseInt(skip));


        str = str.concat("Test Case Execution %: " + testCaseExecutedPer + "%"+"\n" +
                "Passed Test Case%: " + testCasePassPer + "%"+"\n" +
                "Failed Test Case%: " + testCaseFailPer + "%"+"\n"+
                "Skipped Test Case%: " + testCaseSkipPer + "%"+"\n");
        try {
            BodyPart messageBodyPart = new MimeBodyPart();
//            messageBodyPart.setText("Mandeep");
//            messageBodyPart.setText(str);
            messageBodyPart.setContent(htmlMessage, contentType);
            multipart.addBodyPart(messageBodyPart);
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * @param filename (file that need to Attached in email)
     * @description This method adds Email email
     *
     */
    public void addAttachments(String filename) {
        try {
            System.out.println("Attaching file "+filename+" to email body");
            BodyPart messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(source.getName());
            multipart.addBodyPart(messageBodyPart);
            System.out.println("Attached file to email body");
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    /**
     * @param toEmails TO-Email Recipients
     * @param ccEmails CC-Email Recipients
     * @param subject Email subject
     * @description Triggers mail request
     * */
    public void sendEmail(String toEmails, String ccEmails, String subject) {
        System.out.println("Sending email....");
        try {
            if(CommonUtil.isValidString(toEmails))
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmails, false));
            else
                throw new RuntimeException("To Address cannot be empty in the config file");

            if(CommonUtil.isValidString(ccEmails))
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmails, false));

            message.setSubject(subject, "UTF-8");
            message.setContent(multipart);
            Transport.send(message);
            System.out.println("Email Sent Successfully with attachment!!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    /**
     * Prepare HTML Message for email
     *
     * @return
     */
    private String prepareHTMLMessage(String lapsedTime, int totalTests, int testPassed, int testFailed,
                                      int testSkipped) {
        StringBuffer strbResult = new StringBuffer();
        strbResult.append("<!DOCTYPE html>");
        strbResult.append("<html><head><title>Automation Execution Results</title>");
        strbResult.append("<style>");
        strbResult.append("table, th, td { border: 1px solid;}");
        strbResult.append(
                "table#testResults { table-layout: fixed ; width: 90% ; margin-left: auto; margin-right: auto; word-wrap: normal;}");
        strbResult.append(
                "table#testStats { table-layout: fixed ; width: 100% ; margin-left: auto; margin-right: auto; word-wrap: normal;}");
        strbResult.append("</style>");
        strbResult.append("</head>");
        strbResult.append("<body><br>");
        // Write Test Results Execution Information
        strbResult.append(
                "<table id=\"footer\" align=\"center\" style=\"border: 0;width: 100%;vertical-align: bottom;\"><tr><td style=\"border: 0;text-align: center;\"><i> **** This is an generated email from the QA Test Automation. Please do not respond. **** </i></td></tr></table>");
        // Write Test Results Stats
        strbResult.append("<br><br><table id=\"testResults\" align=\"center\">");
        strbResult.append("<tr>");
        strbResult.append("<td colspan=\"7\">");
        strbResult.append("<p style=\"text-align: left;\">");
        // Change the number of test cases
        strbResult.append("<b>Total Test Cases Executed</b>: " + "44" + "<br>");
        strbResult.append("<b>Total Test Cases Steps Executed</b>: " + totalTests + "<br>");
        if (System.getProperty("suiteXmlFile") != null)
            strbResult.append("<b>Test Pack</b>: " + System.getProperty("suiteXmlFile") + "<br>");
        strbResult.append("</p>");
        strbResult.append("</td>");
        strbResult.append("</tr>");
        strbResult.append("<tr>");
        strbResult.append("<td align=\"center\" rowspan=\"4\">");
        strbResult.append(
                "<b><img style=\"width:100%;max-width:200px\" src=\"https://quickchart.io/chart?c={ type: 'doughnut', data: { datasets: [ { data: ["
                        + testPassed + ", " + testFailed + ", " + testSkipped
                        + "], backgroundColor: [ 'rgb(0, 128, 0)', 'rgb(255, 0, 0)', 'rgb(255, 127, 80)' ], label: 'results', }, ], labels: ['Pass', 'Fail', 'Skip'], }, options: { title: { display: true, text: 'Test Results', }, }, }\" alt=\"Test Results\"></b>");
        strbResult.append("</td>");
        strbResult.append("<td align=\"center\" colspan=\"6\"><p><b>Total Test Cases Steps Executed<br><br>" + totalTests
                + "</b></p></td>");
        strbResult.append("</tr>");
        strbResult.append("<tr><td colspan=\"2\">Passed Test Case Steps Count</td><td colspan=\"1\" align=\"center\">"
                + testPassed + "</td><td colspan=\"2\">Passed Test Case Steps %</td><td colspan=\"1\" align=\"center\">"
                + (new DecimalFormat("#.##")).format(((float) testPassed * 100) / (float) totalTests) + "%</td></tr>");
        strbResult.append("<tr><td colspan=\"2\">Failed Test Case Steps Count</td><td colspan=\"1\" align=\"center\">"
                + testFailed + "</td><td colspan=\"2\">Failed Test Case Steps %</td><td colspan=\"1\" align=\"center\">"
                + (new DecimalFormat("#.##")).format(((float) testFailed * 100) / (float) totalTests) + "%</td></tr>");
        strbResult.append("<tr><td colspan=\"2\">Skipped Test Case Steps Count</td><td colspan=\"1\" align=\"center\">"
                + testSkipped + "</td><td colspan=\"2\">Skipped Test Case Steps %</td><td colspan=\"1\" align=\"center\">"
                + (new DecimalFormat("#.##")).format(((float) testSkipped * 100) / (float) totalTests) + "%</td></tr>");
        strbResult.append("</table>");
        strbResult.append("</body></html>");
        return strbResult.toString();
    }
}

