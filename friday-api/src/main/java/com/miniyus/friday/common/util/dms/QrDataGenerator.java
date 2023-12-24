package com.miniyus.friday.common.util.dms;

import lombok.Builder;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

public class QrDataGenerator {
    public static List<QrDataRule> rules = new ArrayList<>();

    public static void initialize() {
        rules.add(QrDataRule.builder()
            .identifier("serial")
            .byteSize(12)
            .build()
        );
        rules.add(QrDataRule.builder()
            .identifier("date")
            .byteSize(10)
            .build()
        );
        rules.add(QrDataRule.builder()
            .identifier("cartridgeId")
            .byteSize(null)
            .build()
        );
    }
    @Builder
    public record QrDataRule(
        @NonNull String identifier,
        @Nullable Integer byteSize
    ) {
    }
}
