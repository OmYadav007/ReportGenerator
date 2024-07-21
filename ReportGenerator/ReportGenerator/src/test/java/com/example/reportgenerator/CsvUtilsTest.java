package com.example.reportgenerator;

import com.example.reportgenerator.model.InputRecord;
import com.example.reportgenerator.model.OutputRecord;
import com.example.reportgenerator.model.ReferenceRecord;
import com.example.reportgenerator.util.CsvUtils;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CsvUtilsTest {

    @InjectMocks
    private CsvUtils csvUtils;
    @Value("${report.input-file-path}")
    private String inputFilePath;
    @Value("${report.reference-file-path}")
    private String referenceFilePath;
    @Value("${report.output-file-path}")
    private String outputPath;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testReadInputRecords() throws IOException {

        List<InputRecord> result = CsvUtils.readInputRecords(inputFilePath);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testReadReferenceRecords() throws IOException {

        List<ReferenceRecord> result = csvUtils.readReferenceRecords(referenceFilePath);

        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    public void testWriteOutputRecords() throws IOException {
        List<OutputRecord> outputRecords = Arrays.asList(new OutputRecord());
        String filePath = "output.csv";

        assertDoesNotThrow(() -> csvUtils.writeOutputRecords(outputRecords, filePath, outputPath));
    }
}