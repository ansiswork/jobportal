package com.example.jobportal.controller;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.example.jobportal.controller.UserController;
import com.example.jobportal.dto.LoginRequest;
import com.example.jobportal.dto.LoginResponse;
import com.example.jobportal.dto.UserResponse;
import com.example.jobportal.repository.UserRepository;
import com.example.jobportal.security.JwtService;
import com.example.jobportal.service.UserService;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
	
	@Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService; 
    
    @MockitoBean
    JwtService jwtService;

    @MockitoBean
    UserRepository userRepository;
    
    @Test
    void shouldRegisterUser() throws Exception {

        UserResponse response = new UserResponse();
        response.setId(1L);
        response.setName("John");
        response.setEmail("john@example.com");
        response.setRole("USER");

        when(userService.register(any()))
                .thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "John",
                        "email": "john@example.com",
                        "password": "password123"
                    }
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    } 
    
    @Test
    void shouldRejectRegistrationWithInvalidData() throws Exception {

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "name": "John",
                        "email": "",
                        "password": "password123"
                    }
                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    } 
    
    @Test
    void shouldLoginUser() throws Exception {

        LoginResponse response = new LoginResponse("test-jwt-token");

        when(userService.login(any(LoginRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "email": "john@example.com",
                        "password": "password123"
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-jwt-token"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

}
