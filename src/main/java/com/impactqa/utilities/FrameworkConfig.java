package com.impactqa.utilities;

import com.impactqa.exceptions.CustomRunTimeException;

import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.swing.*;

import static com.impactqa.utilities.SystemUtils.FILE_SEPARATOR;
import static com.impactqa.utilities.SystemUtils.PROJECT_DIRECTORY;

/*
 *
 *
 * @version 1.0
 * @description This class is Util class to get framework config values like timeouts, URL, paths, etc
 * Framework Config values should be provided in the file src/test/resources/config.properties
 * @since 22020-09-12
 * */

public class FrameworkConfig extends CommonUtil {

    private static final String envProperties = PROJECT_DIRECTORY + FILE_SEPARATOR + "src"+ FILE_SEPARATOR +"test"+ FILE_SEPARATOR +"resources"+ FILE_SEPARATOR +"config.properties";
    //private static final String envProperties = PROJECT_DIRECTORY + FILE_SEPARATOR + "config.properties";

    private static final Properties props = loadFrameworkConfigProperties();

    /**
     * loads the config.properties to get all the values from it which can be used through out the whole execution
     *
     * @return Properties instance
     */
    private static Properties loadFrameworkConfigProperties() {
        Properties props = new Properties();
        try {
            InputStream steam = new FileInputStream(envProperties);
            props.load(steam);
        } catch (IOException e) {
            throw new CustomRunTimeException("Error occurred while reading Framework Config file: " + envProperties, e);
        }

        return props;
    }

    /**
     * return String value of the given @key config property
     *
     * @param key - The name of the key in the config file
     * @return String value of the property
     */
    static public String getStringConfigProperty(String key) {
        if(!isValidString(key))
            throw new CustomRunTimeException("Config Property should not be null or empty");
        String value = props.getProperty(key);
        if(!isValidString(value))
            throw new CustomRunTimeException("Property with key '" + key + "' is not found or it's empty");
        return value;
    }


    /**
     * return String value of the given @key config property
     *
     * @param key - The name of the key in the config file
     * @return numeric value of the property
     */
    static public int getNumberConfigProperty(String key) {
        String stringVal = getStringConfigProperty(key);
        if (!isValidInteger(stringVal))
            throw new CustomRunTimeException("Config Property '"+key+"' is not a number");
        return getNumericValue(stringVal);
    }


}
