package com.miniyus.friday.infrastructure.advice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniyus.friday.common.error.ErrorResponse;
import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;
import com.miniyus.friday.infrastructure.security.social.exception.NotSupportProviderException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Exception Handler
 *
 * @author seongminyoo
 * @since 2023/09/01
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * message source: translator for i18n
     */
    private final MessageSource messageSource;

    /**
     * object mapper: for serializing/deserializing
     */
    private final ObjectMapper objectMapper;

    /**
     * A handler method for handling HTTP request methods not supported.
     *
     * @param ex      the exception that was thrown
     * @param headers the HTTP headers of the request
     * @param status  the HTTP status code to be returned
     * @param request the web request object
     * @return the response entity containing the error details
     */
    @Override
    protected final ResponseEntity<Object> handleHttpRequestMethodNotSupported(
        @NonNull HttpRequestMethodNotSupportedException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        report(ex, ((ServletWebRequest) request).getRequest());

        var errorCode = RestErrorCode.METHOD_NOT_ALLOWED;
        ErrorResponse errorDetails = new ErrorResponse(
            errorCode,
            translateMessage(errorCode.getMessage(), ex.getLocalizedMessage()));

        return response(errorDetails, headers, errorCode.getHttpStatus());
    }

    /**
     * Handles the HttpMediaTypeNotAcceptableException by reporting the exception, generating the
     * error response, and returning it as a ResponseEntity.
     *
     * @param ex      the HttpMediaTypeNotAcceptableException that was thrown
     * @param headers the HttpHeaders of the request
     * @param status  the HttpStatusCode to be returned
     * @param request the WebRequest received
     * @return the ResponseEntity containing the error response
     */
    @Override
    protected final ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
        @NonNull HttpMediaTypeNotAcceptableException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        report(ex, ((ServletWebRequest) request).getRequest());

        var errorCode = RestErrorCode.NOT_ACCEPTABLE;
        ErrorResponse errorDetails = new ErrorResponse(
            errorCode,
            translateMessage(errorCode.getMessage(), ex.getLocalizedMessage()));

        return response(errorDetails, headers, errorCode.getHttpStatus());
    }

    /**
     * Handles the HttpMediaTypeNotSupportedException and returns a ResponseEntity object.
     *
     * @param ex      the HttpMediaTypeNotSupportedException object
     * @param headers the HttpHeaders object
     * @param status  the HttpStatusCode object
     * @param request the WebRequest object
     * @return a ResponseEntity object
     */
    @Override
    protected final ResponseEntity<Object> handleHttpMediaTypeNotSupported(
        @NonNull HttpMediaTypeNotSupportedException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        report(ex, ((ServletWebRequest) request).getRequest());

        var errorCode = RestErrorCode.UNSUPPORTED_MEDIA_TYPE;
        ErrorResponse errorDetails = new ErrorResponse(
            errorCode,
            translateMessage(errorCode.getMessage(), ex.getLocalizedMessage()));
        return response(errorDetails, headers, errorCode.getHttpStatus());
    }

    /**
     * A description of the entire Java function.
     *
     * @param ex      description of parameter
     * @param headers description of parameter
     * @param status  description of parameter
     * @param request description of parameter
     * @return description of return value
     */
    @Override
    protected final ResponseEntity<Object> handleMissingPathVariable(
        @NonNull MissingPathVariableException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        report(ex, ((ServletWebRequest) request).getRequest());
        var errorCode = RestErrorCode.MISSING_PATH_VARIABLE;
        ErrorResponse errorDetails = new ErrorResponse(
            errorCode,
            translateMessage(errorCode.getMessage(), ex.getLocalizedMessage()));
        return response(errorDetails, headers, errorCode.getHttpStatus());

    }

    /**
     * Overrides the handleMissingServletRequestParameter method from the parent class. Handles the
     * case when a required request parameter is missing.
     *
     * @param ex      the MissingServletRequestParameterException that was thrown
     * @param headers the HTTP headers of the response
     * @param status  the HTTP status code of the response
     * @param request the WebRequest object representing the incoming request
     * @return the ResponseEntity object containing the error details
     */
    @Override
    protected final ResponseEntity<Object> handleMissingServletRequestParameter(
        @NonNull MissingServletRequestParameterException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        report(ex, ((ServletWebRequest) request).getRequest());
        var errorCode = RestErrorCode.MISSING_SERVLET_REQUEST_PARAMETER;
        ErrorResponse errorDetails = new ErrorResponse(
            errorCode,
            translateMessage(errorCode.getMessage(), ex.getLocalizedMessage()));
        return response(errorDetails, headers, errorCode.getHttpStatus());
    }

    /**
     * Handles the case when a required servlet request part is missing.
     *
     * @param ex      the MissingServletRequestPartException that was thrown
     * @param headers the HttpHeaders of the request
     * @param status  the HttpStatusCode of the response
     * @param request the WebRequest object representing the request
     * @return a ResponseEntity containing the error details
     */
    @Override
    protected final ResponseEntity<Object> handleMissingServletRequestPart(
        @NonNull MissingServletRequestPartException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        report(ex, ((ServletWebRequest) request).getRequest());
        var errorCode = RestErrorCode.MISSING_SERVLET_REQUEST_PART;
        ErrorResponse errorDetails = new ErrorResponse(
            errorCode,
            translateMessage(errorCode.getMessage(), ex.getLocalizedMessage()));
        return response(errorDetails, headers, errorCode.getHttpStatus());
    }

    /**
     * Handles the ServletRequestBindingException and returns a ResponseEntity.
     *
     * @param ex      the ServletRequestBindingException that occurred
     * @param headers the HttpHeaders to be included in the ResponseEntity
     * @param status  the HttpStatusCode to be included in the ResponseEntity
     * @param request the WebRequest object
     * @return the ResponseEntity containing the error details
     */
    @Override
    protected final ResponseEntity<Object> handleServletRequestBindingException(
        @NonNull ServletRequestBindingException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request
    ) {
        report(ex, ((ServletWebRequest) request).getRequest());
        var errorCode = RestErrorCode.MISSING_SERVLET_REQUEST_BINDING;
        ErrorResponse errorDetails = new ErrorResponse(
            errorCode,
            translateMessage(errorCode.getMessage(), ex.getLocalizedMessage()));
        return response(errorDetails, headers, errorCode.getHttpStatus());
    }

    /**
     * Handles the NoHandlerFoundException and returns a ResponseEntity containing error details.
     *
     * @param ex      the NoHandlerFoundException
     * @param headers the HttpHeaders
     * @param status  the HttpStatusCode
     * @param request the WebRequest
     * @return the ResponseEntity containing error details
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
        @NonNull NoHandlerFoundException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        report(ex, ((ServletWebRequest) request).getRequest());

        var errorCode = RestErrorCode.NOT_FOUND;

        ErrorResponse errorDetails = new ErrorResponse(
            errorCode,
            translateMessage(errorCode.getMessage(), ex.getDetailMessageCode()));

        return response(errorDetails, headers, errorCode.getHttpStatus());
    }

    /**
     * Handles a MethodArgumentNotValidException for validating method arguments.
     *
     * @param ex      the MethodArgumentNotValidException object
     * @param headers the HttpHeaders object
     * @param status  the HttpStatusCode object
     * @param request the WebRequest object
     * @return the {@code ResponseEntity<Object> object}
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        @NonNull MethodArgumentNotValidException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        report(ex, ((ServletWebRequest) request).getRequest());
        RestErrorCode errorCode = RestErrorCode.BAD_REQUEST;
        HashMap<String, List<String>> details = new HashMap<>();

        String messageSummary = null;
        List<String> errorMessages = new ArrayList<>();
        for (FieldError error : ex.getFieldErrors()) {
            log.debug(error.toString());

            if (!details.containsKey(error.getField())) {
                errorMessages = new ArrayList<>();
            }

            var errorMessage = error.getDefaultMessage();
            var message = translateMessage(errorMessage);

            if (messageSummary == null) {
                messageSummary = message;
            }

            errorMessages.add(message);
            details.put(error.getField(), errorMessages);
        }

        if (messageSummary == null) {
            messageSummary = translateMessage(errorCode.getMessage());
        }

        ErrorResponse errorDetails = new ErrorResponse(
            errorCode,
            messageSummary,
            details);
        return response(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles an AsyncRequestTimeoutException and returns a ResponseEntity.
     *
     * @param ex      the AsyncRequestTimeoutException to handle
     * @param headers the HttpHeaders of the response
     * @param status  the HttpStatusCode of the response
     * @param request the WebRequest of the request
     * @return the {@code ResponseEntity<Object>} containing the error details
     */
    @Override
    protected final ResponseEntity<Object> handleAsyncRequestTimeoutException(
        @NonNull AsyncRequestTimeoutException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        report(ex, ((ServletWebRequest) request).getRequest());
        var errorCode = RestErrorCode.ASYNC_REQUEST_TIMEOUT;
        ErrorResponse errorDetails = new ErrorResponse(
            errorCode,
            translateMessage("error.asyncRequestTimeout", ex.getLocalizedMessage()));
        return response(errorDetails, headers, errorCode.getHttpStatus());
    }

    /**
     * Overrides the handleErrorResponseException method to handle the given
     * ErrorResponseException.
     *
     * @param ex      the ErrorResponseException to handle
     * @param headers the HttpHeaders of the response
     * @param status  the HttpStatusCode of the response
     * @param request the WebRequest object representing the incoming request
     * @return the ResponseEntity containing the error details and the appropriate HTTP status code
     */
    @Override
    protected final ResponseEntity<Object> handleErrorResponseException(
        @NonNull ErrorResponseException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request
    ) {
        report(ex, ((ServletWebRequest) request).getRequest());
        var errorCode = RestErrorCode.INTERNAL_SERVER_ERROR;
        var body = ex.updateAndGetBody(this.messageSource, LocaleContextHolder.getLocale());
        ErrorResponse errorDetails = new ErrorResponse(
            errorCode,
            body.getDetail()
        );
        return response(errorDetails, headers, errorCode.getHttpStatus());
    }

    /**
     * Handles the exception when conversion is not supported.
     *
     * @param ex      the ConversionNotSupportedException object
     * @param headers the HttpHeaders object
     * @param status  the HttpStatusCode object
     * @param request the WebRequest object
     * @return the ResponseEntity object
     */
    @Override
    protected final ResponseEntity<Object> handleConversionNotSupported(
        @NonNull ConversionNotSupportedException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        report(ex, ((ServletWebRequest) request).getRequest());
        var errorCode = RestErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResponse errorDetails = new ErrorResponse(
            errorCode,
            translateMessage("error.conversionNotSupported", ex.getLocalizedMessage()));
        return response(errorDetails, headers, errorCode.getHttpStatus());
    }

    /**
     * Handles a type mismatch exception and returns a ResponseEntity object.
     *
     * @param ex      the TypeMismatchException object
     * @param headers the HttpHeaders object
     * @param status  the HttpStatusCode object
     * @param request the WebRequest object
     * @return a ResponseEntity object
     */
    @Override
    protected final ResponseEntity<Object> handleTypeMismatch(
        @NonNull TypeMismatchException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        report(ex, ((ServletWebRequest) request).getRequest());
        var errorCode = RestErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResponse errorDetails = new ErrorResponse(
            errorCode,
            translateMessage("error.typeMismatch", ex.getLocalizedMessage()));
        return response(errorDetails, headers, errorCode.getHttpStatus());
    }

    /**
     * Handles the HttpMessageNotWritableException and returns a ResponseEntity with the error
     * details.
     *
     * @param ex      the HttpMessageNotWritableException to handle
     * @param headers the HttpHeaders of the response
     * @param status  the HttpStatusCode of the response
     * @param request the WebRequest containing the request information
     * @return the ResponseEntity with the error details
     */
    @Override
    protected final ResponseEntity<Object> handleHttpMessageNotWritable(
        @NonNull HttpMessageNotWritableException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        report(ex, ((ServletWebRequest) request).getRequest());
        var errorCode = RestErrorCode.INTERNAL_SERVER_ERROR;
        ErrorResponse errorDetails = new ErrorResponse(
            errorCode,
            translateMessage("error.httpMessageNotWritable", ex.getLocalizedMessage()));
        return response(errorDetails, headers, errorCode.getHttpStatus());
    }


    /**
     * Handles the case when an HTTP message is not readable.
     *
     * @param ex      the HttpMessageNotReadableException that occurred
     * @param headers the HttpHeaders of the request
     * @param status  the HttpStatusCode of the request
     * @param request the WebRequest object
     * @return the {@code ResponseEntity<Object>} containing the error details
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
        @NonNull HttpMessageNotReadableException ex,
        @NonNull HttpHeaders headers,
        @NonNull HttpStatusCode status,
        @NonNull WebRequest request) {
        report(ex, ((ServletWebRequest) request).getRequest());
        var errorCode = RestErrorCode.BAD_REQUEST;
        ErrorResponse errorDetails = new ErrorResponse(
            errorCode,
            translateMessage("error.httpMessageNotReadable", ex.getLocalizedMessage()));
        return response(errorDetails, headers, errorCode.getHttpStatus());
    }

    /**
     * Handles the exception of type Exception and returns a ResponseEntity
     *
     * @param ex      exception
     * @param request request
     * @return response entity
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleFallbackException(
        Exception ex,
        WebRequest request) {
        report(ex, ((ServletWebRequest) request).getRequest());

        var errorCode = RestErrorCode.INTERNAL_SERVER_ERROR;

        ErrorResponse errorDetails = new ErrorResponse(
            errorCode,
            translateMessage(ex.getLocalizedMessage()));
        return response(errorDetails, errorCode.getHttpStatus());
    }

    /**
     * Handles the exception of type CommonErrorException and returns a ResponseEntity with an
     * ErrorResponse object containing the error details.
     *
     * @param ex      the CommonErrorException object representing the exception
     * @param request the WebRequest object representing the HTTP request
     * @return a ResponseEntity object containing the error details
     */
    @ExceptionHandler(RestErrorException.class)
    protected final ResponseEntity<Object> handleApiException(
        RestErrorException ex,
        WebRequest request) {
        report(ex, ((ServletWebRequest) request).getRequest());

        var errorDetails = newErrorResponse(ex);

        return response(errorDetails, ex.getErrorCode().getHttpStatus());
    }

    /**
     * Handles the NotSupportProviderException and returns an ErrorResponse object.
     *
     * @param ex      the NotSupportProviderException that occurred
     * @param request the WebRequest object
     * @return the ResponseEntity containing the ErrorResponse object
     */
    @ExceptionHandler(NotSupportProviderException.class)
    protected final ResponseEntity<Object> handleNotSupportProviderException(
        NotSupportProviderException ex,
        WebRequest request) {
        report(ex, ((ServletWebRequest) request).getRequest());

        var errorCode = RestErrorCode.AUTH_SERVER_ERROR;

        ErrorResponse errorDetails = new ErrorResponse(
            errorCode,
            translateMessage("error.notSupportProvider", ex.getLocalizedMessage()));

        return response(errorDetails, errorCode.getHttpStatus());
    }

    /**
     * Handles the AccessDeniedException and returns an ErrorResponse.
     *
     * @param ex      the AccessDeniedException that occurred
     * @param request the WebRequest object
     * @return the ResponseEntity containing the ErrorResponse
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected final ResponseEntity<Object> handleAccessDeniedException(
        AccessDeniedException ex,
        WebRequest request) {
        report(ex, ((ServletWebRequest) request).getRequest());

        var errorCode = RestErrorCode.ACCESS_DENIED;

        ErrorResponse errorDetails = new ErrorResponse(
            errorCode,
            translateMessage("error.accessDenied"));

        return response(errorDetails, errorCode.getHttpStatus());
    }

    /**
     * Handles the exception when an entity is not found.
     *
     * @param ex      the EntityNotFoundException instance
     * @param request the WebRequest instance
     * @return the ResponseEntity containing the error response
     */
    @ExceptionHandler(EntityNotFoundException.class)
    protected final ResponseEntity<Object> handleEntityNotFound(
        EntityNotFoundException ex,
        WebRequest request) {
        report(ex, ((ServletWebRequest) request).getRequest());

        var errorCode = RestErrorCode.NOT_FOUND;
        ErrorResponse errorDetails = new ErrorResponse(
            errorCode,
            translateMessage("error.notFound", ex.getLocalizedMessage()));

        return response(errorDetails, errorCode.getHttpStatus());
    }

    /**
     * Handles the UsernameNotFoundException exception and returns a ResponseEntity with an
     * ErrorResponse object.
     *
     * @param ex      the UsernameNotFoundException that was thrown
     * @param request the WebRequest object representing the current request
     * @return a ResponseEntity containing an ErrorResponse object and the corresponding HttpStatus
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    protected final ResponseEntity<Object> handleUsernameNotFound(
        UsernameNotFoundException ex,
        WebRequest request) {
        report(ex, ((ServletWebRequest) request).getRequest());

        var errorCode = RestErrorCode.NOT_FOUND;
        ErrorResponse errorDetails = new ErrorResponse(
            errorCode,
            translateMessage("error.usernameNotFound", ex.getLocalizedMessage()));

        return response(errorDetails, errorCode.getHttpStatus());
    }

    /**
     * Translates the given message code into a localized message.
     *
     * @param messageCode the code of the message to be translated
     * @return the translated message
     */
    private String translateMessage(String messageCode, String defaultMessage) {
        return messageSource.getMessage(
            messageCode,
            null,
            defaultMessage,
            LocaleContextHolder.getLocale());
    }

    /**
     * Translates the given message code into a localized message.
     *
     * @param messageCode the code of the message to be translated
     * @return the translated message
     */
    private String translateMessage(String messageCode) {
        return messageSource.getMessage(
            messageCode,
            null,
            messageCode,
            LocaleContextHolder.getLocale());
    }

    /**
     * Creates a new ErrorResponse object based on the given RestErrorException.
     *
     * @param ex the RestErrorException to create the ErrorResponse from
     * @return the newly created ErrorResponse object
     */
    private ErrorResponse newErrorResponse(RestErrorException ex) {
        return ex.getBody(messageSource, LocaleContextHolder.getLocale());
    }

    /**
     * Creates a response entity with the given error response, headers, and status.
     *
     * @param errorResponse the error response object (nullable)
     * @param status        the HTTP status code for the response (non-null)
     * @return the response entity object
     */
    private ResponseEntity<Object> response(
        @Nullable ErrorResponse errorResponse,
        @NonNull HttpStatus status) {
        return response(errorResponse, null, status);
    }

    /**
     * Creates a response entity with the given error response, headers, and status.
     *
     * @param errorResponse the error response object (nullable)
     * @param headers       the headers for the response (nullable)
     * @param status        the HTTP status code for the response (non-null)
     * @return the response entity object
     */
    private ResponseEntity<Object> response(
        @Nullable ErrorResponse errorResponse,
        @Nullable HttpHeaders headers,
        @NonNull HttpStatus status) {
        try {
            log.error("Error Response: {}", objectMapper.writeValueAsString(errorResponse));
        } catch (IOException e) {
            log.error("Failed to write error response {}", e.getMessage());
        }

        return new ResponseEntity<>(
            errorResponse,
            headers,
            status
        );
    }

    /**
     * Reports an exception and logs relevant information from the HttpServletRequest.
     *
     * @param ex      the exception to be reported
     * @param request the HttpServletRequest object containing the request information
     */
    private void report(Exception ex, HttpServletRequest request) {
        log.error("[Report Error] RequestId \"{}\"", request.getRequestId());
        log.error("error message \"{}\"", ex.getLocalizedMessage());
        log.error("{} {}", request.getMethod(), request.getRequestURI());
        log.error("ContentType {}, ContentLength {}", request.getContentType(),
            request.getContentLength());
        log.error("RemoteAddr {}", request.getRemoteAddr());
        log.error("Locale {}", request.getLocale().getDisplayName());
        log.error("UserAgent {}", request.getHeader("User-Agent"));
        log.error("Query {}", request.getQueryString());

        try {
            log.error("Params {}", objectMapper.writeValueAsString(
                request.getParameterMap()));
            log.error("Body {}", IOUtils.toString(request.getReader()));
        } catch (IOException e) {
            log.error("Failed to read request {}", e.getMessage());
        }
        log.error(ex.getMessage(), ex);
    }
}
