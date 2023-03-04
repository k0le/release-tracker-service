package com.vladimirkolarevic.releasetracker.domain.exception;

public class ReleaseTrackerException extends RuntimeException {


    public ReleaseTrackerException(final String message) {
        super(message);
    }

    public ReleaseTrackerException(final String message,
                                   final Throwable cause) {
        super(message, cause);
    }

    public ReleaseTrackerException(final Throwable cause) {
        super(cause);
    }


}
