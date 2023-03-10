package com.vladimirkolarevic.releasetracker.rest.exception;

import com.vladimirkolarevic.releasetracker.domain.exception.NonExistentReleaseException;
import com.vladimirkolarevic.releasetracker.domain.exception.ReleaseTrackerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice(annotations = RestController.class)
public class RestApiExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestApiExceptionHandler.class);

    @ExceptionHandler(NonExistentReleaseException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public RestApiError handleNotFoundException(NonExistentReleaseException exception, WebRequest webRequest) {
        LOGGER.warn("Not Found error, for web request : {}", webRequest.getDescription(false));
        return new RestApiError(exception.getMessage());
    }

    @ExceptionHandler(ReleaseTrackerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public RestApiError handleBadRequestException(ReleaseTrackerException exception, WebRequest webRequest) {
        LOGGER.warn("Bad request, for web request : {}", webRequest.getDescription(false));
        return new RestApiError(exception.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public RestApiError handleBadRequestException(Exception exception, WebRequest webRequest) {
        LOGGER.error("Internal server error, for web request : {} and exception : {}", webRequest.getDescription(false),
            exception);
        return new RestApiError(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public RestApiError handleValidationExceptions(MethodArgumentNotValidException exception, WebRequest webRequest) {
        LOGGER.warn("Bad request, for web request : {}", webRequest.getDescription(false));
        return new RestApiError(exception.getMessage());
    }
}
