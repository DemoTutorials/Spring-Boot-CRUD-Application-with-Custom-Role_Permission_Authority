package com.employee.security.controller;

import com.employee.security.dto.LoginRequestDTO;
import com.employee.security.dto.LoginResponseDTO;
import com.employee.security.dto.SignupRequestDTO;
import com.employee.security.dto.SignupResponseDTO;
import com.employee.security.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDTO> signUp(@RequestBody SignupRequestDTO signupRequestDTO){
        return ResponseEntity.status(HttpStatus.OK).body(authService.signup(signupRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        return ResponseEntity.status(HttpStatus.OK).body(authService.login(loginRequestDTO));
    }
}
