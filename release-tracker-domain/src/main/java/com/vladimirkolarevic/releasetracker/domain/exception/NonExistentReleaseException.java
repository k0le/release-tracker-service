package com.vladimirkolarevic.releasetracker.domain.exception;

public class NonExistentReleaseException extends ReleaseTrackerException{



    public NonExistentReleaseException(String message){
        super(message);
    }

    public NonExistentReleaseException(String message,Throwable cause){
        super(message,cause);
    }

    public NonExistentReleaseException(Throwable cause){
        super(cause);
    }
}
