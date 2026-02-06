package com.employee.service;

import com.employee.dto.EmployeeRequestDTO;
import com.employee.dto.EmployeeResponseDTO;
import java.util.List;
import java.util.Map;

public interface EmployeeService {
    EmployeeResponseDTO create(EmployeeRequestDTO employeeRequestDTO);

    List<EmployeeResponseDTO> getAll();

    EmployeeResponseDTO getById(Long id);

    EmployeeResponseDTO update(Long id, EmployeeRequestDTO employeeRequestDTO);

    void delete(Long id);

    EmployeeResponseDTO patchUpdate(Long id, Map<String, Object> updates);
}
