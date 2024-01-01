package com.miniyus.friday.infrastructure.advice;

import com.miniyus.friday.common.error.ErrorResponse;
import com.miniyus.friday.common.error.RestErrorCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
class ErrorResponseTest {
    @Autowired
    private JacksonTester<ErrorResponse> json;

    @Test
    void toJsonTest() throws IOException {
        HashMap<String, List<String>> details = new HashMap<>();
        var field1List = new ArrayList<String>();
        field1List.add("test fail!");

        details.put("field1", field1List);

        ErrorResponse testResponse = new ErrorResponse(
                RestErrorCode.INTERNAL_SERVER_ERROR,
                "testMessage",
                details);
        assertThat(json.write(testResponse))
                .hasJsonPathStringValue("$.timestamp")
                .hasJsonPathStringValue("$.error")
                .hasJsonPathStringValue("$.message")
                .hasJsonPathStringValue("$.details.field1[0]");
    }
}
