
package com.example.jobportal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.jobportal.entity.Application;
import com.example.jobportal.entity.Job;
import com.example.jobportal.entity.User;
import com.example.jobportal.repository.ApplicationRepository;
import com.example.jobportal.repository.JobRepository;
import com.example.jobportal.repository.UserRepository;

import java.util.List;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ApplicationService applicationService;

    private User user;
    private Job job;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(2L);
        user.setEmail("jane@example.com");

        job = new Job();
        job.setId(2L);
        job.setTitle("Senior Java Developer");
        job.setCompany("ABC Technologies");
    }

    @Test
    void applyForJobSuccessfully() {
        when(userRepository.findByEmail("jane@example.com"))
                .thenReturn(Optional.of(user));

        when(jobRepository.findById(2L))
                .thenReturn(Optional.of(job));

        when(applicationRepository.existsByUser_IdAndJob_Id(2L, 2L))
                .thenReturn(false);

        when(applicationRepository.save(any(Application.class)))
                .thenAnswer(invocation -> {
                    Application application = invocation.getArgument(0);
                    application.setId(1L);
                    return application;
                });

        var response = applicationService.applyForJob(
                "jane@example.com", 2L);

        assertEquals(1L, response.getId());
        assertEquals(2L, response.getJobId());
        assertEquals("Senior Java Developer", response.getJobTitle());
        assertEquals("PENDING", response.getStatus());

        verify(applicationRepository).save(any(Application.class));
    }

    @Test
    void applyForJobRejectsDuplicateApplication() {
        when(userRepository.findByEmail("jane@example.com"))
                .thenReturn(Optional.of(user));

        when(jobRepository.findById(2L))
                .thenReturn(Optional.of(job));

        when(applicationRepository.existsByUser_IdAndJob_Id(2L, 2L))
                .thenReturn(true);

        assertThrows(IllegalArgumentException.class, () ->
                applicationService.applyForJob("jane@example.com", 2L));

        verify(applicationRepository, never()).save(any(Application.class));
    }
    
    @Test
    void getMyApplicationsSuccessfully() {
        Application application = new Application();
        application.setId(1L);
        application.setUser(user);
        application.setJob(job);
        application.setStatus("PENDING");

        when(userRepository.findByEmail("jane@example.com"))
                .thenReturn(Optional.of(user));

        when(applicationRepository.findByUser_Id(2L))
                .thenReturn(java.util.List.of(application));

        var responses = applicationService.getMyApplications(
                "jane@example.com");

        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getId());
        assertEquals("Senior Java Developer",
                responses.get(0).getJobTitle());
        assertEquals("PENDING", responses.get(0).getStatus());

        verify(applicationRepository).findByUser_Id(2L);
    }
    
    @Test
    void getAllApplicationsSuccessfully() {
        Application application = new Application();
        application.setId(1L);
        application.setUser(user);
        application.setJob(job);
        application.setStatus("PENDING");

        when(applicationRepository.findAll())
                .thenReturn(java.util.List.of(application));

        var responses = applicationService.getAllApplications();

        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getId());
        assertEquals("Senior Java Developer",
                responses.get(0).getJobTitle());
        assertEquals("PENDING", responses.get(0).getStatus());

        verify(applicationRepository).findAll();
    }
    
    @Test
    void updateApplicationStatusSuccessfully() {
        Application application = new Application();
        application.setId(1L);
        application.setUser(user);
        application.setJob(job);
        application.setStatus("PENDING");

        when(applicationRepository.findById(1L))
                .thenReturn(Optional.of(application));

        when(applicationRepository.save(any(Application.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        var response = applicationService.updateApplicationStatus(
                1L, "ACCEPTED");

        assertEquals(1L, response.getId());
        assertEquals("ACCEPTED", response.getStatus());
        assertEquals("Senior Java Developer", response.getJobTitle());

        verify(applicationRepository).save(application);
    }
}

