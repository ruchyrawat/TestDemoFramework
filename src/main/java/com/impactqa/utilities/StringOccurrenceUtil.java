package com.impactqa.utilities;

import org.testng.Assert;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @version 1.0
 * @since   2021-03-20
 * @description StringOccurrenceUtil implements different logics to the number of occurrences of the substring in the given string
 */
public class StringOccurrenceUtil extends CommonUtil {


    /**
     * Set the type of logic to be used to find the number occurrence of the substring in the given string
     */
    private final LogicToUse logicToBeUsed = LogicToUse.STRING_REPLACE_METHOD;
    public enum LogicToUse{
        STRING_REPLACE_METHOD,
        STRING_INDEXOF_METHOD,
        REGEX_PATTERN,
    }

    /**
     * Return the number of occurrence of the @searchTerm in the @searchContent
     * set private variable logicToBeUsed in the class to choose the type of logic to be used
     * @param searchTerm
     * @param searchContent
     * @return number of occurrence - int
     */

    public int getNumberOfOccurrences(String searchTerm, String searchContent){
        Assert.assertTrue(isValidString(searchTerm), "searchTerm shouldn't be null or empty.");
        Assert.assertTrue(isValidString(searchContent), "searchContent shouldn't be null or empty.");
        Assert.assertTrue(searchTerm.length() < searchContent.length(), "searchTerm shouldn't be greater than searchContent");
        switch (logicToBeUsed)
        {
            case STRING_REPLACE_METHOD:
                return getNumberOfOccurrencesUsingFormula(searchTerm, searchContent);
            case STRING_INDEXOF_METHOD:
                return getNumberOfOccurrencesUsingIndexofMethod(searchTerm, searchContent);
            case REGEX_PATTERN:
                return getNumberOfOccurrencesUsingRegexPattern(searchTerm, searchContent);
            default:
                throw new IllegalStateException("Unexpected value: " + logicToBeUsed);
        }
    }

    /**
     * Return the number of occurrence of @searchTerm in the @searchContent using string replace and the formula
     * (content_size-(content_size_after_removing_term))/term_size
     * @param searchTerm
     * @param searchContent
     * @return number of occurrence - int
     */
    private int getNumberOfOccurrencesUsingFormula(String searchTerm, String searchContent){
        return (searchContent.length()-searchContent.replace(searchTerm, "").length())/searchTerm.length();
    }

    /**
     * Return the number of occurrence of @searchTerm in the @searchContent using string indexOf method
     * scans the searchContent from left to right with the progressive dynamic start index ( start from the position where last found searchTerm ends )
     * @param searchTerm
     * @param searchContent
     * @return number of occurrence - int
     */
    private int getNumberOfOccurrencesUsingIndexofMethod(String searchTerm, String searchContent){
        int lastIndex = 0;
        int count = 0;
        while(lastIndex != -1){
            lastIndex = searchContent.indexOf(searchTerm,lastIndex);
            if(lastIndex != -1){
                count ++;
                lastIndex += searchTerm.length();
            }
        }
        return count;
    }

    /**
     * Return the number of occurrence of @searchTerm in the @searchContent using regular expressions Pattern matching
     * @param searchTerm
     * @param searchContent
     * @return number of occurrence - int
     */
    private int getNumberOfOccurrencesUsingRegexPattern(String searchTerm, String searchContent){
        Matcher matcher = Pattern.compile(Pattern.quote(searchTerm)).matcher(searchContent);
        int count = 0;
        while (matcher.find()) {
            count++;
        }
        return count;
    }


}
