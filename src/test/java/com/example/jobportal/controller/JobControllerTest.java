package com.example.jobportal.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import com.example.jobportal.service.JobService;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.http.MediaType;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import com.example.jobportal.dto.JobResponse;
import com.example.jobportal.repository.UserRepository;
import com.example.jobportal.security.JwtService;
import com.example.jobportal.dto.JobRequest;


@WebMvcTest(JobController.class)
@AutoConfigureMockMvc(addFilters = false)
class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobService jobService;
    
    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    UserRepository userRepository;
    
    @Test
    void shouldGetAllJobs() throws Exception {

        JobResponse job = new JobResponse(
                1L,
                "Java Developer",
                "Spring Boot development",
                "ABC Company",
                "Atlanta",
                85000.0
        );

        Page<JobResponse> jobPage = new PageImpl<>(
                List.of(job),
                PageRequest.of(0, 5),
                1
        );

        when(jobService.getJobs(0, 5))
                .thenReturn(jobPage);

        mockMvc.perform(get("/api/jobs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title")
                        .value("Java Developer"))
                .andExpect(jsonPath("$.content[0].company")
                        .value("ABC Company"))
                .andExpect(jsonPath("$.content[0].location")
                        .value("Atlanta"));
    }
    
    @Test
    void shouldGetJobById() throws Exception {

        JobResponse job = new JobResponse(
                1L,
                "Java Developer",
                "Spring Boot development",
                "ABC Company",
                "Atlanta",
                85000.0
        );

        when(jobService.getJobById(1L))
                .thenReturn(job);

        mockMvc.perform(get("/api/jobs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Java Developer"))
                .andExpect(jsonPath("$.company").value("ABC Company"));
    }
    
    @Test
    void shouldCreateJob() throws Exception {

        JobResponse response = new JobResponse(
                1L,
                "Java Developer",
                "Spring Boot development",
                "ABC Company",
                "Atlanta",
                85000.0
        );

        when(jobService.createJob(any(JobRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/jobs")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "title": "Java Developer",
                        "description": "Spring Boot development",
                        "company": "ABC Company",
                        "location": "Atlanta",
                        "salary": 85000
                    }
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Java Developer"))
                .andExpect(jsonPath("$.company").value("ABC Company"));
    }
    
    @Test
    void shouldUpdateJob() throws Exception {

        JobResponse response = new JobResponse(
                1L,
                "Senior Java Developer",
                "Advanced Spring Boot development",
                "ABC Company",
                "Atlanta",
                100000.0
        );

        when(jobService.updateJob(
                org.mockito.ArgumentMatchers.eq(1L),
                any(JobRequest.class)
        )).thenReturn(response);

        mockMvc.perform(put("/api/jobs/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "title": "Senior Java Developer",
                        "description": "Advanced Spring Boot development",
                        "company": "ABC Company",
                        "location": "Atlanta",
                        "salary": 100000
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Senior Java Developer"))
                .andExpect(jsonPath("$.salary").value(100000.0));
    }
    
    @Test
    void shouldDeleteJob() throws Exception {

        mockMvc.perform(delete("/api/jobs/1"))
                .andExpect(status().isNoContent());

        verify(jobService).deleteJob(1L);
    }
}
