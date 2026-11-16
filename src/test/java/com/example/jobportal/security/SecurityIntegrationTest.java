package com.example.jobportal.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.jobportal.controller.JobController;
import com.example.jobportal.service.JobService;  

import org.springframework.http.HttpStatus;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity; 

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

import org.springframework.boot.test.context.TestConfiguration;

import static org.mockito.ArgumentMatchers.any;
import org.mockito.ArgumentMatchers;
import static org.mockito.Mockito.when;

import com.example.jobportal.dto.JobRequest;
import com.example.jobportal.dto.JobResponse;
import com.example.jobportal.entity.User;
import com.example.jobportal.repository.UserRepository;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import io.jsonwebtoken.MalformedJwtException;
import java.util.Optional; 

@WebMvcTest(JobController.class)
@AutoConfigureMockMvc
@Import({
    SecurityIntegrationTest.TestSecurityConfig.class
})
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private JobService jobService;
    
    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @Test
    void shouldRejectRequestWithoutToken() throws Exception {
        mockMvc.perform(get("/api/jobs"))
                .andExpect(status().isUnauthorized());
    } 
    
    @Test
    void shouldRejectUserWhenCreatingJob() throws Exception {

        mockMvc.perform(post("/api/jobs")
                .with(user("testuser").roles("USER"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "title": "Java Developer",
                        "description": "Spring Boot developer",
                        "company": "ABC Company",
                        "location": "Atlanta",
                        "salary": 90000
                    }
                    """))
                .andExpect(status().isForbidden());
    }
    
    @Test
    void shouldAllowAdminToCreateJob() throws Exception {
    	
    	JobResponse response = new JobResponse(
    	        1L,
    	        "Java Developer",
    	        "Spring Boot developer",
    	        "ABC Company",
    	        "Atlanta",
    	        90000.0
    	);

    	when(jobService.createJob(any(JobRequest.class)))
    	        .thenReturn(response);

        mockMvc.perform(post("/api/jobs")
                .with(user("admin").roles("ADMIN"))
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "title": "Java Developer",
                        "description": "Spring Boot developer",
                        "company": "ABC Company",
                        "location": "Atlanta",
                        "salary": 90000
                    }
                    """))
                .andExpect(status().isCreated());
    } 
    
    @Test
    void shouldRejectInvalidJwt() throws Exception {

        when(jwtService.extractEmail("invalid-token"))
            .thenThrow(new MalformedJwtException("Invalid token"));

        mockMvc.perform(get("/api/jobs")
                .header("Authorization", "Bearer invalid-token"))
            .andExpect(status().isUnauthorized());
    }
    
    @Test
    void shouldAllowAdminWithValidJwt() throws Exception {

        User admin = new User();
        admin.setEmail("admin@test.com");
        admin.setRole("ADMIN");

        when(jwtService.extractEmail("valid-token"))
            .thenReturn("admin@test.com");

        when(userRepository.findByEmail("admin@test.com"))
            .thenReturn(Optional.of(admin));

        when(jwtService.isTokenValid(
                "valid-token", admin))
            .thenReturn(true);

        JobResponse response = new JobResponse(
            1L,
            "Java Developer",
            "Spring Boot role",
            "ABC Company",
            "Remote",
            90000.0
        );

        when(jobService.createJob(ArgumentMatchers.any(JobRequest.class)))
            .thenReturn(response);

        mockMvc.perform(post("/api/jobs")
                .header("Authorization", "Bearer valid-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "title": "Java Developer",
                        "description": "Spring Boot role",
                        "company": "ABC Company",
                        "location": "Remote",
                        "salary": 90000
                    }
                    """))
            .andExpect(status().isCreated());
    }
    
    @Test
    void shouldRejectUserWithValidJwtWhenCreatingJob() throws Exception {

        User user = new User();
        user.setEmail("user@test.com");
        user.setRole("USER");

        when(jwtService.extractEmail("valid-user-token"))
            .thenReturn("user@test.com");

        when(userRepository.findByEmail("user@test.com"))
            .thenReturn(Optional.of(user));

        when(jwtService.isTokenValid("valid-user-token", user))
            .thenReturn(true);

        mockMvc.perform(post("/api/jobs")
                .header("Authorization", "Bearer valid-user-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "title": "Java Developer",
                        "description": "Spring Boot role",
                        "company": "ABC Company",
                        "location": "Remote",
                        "salary": 90000
                    }
                    """))
            .andExpect(status().isForbidden());
    }

    @TestConfiguration
    @EnableWebSecurity
    @EnableMethodSecurity
    static class TestSecurityConfig {

    	@Bean
    	SecurityFilterChain testSecurityFilterChain(
    	        HttpSecurity http,
    	        JwtService jwtService,
    	        UserRepository userRepository) throws Exception {

    	    JwtAuthenticationFilter jwtFilter =
    	            new JwtAuthenticationFilter(jwtService, userRepository);

    	    http
    	        .csrf(csrf -> csrf.disable())
    	        .exceptionHandling(exception -> exception
    	            .authenticationEntryPoint(
    	                new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)
    	            )
    	        )
    	        .authorizeHttpRequests(auth -> auth
    	            .anyRequest().authenticated()
    	        )
    	        .addFilterBefore(
    	            jwtFilter,
    	            UsernamePasswordAuthenticationFilter.class
    	        );

    	    return http.build();
    	}
    }
}