package com.meteormin.friday.infrastructure.security.auth.handler;

import com.meteormin.friday.infrastructure.jwt.IssueToken;
import com.meteormin.friday.infrastructure.jwt.JwtService;
import com.meteormin.friday.infrastructure.security.AuthResponseHandler;
import com.meteormin.friday.infrastructure.security.PrincipalUserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

/**
 * Login Success Handler
 *
 * @author meteormin
 * @since 2023/09/04
 */
@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    /**
     * jwtService
     */
    private final JwtService jwtService;

    /**
     * authResponseHandler
     */
    private final AuthResponseHandler responseHandler;

    /**
     * Handles the authentication success event.
     *
     * @param request        the HTTP request
     * @param response       the HTTP response
     * @param authentication the authentication object
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void onAuthenticationSuccess(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication) throws IOException {

        var userDetails = (PrincipalUserInfo) authentication.getPrincipal();

        IssueToken tokens = jwtService.issueToken(userDetails.getId());

        log.debug("Success Login Email: {}", userDetails.getUsername());
        log.debug("Success Login AccessToken : {}", tokens.accessToken());
        log.debug("Issued AccessToken expires in: {}(seconds)", tokens.expiresIn());

        responseHandler.handlePasswordIssueTokenResponse(
            request,
            response,
            userDetails,
            tokens);
    }
}
