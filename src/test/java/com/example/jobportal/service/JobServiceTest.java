package com.example.jobportal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.jobportal.dto.JobResponse;
import com.example.jobportal.entity.Job;
import com.example.jobportal.exception.JobNotFoundException;
import com.example.jobportal.repository.JobRepository;
import com.example.jobportal.service.JobService;

@ExtendWith(MockitoExtension.class)
public class JobServiceTest { 
	
	@Mock
    private JobRepository jobRepository;

    @InjectMocks
    private JobService jobService;

    @Test
    void shouldFindJobById() {

        // Arrange: create a sample job
        Job job = new Job();
        job.setId(1L);
        job.setTitle("Java Developer");
        job.setCompany("ABC Company");

        // Tell Mockito what the repository should return
        when(jobRepository.findById(1L))
                .thenReturn(Optional.of(job));

        // Act: call the service
        JobResponse result = jobService.getJobById(1L);

        // Assert: verify the result
        assertNotNull(result);
        assertEquals("Java Developer", result.getTitle());

        verify(jobRepository).findById(1L); 
    
    }
    
    @Test
    void shouldThrowExceptionWhenJobDoesNotExist() {

        // Arrange
        when(jobRepository.findById(999L))
                .thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(
            JobNotFoundException.class,
            () -> jobService.getJobById(999L)
        );

        verify(jobRepository).findById(999L);
    }

}
