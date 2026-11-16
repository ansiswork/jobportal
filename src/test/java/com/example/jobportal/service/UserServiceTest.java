package com.example.jobportal.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.jobportal.dto.UserRequest;
import com.example.jobportal.dto.UserResponse;
import com.example.jobportal.entity.User;
import com.example.jobportal.repository.UserRepository;
import com.example.jobportal.security.JwtService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

	    @Mock
	    private UserRepository userRepository;

	    @Mock
	    private PasswordEncoder passwordEncoder;

	    @Mock
	    private JwtService jwtService;

	    @InjectMocks
	    private UserService userService;
	    
	    @Test
	    void shouldRejectDuplicateEmail() {

	        // Arrange
	        UserRequest request = new UserRequest();
	        request.setName("John");
	        request.setEmail("john@example.com");
	        request.setPassword("password123");

	        when(userRepository.existsByEmail("john@example.com"))
	                .thenReturn(true);

	        // Act and Assert
	        assertThrows(
	            RuntimeException.class,
	            () -> userService.register(request)
	        );

	        // Verify that no user was saved
	        verify(userRepository, never()).save(any(User.class));
	    }

	    @Test
	    void shouldRegisterNewUser() {

	        // Arrange
	        UserRequest request = new UserRequest();
	        request.setName("John");
	        request.setEmail("john@example.com");
	        request.setPassword("password123");

	        when(userRepository.existsByEmail("john@example.com"))
	                .thenReturn(false);

	        when(passwordEncoder.encode("password123"))
	                .thenReturn("encryptedPassword");

	        when(userRepository.save(any(User.class)))
	                .thenAnswer(invocation -> invocation.getArgument(0));

	        // Act
	        UserResponse response = userService.register(request);

	        // Assert
	        assertNotNull(response);
	        assertEquals("john@example.com", response.getEmail());
	        assertEquals("USER", response.getRole());

	        verify(userRepository).save(any(User.class));
	        verify(passwordEncoder).encode("password123");
	        verify(userRepository).save(argThat(user ->
	        user.getPassword().equals("encryptedPassword")
	    ));
	    
	}

}
