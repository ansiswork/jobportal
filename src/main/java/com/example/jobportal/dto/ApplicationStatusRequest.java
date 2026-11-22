package com.example.jobportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class ApplicationStatusRequest {

    @NotBlank(message = "Status is required")
    @Pattern(
        regexp = "ACCEPTED|REJECTED",
        message = "Status must be ACCEPTED or REJECTED"
    )
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
