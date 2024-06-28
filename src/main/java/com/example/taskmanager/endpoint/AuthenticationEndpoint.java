package com.example.taskmanager.endpoint;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.exception.CustomException;
import com.example.taskmanager.model.dto.LoginRequest;
import com.example.taskmanager.model.dto.LoginResponse;
import com.example.taskmanager.service.AuthenticationService;

@RestController
public class AuthenticationEndpoint {

    private final AuthenticationService authenticationService;

    public AuthenticationEndpoint(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return authenticationService.login(loginRequest);
    }

    @PostMapping("/refreshToken")
    public LoginResponse refreshToken(@RequestBody LoginRequest loginRequest) throws CustomException {
        return authenticationService.refreshToken(loginRequest);
    }

    // TODO: Check if a logout functionality can be implemented for the back-end
    @PostMapping("/logout")
    public void logout(@RequestBody LoginRequest loginRequest) throws CustomException {
        authenticationService.logout(loginRequest);
    }
}
