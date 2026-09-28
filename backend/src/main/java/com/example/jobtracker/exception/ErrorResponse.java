package com.example.jobtracker.exception;

// One shape for every error the API returns, so the extension can always read
// response.message without checking which kind of failure it was.
public record ErrorResponse(int status, String message) {
}
