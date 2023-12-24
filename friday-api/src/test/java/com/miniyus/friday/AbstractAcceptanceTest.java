package com.miniyus.friday;

import com.precisionbio.cuttysark.infrastructure.aws.s3.S3Service;
import com.precisionbio.cuttysark.infrastructure.config.SecurityConfiguration;
import com.precisionbio.cuttysark.infrastructure.security.social.SocialProvider;
import com.precisionbio.cuttysark.users.adapter.in.auth.request.SocialLoginRequest;
import com.precisionbio.cuttysark.users.domain.Client;
import com.precisionbio.cuttysark.users.domain.Token;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractAcceptanceTest extends StubDmsClient {
    @LocalServerPort
    int port;

    @MockBean
    private S3Service s3;

    protected static String accessToken;

    @BeforeEach
    public void setUp() throws Exception {
        super.setup();
        RestAssured.port = port;

        var faker = fakeInjector.getFaker();
        lenient().when(s3.save(any(), any())).thenReturn(true);
        lenient().when(s3.getUrl(any())).thenReturn(new URL(faker.internet().avatar()));
    }

    protected Token socialLoginTest(Client client) {
        var req = SocialLoginRequest.builder()
            .snsId(fakeInjector.getFaker().internet().uuid())
            .provider(fakeInjector.randomEnum(SocialProvider.class).value())
            .client(client.value())
            .secret("secret")
            .build();

        ExtractableResponse<Response> response = RestAssured
            .given().log().all()
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .body(req)
            .when()
            .post(SecurityConfiguration.AUTH_TOKEN_URL)
            .then().log().all()
            .extract();
        assertThat(response.statusCode()).isEqualTo(201);

        Token token = response.body().as(Token.class);
        assertThat(token).isNotNull()
            .hasFieldOrProperty("tokenType")
            .hasFieldOrProperty("accessToken")
            .hasFieldOrProperty("expiresIn")
            .hasFieldOrProperty("refreshToken");

        accessToken = token.accessToken();

        return token;
    }
}
