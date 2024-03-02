package com.example.taskmanager.endpoint;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.taskmanager.exception.CustomException;
import com.example.taskmanager.model.dto.RegistrationRequest;
import com.example.taskmanager.model.dto.UserDataDTO;
import com.example.taskmanager.service.SystemUserService;

@RestController
@RequestMapping("api/user")
public class SystemUserEndpoint {

    private final SystemUserService systemUserService;

    public SystemUserEndpoint(SystemUserService systemUserService) {
        this.systemUserService = systemUserService;
    }

    @GetMapping
    public List<UserDataDTO> getAllUsers() {
        return systemUserService.getAllUsers();
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody RegistrationRequest registrationRequest) throws CustomException {
        systemUserService.registerUser(registrationRequest);
        return new ResponseEntity<>("User registered", HttpStatus.CREATED);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateUser(@RequestParam("token") String token) throws CustomException {
        systemUserService.activateUser(token);
        return new ResponseEntity<>("User activated", HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<UserDataDTO> updateUserProfile(@RequestBody UserDataDTO userDataDTO) throws CustomException {
        UserDataDTO userData = systemUserService.updateUserProfile(userDataDTO);
        return new ResponseEntity<>(userData, HttpStatus.OK);
    }
}
