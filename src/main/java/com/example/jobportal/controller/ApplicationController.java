package com.example.jobportal.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.example.jobportal.dto.ApplicationRequest;
import com.example.jobportal.dto.ApplicationResponse;
import com.example.jobportal.service.ApplicationService;

import jakarta.validation.Valid;

import com.example.jobportal.dto.ApplicationStatusRequest;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping
    public ResponseEntity<ApplicationResponse> applyForJob(
            @Valid @RequestBody ApplicationRequest request,
            Authentication authentication) {

        String email = authentication.getName();

        ApplicationResponse response =
                applicationService.applyForJob(email, request.getJobId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my")
    public ResponseEntity<List<ApplicationResponse>> getMyApplications(
            Authentication authentication) {

        String email = authentication.getName();

        return ResponseEntity.ok(
                applicationService.getMyApplications(email));
    }
    
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ApplicationResponse>> getAllApplications() {
        return ResponseEntity.ok(applicationService.getAllApplications());
    }

    @PutMapping("/admin/{applicationId}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApplicationResponse> updateApplicationStatus(
            @PathVariable Long applicationId,
            @Valid @RequestBody ApplicationStatusRequest request) {

        ApplicationResponse response =
                applicationService.updateApplicationStatus(
                        applicationId, request.getStatus());

        return ResponseEntity.ok(response);
    }
}
