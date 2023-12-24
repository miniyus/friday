package com.miniyus.friday.common.util.dms;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.List;

import static com.precisionbio.cuttysark.common.util.dms.QrDataGenerator.QrDataRule;

/**
 * TODO: 이슈부터 해결
 */
@Slf4j
public class QrDataEncoder {
    private final List<QrDataRule> rules;

    public QrDataEncoder(
        List<QrDataRule> rules
    ) {
        this.rules = rules;
    }

    public String encode(AnalyteResult analyteResult) {
        if (validateByteSize(analyteResult)) {
            long unixTime = analyteResult.date().atZone(
                ZoneId.systemDefault()
            ).toInstant().toEpochMilli();

            StringBuilder sb = new StringBuilder();
            sb.append(analyteResult.serial())
                .append(unixTime)
                .append(analyteResult.cartridgeId())
                .append("|");
            analyteResult.resultData()
                .stream().
                sorted(Comparator.comparing(AnalyteResult.ResultData::analyteId))
                .forEach(resultData -> {
                    sb.append(resultData.analyteId())
                        .append(",")
                        .append(resultData.result());
                });

            return sb.toString();
        }
        return "";
    }

    private boolean validateByteSize(AnalyteResult analyteResult) {
        return rules.stream().filter(rule -> {
                if (rule.byteSize() == null) {
                    return false;
                }

                return switch (rule.identifier()) {
                    case "serial" -> analyteResult.serial().getBytes().length
                        != rule.byteSize();
                    case "date" -> {
                        long unixTime = analyteResult.date().atZone(
                            ZoneId.systemDefault()
                        ).toInstant().toEpochMilli();
                        yield String.valueOf(unixTime).length() != rule.byteSize();
                    }
                    case "cartridgeId" -> analyteResult.cartridgeId() != null;
                    default -> false;
                };
            }).toList()
            .isEmpty();
    }

    @Builder
    public record AnalyteResult(
        String patientId,
        LocalDateTime date,
        String cartridgeId,
        String serial,
        List<ResultData> resultData
    ) {

        @Builder
        public record ResultData(
            String analyteId,
            String result
        ) {

        }
    }
}
