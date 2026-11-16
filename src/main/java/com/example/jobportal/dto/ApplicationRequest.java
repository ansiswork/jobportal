package com.example.jobportal.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class ApplicationRequest {

    @NotNull(message = "Job ID is required")
    @Positive(message = "Job ID must be positive")
    private Long jobId;

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }
}