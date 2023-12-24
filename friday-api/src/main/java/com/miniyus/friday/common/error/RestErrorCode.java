package com.miniyus.friday.common.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * ErrorCode common error codes
 *
 * @author miniyus
 * @since 2023/09/01
 */
@Getter
public enum RestErrorCode implements ErrorCode {
    ACCESS_DENIED(1, 401, "error.accessDenied"), // access denied
    INSUFFICIENT_SCOPE(2, 403, "error.insufficientScope"), // insufficient scope
    INVALID_CLIENT(3, 400, "error.invalidClient"), // invalid client
    INVALID_GRANT(4, 400, "error.invalidGrant"), // invalid grant
    INVALID_REDIRECT_URI(5, 400, "error.invalidRedirectUri"), // invalid redirect uri
    INVALID_REQUEST(6, 400, "error.invalidRequest"), // invalid request
    INVALID_SCOPE(7, 400, "error.invalidScope"), // invalid scope
    INVALID_TOKEN(8, 400, "error.invalidToken"), // invalid token
    AUTH_SERVER_ERROR(9, 500, "error.authServerError"), // server error
    TEMPORARILY_UNAVAILABLE(10, 503, "error.temporarilyUnavailable"), // temporary unavailable
    UNAUTHORIZED_CLIENT(11, 401, "error.unauthorizedClient"), // unauthorized client
    UNSUPPORTED_GRANT_TYPE(12, 400, "error.unsupportedGrantType"), // unsupported grant type
    UNSUPPORTED_RESPONSE_TYPE(13, 400,
        "error.unsupportedResponseType"), // unsupported response type
    UNSUPPORTED_TOKEN_TYPE(14, 400, "error.unsupportedTokenType"), // unsupported token type
    BAD_REQUEST(15, 400, "error.badRequest"), // bad request
    FORBIDDEN(16, 403, "error.forbidden"), // forbidden
    NOT_FOUND(17, 404, "error.notFound"), // not found
    METHOD_NOT_ALLOWED(18, 405, "error.methodNotAllowed"), // method not allowed
    CONFLICT(19, 409, "error.conflict"), // conflict
    TOO_MANY_REQUESTS(20, 429, "error.tooManyRequests"), // too many requests
    INTERNAL_SERVER_ERROR(21, 500, "error.internalServerError"), // internal server error
    QUERY_ERROR(22, 500, "error.queryError"), // query error
    SERVER_UNAVAILABLE(23, 503, "error.serverUnavailable"), // server unavailable
    UPLOAD_ERROR(24, 500, "error.uploadError"), // upload error
    UNSUPPORTED_MEDIA_TYPE(25, 415, "error.unsupportedMediaType"), // unsupported media type
    NOT_ACCEPTABLE(26, 406, "error.notAcceptable"), // not acceptable
    MISSING_PATH_VARIABLE(27, 400, "error.missingPathVariable"), // missing path variable
    MISSING_SERVLET_REQUEST_PARAMETER(28, 400,
        "error.missingServletRequestParameter"), // missing servlet
    MISSING_SERVLET_REQUEST_PART(29, 400, "error.missingServletRequestPart"), // missing servlet
    MISSING_SERVLET_REQUEST_BINDING(30, 400,
        "error.missingServletRequestBinding"), // missing servlet
    ASYNC_REQUEST_TIMEOUT(31, 503, "error.asyncRequestTimeout"); // async request timeout

    /**
     * -- GETTER -- Retrieves the code value.
     */
    private final int errorCode;

    /**
     * -- GETTER -- A description of the entire Java function.
     */
    private final int statusCode;

    /**
     * default error message.
     */
    private final String message;

    RestErrorCode(int code, int status, String message) {
        this.errorCode = code;
        this.statusCode = status;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.valueOf(statusCode);
    }

    /**
     * A function to retrieve an ErrorCode object based on a given code.
     *
     * @param code the code to match against the ErrorCode objects
     * @return the ErrorCode object matching the given code, or null if no match is found
     */
    public static RestErrorCode fromCode(int code) {
        for (RestErrorCode e : RestErrorCode.values()) {
            if (e.getErrorCode() == code) {
                return e;
            }
        }
        return null;
    }

    /**
     * Retrieves the title of the object.
     *
     * @return the title of the object
     */
    public String getTitle() {
        return this.name();
    }

    /**
     * Retrieves the description of the entire Java function.
     *
     * @return the reason phrase of the HTTP status
     */
    public String getDescription() {
        return getHttpStatus().getReasonPhrase();
    }

    /**
     * Retrieves the message associated with this object.
     *
     * @return The message.
     */
    @Override
    public String getMessage() {
        return message;
    }
}
