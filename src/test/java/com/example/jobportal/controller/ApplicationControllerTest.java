package com.example.jobportal.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import com.example.jobportal.service.ApplicationService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.example.jobportal.controller.ApplicationController;
import com.example.jobportal.dto.ApplicationResponse;

import com.example.jobportal.security.JwtService;
import com.example.jobportal.security.JwtAuthenticationFilter;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.mockito.Mockito.verify;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WebMvcTest(ApplicationController.class)
@AutoConfigureMockMvc(addFilters = false)
class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ApplicationService applicationService;
    
    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    
    @Test
    @WithMockUser(username = "jane@example.com", roles = "USER")
    void applyForJobSuccessfully() throws Exception {

        ApplicationResponse response = new ApplicationResponse(
                1L,
                2L,
                "Senior Java Developer",
                "ABC Technologies",
                java.time.LocalDateTime.of(2026, 11, 3, 10, 0),
                "PENDING"
        );

        when(applicationService.applyForJob("jane@example.com", 2L))
                .thenReturn(response);

        mockMvc.perform(post("/api/applications")
                .principal(new UsernamePasswordAuthenticationToken(
                        "jane@example.com", null, java.util.List.of()))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"jobId\":2}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.jobId").value(2))
                .andExpect(jsonPath("$.jobTitle").value("Senior Java Developer"))
                .andExpect(jsonPath("$.status").value("PENDING"));
    }
    
    @Test
    void getMyApplicationsSuccessfully() throws Exception {

        ApplicationResponse response = new ApplicationResponse(
                1L,
                2L,
                "Senior Java Developer",
                "ABC Technologies",
                java.time.LocalDateTime.of(2026, 11, 3, 10, 0),
                "PENDING"
        );

        when(applicationService.getMyApplications("jane@example.com"))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/applications/my")
                .principal(new UsernamePasswordAuthenticationToken(
                        "jane@example.com", null, java.util.List.of())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].jobTitle").value("Senior Java Developer"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        verify(applicationService).getMyApplications("jane@example.com");
    }
    
    @Test
    void getAllApplicationsSuccessfully() throws Exception {

        ApplicationResponse response = new ApplicationResponse(
                1L,
                2L,
                "Senior Java Developer",
                "ABC Technologies",
                java.time.LocalDateTime.of(2026, 11, 3, 10, 0),
                "PENDING"
        );

        when(applicationService.getAllApplications())
                .thenReturn(List.of(response));

        mockMvc.perform(get("/api/applications/admin/all")
                .principal(new UsernamePasswordAuthenticationToken(
                        "john@example.com", null, java.util.List.of())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].jobTitle").value("Senior Java Developer"))
                .andExpect(jsonPath("$[0].status").value("PENDING"));

        verify(applicationService).getAllApplications();
    }
    
    @Test
    void updateApplicationStatusSuccessfully() throws Exception {

        ApplicationResponse response = new ApplicationResponse(
                1L,
                2L,
                "Senior Java Developer",
                "ABC Technologies",
                java.time.LocalDateTime.of(2026, 11, 3, 10, 0),
                "ACCEPTED"
        );

        when(applicationService.updateApplicationStatus(1L, "ACCEPTED"))
                .thenReturn(response);

        mockMvc.perform(put("/api/applications/admin/1/status")
                .principal(new UsernamePasswordAuthenticationToken(
                        "john@example.com", null, java.util.List.of()))
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"status\":\"ACCEPTED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("ACCEPTED"));

        verify(applicationService).updateApplicationStatus(1L, "ACCEPTED");
    }

}
