package com.vladimirkolarevic.releasetracker.domain.exception;

public class ReleaseTrackerException extends RuntimeException {


    public ReleaseTrackerException(String message) {
        super(message);
    }

    public ReleaseTrackerException(String message, Throwable cause) {
        super(message, cause);
    }

    public ReleaseTrackerException(Throwable cause) {
        super(cause);
    }


}
