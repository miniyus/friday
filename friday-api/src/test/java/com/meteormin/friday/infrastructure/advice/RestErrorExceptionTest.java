package com.meteormin.friday.infrastructure.advice;

import com.meteormin.friday.common.error.ErrorCode;
import com.meteormin.friday.common.error.RestErrorCode;
import com.meteormin.friday.common.error.RestErrorException;
import com.meteormin.friday.hexagonal.application.UsecaseTest;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
class RestErrorExceptionTest extends UsecaseTest {
    private final Faker faker = new Faker();

    @Test
    void restErrorExceptionTest() {
        var testSentence = faker.lorem().sentence();
        var ex = new RestErrorException(
            testSentence,
            RestErrorCode.NOT_FOUND
        );

        var errorCode = ex.getErrorCode();
        assertThat(errorCode)
            .isInstanceOf(ErrorCode.class);
        assertThat(errorCode.getErrorCode())
            .isEqualTo(RestErrorCode.NOT_FOUND.getErrorCode());
        assertThat(errorCode.getStatusCode())
            .isEqualTo(RestErrorCode.NOT_FOUND.getStatusCode());
        assertThat(errorCode.getHttpStatus())
            .isEqualTo(RestErrorCode.NOT_FOUND.getHttpStatus());
        assertThat(ex.getMessage())
            .isEqualTo(testSentence);
    }

    @Test
    void restErrorExceptionArgsTest() {
        var testSentence = faker.lorem().sentence();
        var args = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            args.add(faker.lorem().word());
        }

        var ex = new RestErrorException(
            RestErrorCode.NOT_FOUND,
            testSentence,
            args.toArray()
        );

        var errorCode = ex.getErrorCode();
        assertThat(errorCode)
            .isInstanceOf(ErrorCode.class);
        assertThat(errorCode.getErrorCode())
            .isEqualTo(RestErrorCode.NOT_FOUND.getErrorCode());
        assertThat(errorCode.getStatusCode())
            .isEqualTo(RestErrorCode.NOT_FOUND.getStatusCode());
        assertThat(errorCode.getHttpStatus())
            .isEqualTo(RestErrorCode.NOT_FOUND.getHttpStatus());
        assertThat(ex.getMessage())
            .isEqualTo(testSentence);

        assertThat(ex.getArgs())
            .hasSize(10);

        IntStream.range(0, ex.getArgs().size())
            .forEach(
                idx -> assertThat(ex.getArgs().get(idx))
                    .isEqualTo(args.get(idx))
            );
    }
}
