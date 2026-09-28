package com.example.jobtracker.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

@Entity
@Table(name = "job_application")
public class JobApplication {

    @Id
    // IDENTITY lets Postgres generate the id with its own identity column,
    // so two simultaneous inserts can never be handed the same number.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "company must not be blank")
    @Column(nullable = false)
    private String company;

    @NotBlank(message = "role must not be blank")
    @Column(nullable = false)
    private String role;

    // unique = true adds a database-level constraint, which is a real guarantee
    // even if application code has a bug. The friendly 409 comes from the
    // service check added in Step 3.
    @NotBlank(message = "url must not be blank")
    @Column(nullable = false, unique = true)
    private String url;

    // STRING stores the word "APPLIED". The default, ORDINAL, would store 0 —
    // and reordering the enum later would silently change what old rows mean.
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ApplicationStatus status = ApplicationStatus.APPLIED;

    @Column(nullable = false)
    private LocalDate appliedDate = LocalDate.now();

    // JPA needs a no-args constructor to create entities by reflection.
    public JobApplication() {
    }

    public JobApplication(String company, String role, String url) {
        this.company = company;
        this.role = role;
        this.url = url;
    }

    /**
     * The field initialisers above cover a request that omits these fields, but
     * not one that sends them as explicit nulls — Jackson would overwrite the
     * defaults with null and the insert would fail on the NOT NULL columns.
     * This runs just before the insert and fills them back in.
     */
    @PrePersist
    void applyDefaults() {
        if (status == null) {
            status = ApplicationStatus.APPLIED;
        }
        if (appliedDate == null) {
            appliedDate = LocalDate.now();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDate getAppliedDate() {
        return appliedDate;
    }

    public void setAppliedDate(LocalDate appliedDate) {
        this.appliedDate = appliedDate;
    }
}
