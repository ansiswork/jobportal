package com.example.jobportal.controller;

import com.example.jobportal.dto.JobRequest;
import com.example.jobportal.dto.JobResponse;
import com.example.jobportal.service.JobService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*; 
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public JobResponse createJob(@Valid @RequestBody JobRequest request) {

        return jobService.createJob(request);
    }

    @GetMapping
    public Page<JobResponse> getAllJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        return jobService.getJobs(page, size);
    }

    @GetMapping("/{id}")
    public JobResponse getJobById(@PathVariable Long id) {
        return jobService.getJobById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public JobResponse updateJob(
            @PathVariable Long id,
            @Valid @RequestBody JobRequest request) {

        return jobService.updateJob(id, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
    }
}