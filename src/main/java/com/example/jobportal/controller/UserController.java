package com.example.jobportal.controller;

import org.springframework.web.bind.annotation.RequestBody;

import com.example.jobportal.dto.LoginRequest;
import com.example.jobportal.dto.LoginResponse;
import com.example.jobportal.dto.UserRequest;
import com.example.jobportal.dto.UserResponse;
import com.example.jobportal.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(
            @Valid @RequestBody UserRequest request) {

        return userService.register(request);
    } 
    


@PostMapping("/login")
public LoginResponse login(
        @Valid @RequestBody LoginRequest request) {

    return userService.login(request);
}


}