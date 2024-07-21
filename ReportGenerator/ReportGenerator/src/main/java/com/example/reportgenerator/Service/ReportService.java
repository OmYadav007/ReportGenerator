package com.example.reportgenerator.Service;

import com.example.reportgenerator.model.InputRecord;
import com.example.reportgenerator.model.ReferenceRecord;
import com.example.reportgenerator.model.OutputRecord;
import com.example.reportgenerator.util.CsvUtils;
import com.example.reportgenerator.util.ExcelUtils;
import com.example.reportgenerator.util.TransformationRules;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Service
public class ReportService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${report.input-file-path}")
    private String inputFilePath;
    @Value("${report.reference-file-path}")
    private String referenceFilePath;
    @Value("${report.output-file-path}")
    private String outputPath;


    public String generateReport(String inputFilePath, String referenceFilePath) throws IOException{
        List<InputRecord> inputRecords = generateInputRecords(inputFilePath);
        List<ReferenceRecord> referenceRecords = generateReferenceRecords(referenceFilePath);

        List<OutputRecord> outputRecords = TransformationRules.applyTransformation(inputRecords, referenceRecords);
        String uniqueOutputName = UUID.randomUUID().toString();
        String filePath = "output-" + uniqueOutputName + ".csv";
        CsvUtils.writeOutputRecords(outputRecords, filePath,outputPath);
        return filePath;
    }
    public List<InputRecord> generateInputRecords(String inputFilePath) throws IOException
    {
        String contentType = Files.probeContentType(Paths.get(inputFilePath));
        List<InputRecord> inputRecords = new ArrayList<>();

        if (inputFilePath.contains("csv")) {
            inputRecords = CsvUtils.readInputRecords(inputFilePath);
        } else if ("application/json".equals(contentType)) {
           inputRecords = List.of(objectMapper.readValue(new FileInputStream(inputFilePath), InputRecord[].class));
        } else if (contentType != null && inputFilePath.contains("xlsx")) {
            inputRecords = ExcelUtils.readExcelInputRecords(inputFilePath);
        }
        else {
            throw new IllegalArgumentException("Unsupported file type: " + contentType);
        }
        return inputRecords;
    }
    public List<ReferenceRecord> generateReferenceRecords(String referenceFilePath) throws IOException {
        String contentType = Files.probeContentType(Paths.get(referenceFilePath));
        List<ReferenceRecord> referenceRecords = new ArrayList<>();

        if (referenceFilePath.contains("csv")) {
            referenceRecords = CsvUtils.readReferenceRecords(referenceFilePath);
       } else if ("application/json".equals(contentType)) {
           referenceRecords = List.of(objectMapper.readValue(new FileInputStream(referenceFilePath), ReferenceRecord[].class));
        } else if (contentType != null && referenceFilePath.contains("xlsx")) {
            referenceRecords = ExcelUtils.readExcelReferenceRecords(referenceFilePath);
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + contentType);
        }
        return referenceRecords;
    }

    @Scheduled(cron = "${report.cron}")
    public void scheduledReportGeneration() throws IOException{
        generateReport(inputFilePath, referenceFilePath);
    }
}
