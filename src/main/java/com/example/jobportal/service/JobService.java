package com.example.jobportal.service;

import com.example.jobportal.dto.JobRequest; 
import com.example.jobportal.dto.JobResponse;
import com.example.jobportal.entity.Job;
import com.example.jobportal.exception.JobNotFoundException;
import com.example.jobportal.repository.JobRepository;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Service
public class JobService {

    private final JobRepository jobRepository;

    public JobService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public JobResponse createJob(JobRequest request) {

        Job job = new Job();

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());

        Job savedJob = jobRepository.save(job);

        return convertToResponse(savedJob);
    }

    public List<JobResponse> getAllJobs() {

        return jobRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public JobResponse getJobById(Long id) {

        Job job = jobRepository.findById(id) 
                 .orElseThrow(() ->
                        //new RuntimeException("Job not found with id: " + id));
                        new JobNotFoundException(id));
        		

        return convertToResponse(job);
    }

    public JobResponse updateJob(Long id, JobRequest request) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        //new RuntimeException("Job not found with id: " + id));
                new JobNotFoundException(id));

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setCompany(request.getCompany());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());

        Job updatedJob = jobRepository.save(job);

        return convertToResponse(updatedJob);
    }

    public void deleteJob(Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        //new RuntimeException("Job not found with id: " + id));
                        new JobNotFoundException(id));

        jobRepository.delete(job);
    }

    private JobResponse convertToResponse(Job job) {

        return new JobResponse(
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getCompany(),
                job.getLocation(),
                job.getSalary()
        );
    } 
    
    public Page<JobResponse> getJobs(int page, int size) {

        Page<Job> jobs = jobRepository.findAll(
                PageRequest.of(page, size)
        );

        return jobs.map(this::convertToResponse);
    }
    
    public Page<JobResponse> searchJobs(
            String keyword,
            int page,
            int size) {

        Page<Job> jobs = jobRepository
                .findByTitleContainingIgnoreCase(
                        keyword,
                        PageRequest.of(page, size)
                );

        return jobs.map(this::convertToResponse);
    } 
    
    public Page<JobResponse> searchByLocation(
            String location,
            int page,
            int size) {

        Page<Job> jobs = jobRepository
                .findByLocationContainingIgnoreCase(
                        location,
                        PageRequest.of(page, size)
                );

        return jobs.map(this::convertToResponse);
    }
}  

