package com.example.reportgenerator.util;

import com.example.reportgenerator.model.InputRecord;
import com.example.reportgenerator.model.ReferenceRecord;
import com.example.reportgenerator.model.OutputRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class TransformationRules {

    private static final Logger logger = LoggerFactory.getLogger(TransformationRules.class);

    public static List<OutputRecord> applyTransformation(List<InputRecord> inputRecords, List<ReferenceRecord> referenceRecords) {
        logger.info("Starting transformation of input records");
        List<OutputRecord> transformedRecords = inputRecords.stream().map(inputRecord -> {
            ReferenceRecord referenceRecord = findReferenceRecord(inputRecord, referenceRecords);
            OutputRecord outputRecord = new OutputRecord();
            outputRecord.setOutfield1(inputRecord.getField1() + inputRecord.getField2());
            outputRecord.setOutfield2(referenceRecord.getRefdata1());
            outputRecord.setOutfield3(referenceRecord.getRefdata2() + referenceRecord.getRefdata3());
            outputRecord.setOutfield4(String.valueOf(Double.max(Double.parseDouble(String.valueOf(inputRecord.getField5())), referenceRecord.getRefdata4())));
            outputRecord.setOutfield5(String.valueOf(Double.max(Double.parseDouble(String.valueOf(inputRecord.getField5())), referenceRecord.getRefdata4())));
            return outputRecord;
        }).collect(Collectors.toList());
        logger.info("Completed transformation of input records");
        return transformedRecords;
    }

    private static ReferenceRecord findReferenceRecord(InputRecord inputRecord, List<ReferenceRecord> referenceRecords) {
        return referenceRecords.stream()
                .filter(ref -> ref.getRefkey1().equals(inputRecord.getRefkey1()) && ref.getRefkey2().equals(inputRecord.getRefkey2()))
                .findFirst()
                .orElseThrow(() ->
                {
                    logger.error("Reference data not found for keys: {}, {}", inputRecord.getRefkey1(), inputRecord.getRefkey2());
                    return new IllegalArgumentException("Reference data not found for keys: " + inputRecord.getRefkey1() + ", " + inputRecord.getRefkey2());
                });
    }
}
