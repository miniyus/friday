package com.meteormin.friday.common.pagination;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/09/07
 */
// @ExtendWith(SpringExtension.class)
@JsonTest
@ActiveProfiles("test")
@MockBean(JpaMetamodelMappingContext.class)
class SimplePageTest {
    @Autowired
    private JacksonTester<SimplePage<Map<String, String>>> json;

    @Test
    void simplePageDefaultFieldTest() throws IOException {
        Map<String, String> detail = new HashMap<>();

        detail.put("field1", "hello");
        detail.put("field2", "world");

        var details = new ArrayList<Map<String, String>>();
        details.add(detail);

        var sort = new ArrayList<String>();

        SimplePage<Map<String, String>> testResponse = new SimplePage<Map<String, String>>(
            "tests",
            details,
            1L,
            1,
            1,
            10,
            sort);

        assertThat(json.write(testResponse))
            .hasJsonPathNumberValue("@.totalElements")
            .hasJsonPathNumberValue("@.totalPages")
            .hasJsonPathNumberValue("@.page")
            .hasJsonPathNumberValue("@.size")
            .hasJsonPathArrayValue("@.sort")
            .hasJsonPathArrayValue("@.tests");
    }

    @Test
    void simplePageCustomFieldTest() throws IOException {
        Map<String, String> detail = new HashMap<>();

        detail.put("field1", "hello");
        detail.put("field2", "world");

        var details = new ArrayList<Map<String, String>>();
        details.add(detail);

        var page = PageRequest.of(1, 10, Sort.by("field1"));
        SimplePage<Map<String, String>> tsetResponse =
            new SimplePage<Map<String, String>>(
                details,
                1L,
                page,
                "custom");

        assertThat(json.write(tsetResponse))
            .hasJsonPathNumberValue("@.totalElements")
            .hasJsonPathNumberValue("@.totalPages")
            .hasJsonPathNumberValue("@.page")
            .hasJsonPathNumberValue("@.size")
            .hasJsonPathArrayValue("@.sort")
            .hasJsonPathArrayValue("@.custom");
    }
}
