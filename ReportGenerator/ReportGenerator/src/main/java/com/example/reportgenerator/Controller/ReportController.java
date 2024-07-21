package com.example.reportgenerator.Controller;

import com.example.reportgenerator.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class ReportController {
    @Autowired
    private ReportService reportService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateReport(@RequestParam String inputFilePath, @RequestParam String referenceFilePath) throws IOException {
        String response = reportService.generateReport(inputFilePath, referenceFilePath);
        return ResponseEntity.ok(response);
    }
}
