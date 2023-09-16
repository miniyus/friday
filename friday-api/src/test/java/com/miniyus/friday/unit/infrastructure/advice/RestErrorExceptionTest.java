package com.miniyus.friday.unit.infrastructure.advice;

import com.github.javafaker.Faker;
import com.miniyus.friday.common.error.ErrorCode;
import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.stream.IntStream;

@ActiveProfiles("test")
public class RestErrorExceptionTest {
    private final Faker faker = new Faker();

    @Test
    public void restErrorExceptionTest() {
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
    public void restErrorExceptionArgsTest() {
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
