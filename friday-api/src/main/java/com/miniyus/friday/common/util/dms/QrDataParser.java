package com.miniyus.friday.common.util.dms;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.precisionbio.cuttysark.users.domain.profiles.Species;
import lombok.Builder;
import org.springframework.lang.NonNull;

import java.util.*;

import static com.precisionbio.cuttysark.common.util.dms.QrDataGenerator.QrDataRule;

public class QrDataParser {
    private final List<QrDataRule> rules;

    private final ObjectMapper objectMapper;

    public QrDataParser(
        List<QrDataRule> rules,
        ObjectMapper objectMapper) {
        this.rules = rules;
        this.objectMapper = objectMapper;
    }

    public AnalyteResult parse(String qrString) {
        String[] split = qrString.split("\\|");
        if (split.length != 2) {
            throw new RuntimeException("Invalid QR Code");
        }

        String identifier = split[0];
        String data = split[1];

        var base64Dec = Base64.getDecoder();
        String decodedIds = new String(base64Dec.decode(identifier));
        Map<String, Object> tmpMap = mapping(decodedIds, data);
        return objectMapper.convertValue(tmpMap, AnalyteResult.class);
    }

    @NonNull
    private Map<String, Object> mapping(String decodedIds, String data) {
        Map<String, Object> tmpMap = new HashMap<>();
        int startIndex = 0;
        for (QrDataRule rule : rules) {
            String value;
            if (rule.byteSize() == null) {
                value = decodedIds.substring(startIndex);
            } else {
                value = decodedIds.substring(startIndex, startIndex + rule.byteSize());
                startIndex += rule.byteSize();
            }

            tmpMap.put(rule.identifier(), value);
        }

        List<String> dataSplit = Arrays.asList(data.split(","));
        tmpMap.put("resultData", dataSplit);
        return tmpMap;
    }

    @Builder
    public record AnalyteResult(
        String serial,
        Long date,
        String cartridgeId,
        Species species,
        List<String> resultData
    ) {
    }
}
