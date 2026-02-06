package com.employee.security.service.service_impl;

import com.employee.exception.custom_exception.UserAlreadyExistsException;
import com.employee.security.dto.LoginRequestDTO;
import com.employee.security.dto.LoginResponseDTO;
import com.employee.security.dto.SignupRequestDTO;
import com.employee.security.dto.SignupResponseDTO;
import com.employee.security.entity.User;
import com.employee.security.enums.Authority;
import com.employee.security.enums.Permission;
import com.employee.security.enums.Role;
import com.employee.security.repository.UserRepository;
import com.employee.security.service.AuthService;
import com.employee.security.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;

    public AuthServiceImpl(ModelMapper modelMapper, PasswordEncoder passwordEncoder, UserRepository userRepository, AuthenticationManager authenticationManager, AuthUtil authUtil) {
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.authUtil = authUtil;
    }

    @Override
    public SignupResponseDTO signup(SignupRequestDTO signupRequestDTO) {
        if(userRepository.findByUsername(signupRequestDTO.getUsername()).isPresent()){
            throw new UserAlreadyExistsException("User Already Exists!..."+signupRequestDTO.getUsername());
        }
        User user = modelMapper.map(signupRequestDTO, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles=signupRequestDTO.getRoles()!=null?signupRequestDTO.getRoles().stream().map(Role::valueOf).collect(Collectors.toSet()) : new HashSet<>();
        Set<Permission> permissions=signupRequestDTO.getPermissions()!=null?signupRequestDTO.getPermissions().stream().map(Permission::valueOf).collect(Collectors.toSet()) : new HashSet<>();
        Set<Authority> authority=signupRequestDTO.getAuthority()!=null?signupRequestDTO.getAuthority().stream().map(Authority::valueOf).collect(Collectors.toSet()) : new HashSet<>();
        user.setRoles(roles);
        user.setPermissions(permissions);
        user.setAuthority(authority);
        User updatedUser = userRepository.save(user);
        SignupResponseDTO response = modelMapper.map(updatedUser, SignupResponseDTO.class);
        response.setRoles(updatedUser.getRoles().stream().map(Role::name).collect(Collectors.toList()));
        response.setPermissions(updatedUser.getPermissions().stream().map(Permission::name).collect(Collectors.toList()));
        response.setAuthority(updatedUser.getAuthority().stream().map(Authority::name).collect(Collectors.toList()));
        response.setCreatedAt(updatedUser.getCreatedAt().toString());
        return response;
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        Authentication authenticate = authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));
        User user = (User) authenticate.getPrincipal();
        String token=authUtil.generateAccessToken(user);
        return new LoginResponseDTO(user.getUserId(),token);
    }
}
