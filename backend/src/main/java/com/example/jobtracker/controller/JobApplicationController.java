package com.example.jobtracker.controller;

import com.example.jobtracker.model.JobApplication;
import com.example.jobtracker.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// @RestController = @Controller + @ResponseBody, so returned objects are
// converted straight to JSON instead of being treated as view names.
@RestController
@RequestMapping("/api/v1/applications")
public class JobApplicationController {

    private final JobApplicationService service;

    public JobApplicationController(JobApplicationService service) {
        this.service = service;
    }

    /**
     * @Valid is what actually runs the @NotBlank checks on the entity. Without
     * it the annotations are just decoration and blank values would be saved.
     */
    @PostMapping
    public ResponseEntity<JobApplication> add(@Valid @RequestBody JobApplication application) {
        JobApplication saved = service.addApplication(application);

        // 201 Created rather than 200, because this request created a new
        // resource. The saved object is returned so the caller learns its id.
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping
    public List<JobApplication> getAll() {
        return service.getAll();
    }
}
