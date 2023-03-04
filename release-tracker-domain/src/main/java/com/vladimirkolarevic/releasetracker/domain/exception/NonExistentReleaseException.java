package com.vladimirkolarevic.releasetracker.domain.exception;

public class NonExistentReleaseException extends ReleaseTrackerException {


    public NonExistentReleaseException(final String message) {
        super(message);
    }

    public NonExistentReleaseException(final String message,
                                       final Throwable cause) {
        super(message, cause);
    }

    public NonExistentReleaseException(final Throwable cause) {
        super(cause);
    }
}
