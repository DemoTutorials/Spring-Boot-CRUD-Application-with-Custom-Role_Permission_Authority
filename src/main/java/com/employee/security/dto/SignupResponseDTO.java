package com.employee.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupResponseDTO {
    private Long userId;
    private String username;
    private List<String> roles;
    private List<String> permissions;
    private List<String> authority;
    private String createdAt;
}
