package com.impactqa.utilities;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Properties;

public class AllureEnvironmentPropertyUtil {

    static final String allureEnvironmentPropertyFilePath =  System.getProperty("user.dir") + "/allure-results/environment.properties";
    static final Properties properties = new Properties();

    private static final  AllureEnvironmentPropertyUtil allureEnvironmentPropertyUtil = new AllureEnvironmentPropertyUtil();
    private AllureEnvironmentPropertyUtil() {

        File file = new File(allureEnvironmentPropertyFilePath);
        if(file.exists())
            file.delete();

        try {
            FileUtils.write(file, "", Charset.defaultCharset());
            FileInputStream instream = new FileInputStream(allureEnvironmentPropertyFilePath);
            properties.load(instream);
            instream.close();
        } catch (IOException e) {
            throw new RuntimeException("Error occurred file reading/creating allureEnvironmentPropertyFilePath. " +
                    "Path: "+allureEnvironmentPropertyFilePath, e);
        }

    }

    public static void addProperty(String key, String value)  {
        if(!properties.containsKey(key)) {
            properties.setProperty(key, value);
            try {
                FileOutputStream outstream = new FileOutputStream(allureEnvironmentPropertyFilePath);
                properties.store(outstream, null);
                outstream.close();
            } catch (IOException e) {
                throw new RuntimeException("Error occured while setting/storing the new property", e);
            }
        }
    }
}
