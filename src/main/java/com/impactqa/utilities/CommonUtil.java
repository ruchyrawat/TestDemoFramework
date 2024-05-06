package com.impactqa.utilities;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.testng.Assert;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @version 1.0
 * @description This class is Util class which will provides common methods for validations/manipulations/conversion
 * @since 2021-03-20
 */
public class CommonUtil {

    /**
     * Return the status whether the given string is valid or not
     *
     * @param inputString - The input string that has to be tested
     * @return true if the string is valid else false
     */
    public static boolean isValidString(String inputString) {
        return (inputString != null && !inputString.trim().isEmpty());
    }

    /**
     * Return the status whether the given string is valid number
     *
     * @param inputNumberInString - The input number in string format that has to be tested
     * @return true if the integer is valid else false
     */
    public static boolean isValidInteger(String inputNumberInString) {
        return StringUtils.isNumeric(inputNumberInString);
    }

    /**
     * Converts the given string to number and return it
     *
     * @param inputNumberInString - The input number in string format
     * @return integer value - The converted integer value
     */
    public static int getNumericValue(String inputNumberInString) {
        return Integer.valueOf(inputNumberInString);
    }

    /**
     * Return the status whether the given @regexPatternStr matches with @textContent
     *
     * @param regexPatternStr - The Regex pattern that has to be matched
     * @param textContent     - The text that has to be matched with the regex pattern
     * @return true if the pattern matched with the text else false
     */
    public static boolean isRegexPatternMatched(String regexPatternStr, String textContent) {
        Pattern pattern = Pattern.compile(regexPatternStr);
        Matcher matcher = pattern.matcher(textContent);
        return matcher.find();
    }

    /**
     * Return the extracted  Sub-String using 1st regex Group.
     * the regex @regexPatternStr should have grouping pattern like "'(.*?)'".
     * It will first check whether pattern is found. then it will return 1st group
     *
     * @param regexPatternStr - The regex pattern that has to be matched
     * @param textContent     - The content that has to be matched with regex
     * @return true if the Sub String matched else false
     */
    public static String extractSubStringUsingRegex(String regexPatternStr, String textContent) {
        String extractedValue = null;
        Pattern pattern = Pattern.compile(regexPatternStr);
        Matcher matcher = pattern.matcher(textContent);
        if (matcher.find()) {
            if (matcher.groupCount() > 0)
                extractedValue = matcher.group(1);
            else
                Assert.fail("No matching group found for the extraction");
        } else
            Assert.fail("Pattern '" + regexPatternStr + "' cannot be found in the content: " + textContent);
        return extractedValue;
    }


    /**
     * Get the portion of the number with the help of @input and @percentange
     *
     * @param input       - Input number
     * @param percentange - Percentage of the number
     * @return portion of the number
     * @input * (@percentage / 100.0)
     * <p>
     * For Eg: 25% of 1000 = 250
     */
    public static int getPortionOfTheNumber(int input, int percentange) {

        double inputD = Double.valueOf(input);
        double percentangeD = Double.valueOf(percentange);

        return (int) (inputD * (percentangeD / 100.0));
    }

}
