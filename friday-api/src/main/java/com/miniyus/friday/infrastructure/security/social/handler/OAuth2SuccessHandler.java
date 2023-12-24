package com.miniyus.friday.infrastructure.security.social.handler;

import com.miniyus.friday.infrastructure.security.AuthResponseHandler;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.miniyus.friday.infrastructure.jwt.IssueToken;
import com.miniyus.friday.infrastructure.jwt.JwtService;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/08/31
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final AuthResponseHandler responseHandler;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
        log.debug("OAuth2 Login 성공!");

        if (response.isCommitted()) {
            log.debug("Response has already been committed.");
            return;
        }

        PrincipalUserInfo oAuth2User = (PrincipalUserInfo) authentication.getPrincipal();

        issueToken(response, oAuth2User);
    }

    private void issueToken(HttpServletResponse response, PrincipalUserInfo oAuth2User) {
        IssueToken issueToken = jwtService.issueToken(oAuth2User.getId());

        responseHandler.handleOAuth2IssueTokenHeader(
            response,
            issueToken);
    }
}
