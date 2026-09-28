package com.example.jobtracker.service;

import com.example.jobtracker.exception.DuplicateUrlException;
import com.example.jobtracker.model.JobApplication;
import com.example.jobtracker.repository.JobApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JobApplicationService {

    private final JobApplicationRepository repository;

    // Constructor injection: the dependency is final and can't be null, and the
    // class can be built with a plain "new" in a test without Spring involved.
    public JobApplicationService(JobApplicationRepository repository) {
        this.repository = repository;
    }

    /**
     * The duplicate check lives here, not in the controller or the entity,
     * because "you can't apply to the same URL twice" is a business rule. The
     * controller's job is only to translate HTTP; the repository's job is only
     * to talk to the database.
     *
     * The database still has a UNIQUE constraint on url. That's the real
     * guarantee — this check exists so the user gets a readable 409 instead of a
     * raw constraint violation surfacing as a 500.
     */
    @Transactional
    public JobApplication addApplication(JobApplication application) {
        if (repository.existsByUrl(application.getUrl())) {
            throw new DuplicateUrlException();
        }
        return repository.save(application);
    }

    // readOnly tells Hibernate it doesn't need to track changes for these
    // objects, since nothing here will be modified.
    @Transactional(readOnly = true)
    public List<JobApplication> getAll() {
        return repository.findAllByOrderByAppliedDateDescIdDesc();
    }
}
