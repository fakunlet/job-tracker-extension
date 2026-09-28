package com.example.jobtracker.model;

// An enum instead of a plain String, so only these four values can ever exist.
// A typo like "APPLIEDD" fails at compile time or as a 400 from the API, rather
// than quietly becoming a row in the database nothing can filter on.
public enum ApplicationStatus {
    APPLIED,
    INTERVIEWING,
    OFFER,
    REJECTED
}
