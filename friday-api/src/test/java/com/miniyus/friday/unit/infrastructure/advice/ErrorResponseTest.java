package com.miniyus.friday.unit.infrastructure.advice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniyus.friday.common.error.ErrorCode;
import com.miniyus.friday.common.response.SimplePage;
import com.miniyus.friday.infrastructure.advice.ErrorResponse;
import com.miniyus.friday.unit.common.response.SimplePageTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/09/07
 */
@JsonTest
@ActiveProfiles("test")
@MockBean(JpaMetamodelMappingContext.class)
public class ErrorResponseTest {
    @Autowired
    private JacksonTester<ErrorResponse> json;

    @Test
    public void toJsonTest() throws IOException {
        HashMap<String, List<String>> details = new HashMap<>();
        var field1List = new ArrayList<String>();
        field1List.add("test fail!");

        details.put("field1", field1List);

        ErrorResponse tsetResponse = new ErrorResponse(
                ErrorCode.INTERNAL_SERVER_ERROR,
                "testMessage",
                details);
        assertThat(json.write(tsetResponse))
                .hasJsonPathStringValue("@.timestamp")
                .hasJsonPathStringValue("@.error")
                .hasJsonPathStringValue("@.message")
                .hasJsonPathStringValue("@.details.field1[0]");
    }
}
