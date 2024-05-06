package com.impactqa.utilities;

import com.google.common.io.Files;

import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @version 1.0
 * @description This class is Util class that read data from verify PDF files filter and verification of data
 * @since 2021-03-20
 */

public class SystemUtils {
    static String value = null;
    static public final String INITIAL_TIMESTAMP = getDateTime();
    static public final String FILE_SEPARATOR = System.getProperty("file.separator");
    static public final String PROJECT_DIRECTORY = System.getProperty("user.dir");
    static public final String USER_HOME_DIRECTORY = System.getProperty("user.home");




    /**
     *  This method returns date time in string format
     * @return Datetime in format yyyy-MM-dd_HH-mm-ss
     */
    public static String getDateTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        return dateTimeFormatter.format(localDateTime).replace(" ", "_");
    }

    /**
     * Executes terminal related commands and batch files
     * @return (true / false)
     */
    public static boolean executeCommand(String command) {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (System.getProperty("os.name").contains("Windows")) {
            if (command.contains(".bat"))
                processBuilder.command(command);
            else
                processBuilder.command("cmd.exe", "/c", command);
        } else if (command.contains(".sh"))
            processBuilder.command(command);
        else
            processBuilder.command("bash", "-c", command);

        try {
            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                output.append(line);
            }
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                System.out.println("Success!");
                value = output.toString();
                return true;
            } else {
                System.out.println("abnormal...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return return's IP Address of System
     */
    public static String getIP() {
        String st = null;
        try {
            InetAddress myIP = InetAddress.getLocalHost();
            st = myIP.getHostAddress();
        } catch (Exception e) {
            throw new RuntimeException("Error occurred while getting ip address : ", e);
        }
        return st;
    }

    public static void sleep(long milliSeconds) {
    try {
        Thread.sleep(milliSeconds);
    }catch (Exception e){

    }

    }

}

