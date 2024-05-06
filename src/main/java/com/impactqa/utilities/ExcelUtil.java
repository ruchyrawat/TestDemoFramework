package com.impactqa.utilities;

import com.impactqa.exceptions.CustomRunTimeException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;


/**
 *
 * @version 1.0
 * @description This class is Util class for reading and writing data from an excel source.
 * @since 2021-03-20
 */
public class ExcelUtil {

    private Workbook wb;
    private Sheet sheet;
    private int headerRowNumber=0;

    /**
     * This will read the excel sheet (@sheetName) from the given workbook @path and will create a workbook instance
     *
     * if workbook is password protected then following parameter should be set in the config.properties
     * IsTestDataWorkbookEncrypted = true
     * TestDataSecurityKey = {Encrypted_key}
     *
     * @param path      - The file location of the excel file
     * @param sheetName - The sheet name in the excel file
     */
    public void setWorkbook(String path, String sheetName) {
        try {
            if(FrameworkConfig.getStringConfigProperty("IsTestDataWorkbookEncrypted").equals("true"))
                wb = WorkbookFactory.create(new FileInputStream(path), CryptoUtils.decryptTheValue(FrameworkConfig.getStringConfigProperty("TestDataSecurityKey")));
            else
                wb = WorkbookFactory.create(new FileInputStream(path));
//            wb = new XSSFWorkbook(new FileInputStream(path));
            sheet = wb.getSheet(sheetName);
        } catch (IOException e) {
            throw new CustomRunTimeException("Error occurred while reading the excel file. Path: " + path, e);
        } catch (InvalidFormatException e) {
            throw new CustomRunTimeException("Error occurred while reading the excel file. File format should be xls or xlsx Path: " + path, e);
        } catch (Exception e) {
            throw new CustomRunTimeException("Error occurred while reading the excel file. Path: " + path, e);
        }
    }

    /**
     * This method sets the workbook with given name
     * @param sheetName - The sheet name in the excel file
     */
    public void setSheet(String sheetName){
        sheet = wb.getSheet(sheetName);
        if(sheet==null)
            throw new RuntimeException("Excel sheet '"+sheetName+"' is not found");

    }

    /**
     * This method sets the workbook with given name
     */
    public String getCurrentSheet(){
        if(sheet!=null)
            return sheet.getSheetName();
        else
            return null;
    }

    /**
     * This will get all the cell values(Columns) from the header row (1st row) from the given @sheet
     *
     * @param sheet - the name of the sheet
     * @return list of headers
     */
    private List<String> getColumnNames(Sheet sheet) {
        final Row row = sheet.getRow(headerRowNumber);
        final List<String> columnValues = new ArrayList<>();
        final int firstCellNum = row.getFirstCellNum();
        final int lastCellNum = row.getLastCellNum();
        for (int i = firstCellNum; i < lastCellNum; i++) {
            final Cell cell = row.getCell(i);
            columnValues.add(getStringCellValue(cell));
        }
        return columnValues;
    }

    /**
     * This will iterate all the rows to match the row for the given dataID
     * Once it finds the specific data row, it will create the key-value (Map) from header row (1st row) and data row (1+n th row)
     *
     * @param dataID - Unique ID given to the TestData row
     * @return @Map<String,String> it will return key-value {key: column-names | value: data-cell-value}
     */
    public Map<String, String> getRowDataMatchingDataId(String dataID) {
        final List<String> rowData = new ArrayList<>();
        final Map<String, String> rowVal = new LinkedHashMap<>();
        Object value;

        final List<String> columnNames = getColumnNames(sheet);
        final int totalRows = sheet.getPhysicalNumberOfRows();
        final Row headerRow = sheet.getRow(headerRowNumber);

        final int firstCellNum = headerRow.getFirstCellNum();
        final int lastCellNum = headerRow.getLastCellNum();

        for (int i = (headerRowNumber+1); i < totalRows; i++) {
            final Row row = sheet.getRow(i);
            if(row==null)
                continue;
            final String testLinkID = getStringCellValue(row.getCell(0));
            if (dataID.equalsIgnoreCase(testLinkID)) {
                for (int j = firstCellNum; j < lastCellNum; j++) {
                    final Cell cell = row.getCell(j);
                    rowData.add(getStringCellValue(cell));
                    if(!"".equals(columnNames.get(j)))
                        rowVal.put(columnNames.get(j), rowData.get(j).trim());
                }
                break;
            }
        }
        return rowVal;
    }


    /**
     * This will get all the rows from the specific sheet
     * Each row will be represented as a key-value (Map) from header row (1st row) and data row (1+n th row)
     * @return the List<Map<String, String>> All row data
     */
    public List<Map<String, String>> getAllRows() {
        final List<Map<String, String>> retList = new LinkedList<>();

        Object value;
        final List<String> coulmnNames = getColumnNames(sheet);
        final int totalRows = sheet.getPhysicalNumberOfRows();
        final Row row = sheet.getRow(headerRowNumber);
        final int firstCellNum = row.getFirstCellNum();
        final int lastCellNum = row.getLastCellNum();
        for (int i = (headerRowNumber+1); i < totalRows; i++) {
            final Row rows = sheet.getRow(i);
            if(rows!=null){
                final String cell0 = getStringCellValue(rows.getCell(0));
                if ((!"".equalsIgnoreCase(cell0))) {
                    final List<String> rowData = new ArrayList<>();
                    final Map<String, String> rowVal = new LinkedHashMap<>();
                    for (int j = firstCellNum; j < lastCellNum; j++) {
                        final Cell cell = rows.getCell(j);
                        rowData.add(getStringCellValue(cell));
                        rowVal.put(coulmnNames.get(j), rowData.get(j).trim());
                    }
                    retList.add(rowVal);
                }
            }
        }
        return retList;
    }

    /**
     * This method reads cell and returns cell value
     * @param cell - Cell value
     * returns String
     */
    private String getStringCellValue(Cell cell){
        String retVal = null;
        if (cell == null
                || cell.getCellType() == CellType.BLANK) {
            retVal = "";
        } else if (cell.getCellType() == CellType.NUMERIC) {
            final Double val = cell.getNumericCellValue();
            Object value = val.intValue();
            retVal = value.toString();
        } else if (cell.getCellType() == CellType.STRING) {
            retVal = cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.FORMULA) {
            retVal = cell.getStringCellValue();
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            final boolean booleanVal = cell.getBooleanCellValue();
            retVal = String.valueOf(booleanVal);
        } else if (DateUtil.isCellDateFormatted(cell)) {
            retVal = cell.getDateCellValue().toString();
        } else if (cell.getCellType() == CellType.ERROR) {
            throw new RuntimeException(" Cell Type is not supported ");
        }

        return retVal;
    }

    public void setHeaderRowNumber(int headerRowNumber)
    {
        this.headerRowNumber = headerRowNumber;
    }


    public Workbook getWb() {
        return wb;
    }
}
