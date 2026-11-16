package com.example.jobportal.service;

import org.springframework.stereotype.Service;

import com.example.jobportal.dto.LoginRequest;
import com.example.jobportal.dto.LoginResponse;
import com.example.jobportal.dto.UserRequest;
import com.example.jobportal.dto.UserResponse;
import com.example.jobportal.entity.User;
import com.example.jobportal.repository.UserRepository;
import com.example.jobportal.security.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.BadCredentialsException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder, 
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public UserResponse register(UserRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        String hashedPassword =
                passwordEncoder.encode(request.getPassword());

        user.setPassword(hashedPassword);

        user.setRole("USER");

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    } 
    

public LoginResponse login(LoginRequest request) {

    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() ->
                new BadCredentialsException("Invalid email or password"));

    if (!passwordEncoder.matches(
            request.getPassword(), user.getPassword())) {

        throw new BadCredentialsException(
                "Invalid email or password");
    }

    /*UserResponse response = new UserResponse();
    LoginResponse response = new LoginResponse();
    response.setId(user.getId());
    response.setName(user.getName());
    response.setEmail(user.getEmail());
    response.setRole(user.getRole());

    return response; 
    */ 
    
 // Generate a JWT after the password is verified
    String token = jwtService.generateToken(user);

    // Return the token to the client
    return new LoginResponse(token);
}

}