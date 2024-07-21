package com.example.reportgenerator.util;

import com.example.reportgenerator.model.InputRecord;
import com.example.reportgenerator.model.ReferenceRecord;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelUtils {
    private static final Logger logger = LoggerFactory.getLogger(com.example.reportgenerator.util.ExcelUtils.class);

    public static List<InputRecord> readExcelInputRecords(String filePath) throws IOException {
        List<InputRecord> inputRecords = new ArrayList<>();
        try (FileInputStream excelFile = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();

            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                if (currentRow.getRowNum() == 0) continue;

                InputRecord inputRecord = new InputRecord();
                inputRecord.setField1(currentRow.getCell(0).getStringCellValue());
                inputRecord.setField2(currentRow.getCell(1).getStringCellValue());
                inputRecord.setField3(currentRow.getCell(2).getStringCellValue());
                inputRecord.setField4(currentRow.getCell(3).getStringCellValue());
                inputRecord.setField5(currentRow.getCell(4).getNumericCellValue());
                inputRecord.setRefkey1(currentRow.getCell(5).getStringCellValue());
                inputRecord.setRefkey2(currentRow.getCell(6).getStringCellValue());

                inputRecords.add(inputRecord);
            }
            workbook.close();
        }
        catch (IOException e) {
            logger.error("Error reading Excel input records from {}", filePath, e);
            throw e;
        }
        logger.info("Finished reading Excel input records from {}", filePath);
        return inputRecords;
    }


    public static List<ReferenceRecord> readExcelReferenceRecords(String filePath) throws IOException {
        logger.info("Starting to read Excel reference records from {}", filePath);
        List<ReferenceRecord> referenceRecords = new ArrayList<>();
        try (FileInputStream excelFile = new FileInputStream(filePath)) {
            Workbook workbook = WorkbookFactory.create(excelFile);
            Sheet datatypeSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = datatypeSheet.iterator();

            while (iterator.hasNext()) {
                Row currentRow = iterator.next();
                if (currentRow.getRowNum() == 0) continue;

                ReferenceRecord referenceRecord = new ReferenceRecord();
                referenceRecord.setRefkey1(currentRow.getCell(0).getStringCellValue());
                referenceRecord.setRefkey2(currentRow.getCell(1).getStringCellValue());
                referenceRecord.setRefdata1(currentRow.getCell(2).getStringCellValue());
                referenceRecord.setRefdata2(currentRow.getCell(3).getStringCellValue());
                referenceRecord.setRefdata3(currentRow.getCell(4).getStringCellValue());
                referenceRecord.setRefdata4(currentRow.getCell(5).getNumericCellValue());

                referenceRecords.add(referenceRecord);
            }
            workbook.close();
        }
        catch (IOException e) {
            logger.error("Error reading Excel reference records from {}", filePath, e);
            throw e;
        }
        logger.info("Finished reading Excel reference records from {}", filePath);
        return referenceRecords;
    }
}