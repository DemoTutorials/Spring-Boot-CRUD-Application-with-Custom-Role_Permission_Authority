package com.employee.security.service;

import com.employee.security.dto.LoginRequestDTO;
import com.employee.security.dto.LoginResponseDTO;
import com.employee.security.dto.SignupRequestDTO;
import com.employee.security.dto.SignupResponseDTO;

public interface AuthService {
    SignupResponseDTO signup(SignupRequestDTO signupRequestDTO);

    LoginResponseDTO login(LoginRequestDTO loginRequestDTO);
}
