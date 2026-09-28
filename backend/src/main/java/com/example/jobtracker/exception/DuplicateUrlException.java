package com.example.jobtracker.exception;

// Extends RuntimeException so it doesn't have to be declared in every method
// signature between the service and the controller.
public class DuplicateUrlException extends RuntimeException {

    public DuplicateUrlException() {
        // This text is shown to the user by the Chrome extension, so it's phrased
        // for a person rather than a developer.
        super("Already applied to this job");
    }
}
