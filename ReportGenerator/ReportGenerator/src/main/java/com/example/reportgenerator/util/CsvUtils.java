package com.example.reportgenerator.util;

import com.example.reportgenerator.model.InputRecord;
import com.example.reportgenerator.model.ReferenceRecord;
import com.example.reportgenerator.model.OutputRecord;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvUtils {

    private static final Logger logger = LoggerFactory.getLogger(CsvUtils.class);

    public static List<InputRecord> readInputRecords(String filePath) {
        logger.info("Reading input records from file: {}", filePath);
        try (FileReader reader = new FileReader(filePath)) {
            CsvToBean<InputRecord> csvToBean = new CsvToBeanBuilder<InputRecord>(reader)
                    .withType(InputRecord.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvToBean.parse();
        } catch (IOException e) {
            logger.error("Error reading input CSV file: {}", filePath, e);
            throw new RuntimeException("Error reading input CSV file: " + filePath, e);
        }
    }

    public static List<ReferenceRecord> readReferenceRecords(String filePath) {
        logger.info("Reading reference records from file: {}", filePath);
        try (FileReader reader = new FileReader(filePath)) {
            CsvToBean<ReferenceRecord> csvToBean = new CsvToBeanBuilder<ReferenceRecord>(reader)
                    .withType(ReferenceRecord.class)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvToBean.parse();
        } catch (IOException e) {
            logger.error("Error reading reference CSV file: {}", filePath, e);
            throw new RuntimeException("Error reading reference CSV file: " + filePath, e);
        }
    }

    public static void writeOutputRecords(List<OutputRecord> outputRecords, String filePath, String outputPath) {
        logger.info("Writing output records to file: {}", filePath);
        File outputFolder = new File(outputPath);
        if (!outputFolder.exists()) {
            boolean wasSuccessful = outputFolder.mkdirs();
            if (!wasSuccessful) {
                logger.error("Failed to create output directory: {}", outputPath);
                throw new RuntimeException("Failed to create output directory: " + outputPath);
            }
        }
        filePath = outputPath + File.separator + filePath;

        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            writer.writeNext(new String[]{"outfield1", "outfield2", "outfield3", "outfield4", "outfield5"});

            for (OutputRecord record : outputRecords) {
                writer.writeNext(new String[]{
                        record.getOutfield1(),
                        record.getOutfield2(),
                        record.getOutfield3(),
                        record.getOutfield4(),
                        record.getOutfield5()
                });
            }
        } catch (IOException e) {
            logger.error("Error writing output CSV file: {}", filePath, e);
            throw new RuntimeException("Error writing output CSV file: " + filePath, e);
        }
    }
}
