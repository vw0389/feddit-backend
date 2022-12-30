package com.vweinert.fedditbackend.controllers;

import com.vweinert.fedditbackend.dto.AuthDto;
import com.vweinert.fedditbackend.service.inter.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.vweinert.fedditbackend.entities.User;
import com.vweinert.fedditbackend.request.auth.LoginRequest;
import com.vweinert.fedditbackend.request.auth.SignupRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    public AuthController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(@Validated(LoginRequest.class) @RequestBody User loginRequest) {
        try {
            User user = userService.sigInUser(loginRequest);
            AuthDto authDto = modelMapper.map(user, AuthDto.class);
            return ResponseEntity.ok(authDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Validated(SignupRequest.class) @RequestBody User signUpRequest) {
        try {
            User user = userService.registerUser(signUpRequest);
            AuthDto authDto = modelMapper.map(user, AuthDto.class);
            return ResponseEntity.ok(authDto);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
