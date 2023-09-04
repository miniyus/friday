package com.miniyus.friday.infrastructure.advice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.miniyus.friday.common.error.ErrorCode;
import com.miniyus.friday.common.error.RestErrorException;
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
     * @param args        the arguments to be included in the translated message
     * @return the translated message or the message code if the translation is not
     *         found
     */
    private final String translateMessage(String messageCode, Object... args) {
        return messageSource.getMessage(messageCode, args, messageCode, LocaleContextHolder.getLocale());
    }

    /**
     * Translates the given message code into a localized message.
     *
     * @param messageCode the code of the message to be translated
     * @return the translated message
     */
    private final String translateMessage(String messageCode) {
        return messageSource.getMessage(messageCode, null, messageCode, LocaleContextHolder.getLocale());
    }

    /**
     * Handles the exception of type Exception and returns a ResponseEntity
     * 
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorResponse> handleFallbackException(Exception ex, WebRequest request) {
        var errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        log.error(ex.toString());
        ex.printStackTrace();
        ErrorResponse errorDetails = new ErrorResponse(
                LocalDateTime.now(),
                errorCode.name(),
                ex.getMessage());
        return new ResponseEntity<ErrorResponse>(errorDetails, errorCode.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errorCode = ErrorCode.NOT_FOUND;
        log.error(ex.toString());
        ErrorResponse errorDetails = new ErrorResponse(
                LocalDateTime.now(),
                errorCode.name(),
                ex.getMessage());
        return new ResponseEntity<Object>(errorDetails, errorCode.getHttpStatus());
    }

    /**
     * Handles the exception of type CommonErrorException and returns a
     * ResponseEntity
     * with an ErrorResponse object containing the error details.
     *
     * @param ex      the CommonErrorException object representing the exception
     * @param request the WebRequest object representing the HTTP request
     * @return a ResponseEntity object containing the error details
     */
    @ExceptionHandler(RestErrorException.class)
    public final ResponseEntity<ErrorResponse> handleApiException(RestErrorException ex, WebRequest request) {
        var errorCode = ex.getErrorCode();
        log.debug(ex.toString());
        ErrorResponse errorDetails = new ErrorResponse(
                LocalDateTime.now(),
                errorCode.name(),
                ex.getMessage());

        return new ResponseEntity<ErrorResponse>(errorDetails, errorCode.getHttpStatus());
    }

    /**
     * Handles a MethodArgumentNotValidException for validating method arguments.
     *
     * @param ex      the MethodArgumentNotValidException object
     * @param headers the HttpHeaders object
     * @param status  the HttpStatusCode object
     * @param request the WebRequest object
     * @return the ResponseEntity<Object> object
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
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
                LocalDateTime.now(),
                ErrorCode.BAD_REQUEST.name(),
                "Validation Failed",
                details);
        return new ResponseEntity<Object>(errorDetails, HttpStatus.BAD_REQUEST);
    }
}