package com.meteormin.friday.common.error;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * ErrorCode interface
 */
public interface ErrorCode extends Serializable {
    /**
     * Get integer error code.
     *
     * @return returns error code.
     */
    int getErrorCode();

    /**
     * Get http status object of error.
     * <p>HttpStatus object is spring framework's enum class</p>
     * <br>
     * <p><a
     * href="https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/http/HttpStatus.html">reference
     * spring api.</a></p>
     *
     * @return returns HttpStatus Object of error.
     * @see HttpStatus
     */
    HttpStatus getHttpStatus();

    /**
     * Get the status code.
     *
     * @return the status code.
     */
    int getStatusCode();

    /**
     * Retrieves the name of the object.
     *
     * @return the name of the object
     */
    String name();

    /**
     * Retrieves the title of the object.
     * <p>get error code title</p>
     *
     * @return the title of the object
     */
    String getTitle();

    /**
     * Retrieves the description of the object.
     * <p>get error code description</p>
     *
     * @return description of return value
     */
    String getDescription();

    /**
     * Retrieves the error message.
     *
     * @return the message as a string
     */
    String getMessage();
}
