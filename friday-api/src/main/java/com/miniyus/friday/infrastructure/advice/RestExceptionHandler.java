package com.miniyus.friday.infrastructure.advice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.miniyus.friday.common.error.AuthErrorCode;
import com.miniyus.friday.common.error.AuthErrorException;
import com.miniyus.friday.common.error.ErrorCode;
import com.miniyus.friday.common.error.RestErrorException;
import com.miniyus.friday.infrastructure.security.oauth2.exception.NotSupportProviderException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Exception Handler
 *
 * @author miniyus
 * @date 2023/09/01
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * message source: translator for i18n
     */
    private final MessageSource messageSource;

    /**
     * Translates a message using the provided message code and arguments.
     *
     * @param messageCode the code of the message to be translated
     * @param args the arguments to be included in the translated message
     * @return the translated message or the message code if the translation is not found
     */
    private final String translateMessage(
            String messageCode,
            String defaultMessage,
            Object... args) {
        return messageSource.getMessage(
                messageCode,
                args,
                defaultMessage,
                LocaleContextHolder.getLocale());
    }

    /**
     * Translates the given message code into a localized message.
     *
     * @param messageCode the code of the message to be translated
     * @return the translated message
     */
    private final String translateMessage(String messageCode, String defaultMessage) {
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
    private final String translateMessage(String messageCode) {
        return messageSource.getMessage(
                messageCode,
                null,
                messageCode,
                LocaleContextHolder.getLocale());
    }


    /**
     * Handles the NoHandlerFoundException and returns a ResponseEntity containing error details.
     *
     * @param ex the NoHandlerFoundException
     * @param headers the HttpHeaders
     * @param status the HttpStatusCode
     * @param request the WebRequest
     * @return the ResponseEntity containing error details
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status,
            WebRequest request) {
        var errorCode = ErrorCode.NOT_FOUND;
        log.debug(ex.toString());

        ErrorResponse errorDetails = new ErrorResponse(
                errorCode,
                translateMessage("exception.notFound", ex.getDetailMessageCode()));
        return new ResponseEntity<Object>(errorDetails, errorCode.getHttpStatus());
    }

    /**
     * Handles a MethodArgumentNotValidException for validating method arguments.
     *
     * @param ex the MethodArgumentNotValidException object
     * @param headers the HttpHeaders object
     * @param status the HttpStatusCode object
     * @param request the WebRequest object
     * @return the ResponseEntity<Object> object
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HashMap<String, List<String>> details = new HashMap<>();

        List<String> errorMessages = new ArrayList<>();
        for (FieldError error : ex.getFieldErrors()) {
            log.debug(error.toString());

            if (!details.containsKey(error.getField())) {
                errorMessages = new ArrayList<>();
            }

            var errorMessage = error.getDefaultMessage();
            var message = translateMessage(errorMessage);

            errorMessages.add(message);
            details.put(error.getField(), errorMessages);
        }

        ErrorResponse errorDetails = new ErrorResponse(
                ErrorCode.BAD_REQUEST,
                translateMessage("exception.validationFail"),
                details);
        return new ResponseEntity<Object>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles the exception of type Exception and returns a ResponseEntity
     * 
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleFallbackException(Exception ex,
            WebRequest request) {
        var errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

        log.error(ex.getMessage());
        log.debug(ex.toString());

        ErrorResponse errorDetails = new ErrorResponse(
                errorCode,
                translateMessage(ex.getLocalizedMessage()));
        return new ResponseEntity<ErrorResponse>(errorDetails, errorCode.getHttpStatus());
    }

    /**
     * Handles the exception of type CommonErrorException and returns a ResponseEntity with an
     * ErrorResponse object containing the error details.
     *
     * @param ex the CommonErrorException object representing the exception
     * @param request the WebRequest object representing the HTTP request
     * @return a ResponseEntity object containing the error details
     */
    @ExceptionHandler(RestErrorException.class)
    protected final ResponseEntity<ErrorResponse> handleApiException(
            RestErrorException ex,
            WebRequest request) {
        var errorCode = ex.getErrorCode();

        log.error(ex.toString());

        var message = translateMessage(
                ex.getMessage(),
                ex.getLocalizedMessage(),
                ex.getArgs());

        ErrorResponse errorDetails = new ErrorResponse(
                errorCode,
                message);

        return new ResponseEntity<ErrorResponse>(errorDetails, errorCode.getHttpStatus());
    }

    @ExceptionHandler(AuthErrorException.class)
    protected final ResponseEntity<ErrorResponse> handleAuthException(
            AuthErrorException ex,
            WebRequest request) {
        var errorCode = ex.getErrorCode();

        log.error(ex.toString());

        var message = translateMessage(
                ex.getMessage(),
                ex.getLocalizedMessage(),
                ex.getArgs());

        ErrorResponse errorDetails = new ErrorResponse(
                errorCode,
                message);

        return new ResponseEntity<ErrorResponse>(errorDetails, errorCode.getHttpStatus());
    }

    /**
     * Handles the NotSupportProviderException and returns an ErrorResponse object.
     *
     * @param ex the NotSupportProviderException that occurred
     * @param request the WebRequest object
     * @return the ResponseEntity containing the ErrorResponse object
     */
    @ExceptionHandler(NotSupportProviderException.class)
    protected final ResponseEntity<ErrorResponse> handleNotSupportProviderException(
            NotSupportProviderException ex,
            WebRequest request) {
        var errorCode = AuthErrorCode.SERVER_ERROR;

        log.error(ex.toString());

        ErrorResponse errorDetails = new ErrorResponse(
                errorCode,
                translateMessage("exception.notSupportProvider", ex.getLocalizedMessage()));

        return new ResponseEntity<ErrorResponse>(errorDetails, errorCode.getHttpStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    protected final ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        var errorCode = AuthErrorCode.ACCESS_DENIED;

        ErrorResponse errorDetails = new ErrorResponse(
                errorCode,
                translateMessage("exception.accessDenied"));

        return new ResponseEntity<ErrorResponse>(errorDetails, errorCode.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        var errorCode = ErrorCode.METHOD_NOT_ALLOWED;
        ErrorResponse errorDetails = new ErrorResponse(
                errorCode,
                translateMessage("exception.methodNotAllowed", ex.getLocalizedMessage()));

        return new ResponseEntity<Object>(errorDetails, errorCode.getHttpStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleEntityNotFound(
            EntityNotFoundException ex,
            WebRequest request) {
        var errorCode = ErrorCode.NOT_FOUND;
        ErrorResponse errorDetails = new ErrorResponse(
                errorCode,
                translateMessage("exception.notFound", ex.getLocalizedMessage()));

        return new ResponseEntity<ErrorResponse>(errorDetails, errorCode.getHttpStatus());
    }
}
