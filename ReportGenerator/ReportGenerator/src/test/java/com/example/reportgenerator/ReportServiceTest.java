package com.example.reportgenerator;

import com.example.reportgenerator.Service.ReportService;
import com.example.reportgenerator.model.InputRecord;
import com.example.reportgenerator.model.ReferenceRecord;
import com.example.reportgenerator.model.OutputRecord;
import com.example.reportgenerator.util.CsvUtils;
import com.example.reportgenerator.util.ExcelUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReportServiceTest {
    @Value("${report.input-file-path}")
    private String inputFilePath;
    @Value("${report.reference-file-path}")
    private String referenceFilePath;
    @Value("${report.output-file-path}")
    private String outputPath;

    @Mock
    private CsvUtils csvUtils;
    @Mock
    private ExcelUtils excelUtils;
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private ReportService reportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateReportWithValidInputs() throws IOException {
        // Setup
        List<InputRecord> inputRecords = Arrays.asList(new InputRecord());
        List<ReferenceRecord> referenceRecords = Arrays.asList(new ReferenceRecord());
        List<OutputRecord> outputRecords = Arrays.asList(new OutputRecord());
        when(csvUtils.readInputRecords(anyString())).thenReturn(inputRecords);
        when(csvUtils.readReferenceRecords(anyString())).thenReturn(referenceRecords);
        doNothing().when(csvUtils).writeOutputRecords(outputRecords, "output.csv", outputPath);

        // Execute
        String result = reportService.generateReport(inputFilePath, referenceFilePath);

        // Verify
        assertNotNull(result);
        verify(csvUtils, times(1)).readInputRecords(anyString());
        verify(csvUtils, times(1)).readReferenceRecords(anyString());
    }
}