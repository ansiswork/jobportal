package com.example.jobportal.dto;

import java.time.LocalDateTime;

public class ApplicationResponse {

    private Long id;
    private Long jobId;
    private String jobTitle;
    private String company;
    private LocalDateTime appliedAt;
    private String status;

    public ApplicationResponse(Long id, Long jobId, String jobTitle,
            String company, LocalDateTime appliedAt, String status) {
        this.id = id;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.company = company;
        this.appliedAt = appliedAt;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public Long getJobId() {
        return jobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getCompany() {
        return company;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public String getStatus() {
        return status;
    }
}
