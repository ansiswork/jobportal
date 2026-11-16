package com.example.jobportal.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.jobportal.dto.ApplicationResponse;
import com.example.jobportal.entity.Application;
import com.example.jobportal.entity.Job;
import com.example.jobportal.entity.User;
import com.example.jobportal.repository.ApplicationRepository;
import com.example.jobportal.repository.JobRepository;
import com.example.jobportal.repository.UserRepository;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;

    public ApplicationService(
            ApplicationRepository applicationRepository,
            JobRepository jobRepository,
            UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
    }

    public ApplicationResponse applyForJob(String email, Long jobId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new IllegalArgumentException("Job not found"));

        if (applicationRepository.existsByUser_IdAndJob_Id(user.getId(), jobId)) {
            throw new IllegalArgumentException("You have already applied for this job");
        }

        Application application = new Application();
        application.setUser(user);
        application.setJob(job);
        application.setAppliedAt(LocalDateTime.now());
        application.setStatus("PENDING");

        Application saved = applicationRepository.save(application);

        return new ApplicationResponse(
                saved.getId(),
                job.getId(),
                job.getTitle(),
                job.getCompany(),
                saved.getAppliedAt(),
                saved.getStatus()
        );
    }

    public List<ApplicationResponse> getMyApplications(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return applicationRepository.findByUser_Id(user.getId())
                .stream()
                .map(application -> new ApplicationResponse(
                        application.getId(),
                        application.getJob().getId(),
                        application.getJob().getTitle(),
                        application.getJob().getCompany(),
                        application.getAppliedAt(),
                        application.getStatus()
                ))
                .toList();
    }
    
    public List<ApplicationResponse> getAllApplications() {
        return applicationRepository.findAll().stream()
                .map(application -> new ApplicationResponse(
                        application.getId(),
                        application.getJob().getId(),
                        application.getJob().getTitle(),
                        application.getJob().getCompany(),
                        application.getAppliedAt(),
                        application.getStatus()))
                .toList();
    }

    public ApplicationResponse updateApplicationStatus(
            Long applicationId, String status) {

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Application not found"));

        application.setStatus(status);

        Application updated = applicationRepository.save(application);

        return new ApplicationResponse(
                updated.getId(),
                updated.getJob().getId(),
                updated.getJob().getTitle(),
                updated.getJob().getCompany(),
                updated.getAppliedAt(),
                updated.getStatus());
    }
}
