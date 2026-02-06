package com.employee.controller;

import com.employee.dto.EmployeeRequestDTO;
import com.employee.dto.EmployeeResponseDTO;
import com.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // CREATE
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER','EMPLOYEE') AND hasAuthority('CAN_CREATE_EMPLOYEE')")
    public ResponseEntity<EmployeeResponseDTO> save(@Valid @RequestBody EmployeeRequestDTO employeeRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.create(employeeRequestDTO));
    }

    // GET ALL Employees
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER','EMPLOYEE','INTERN') AND hasAuthority('CAN_READ_EMPLOYEE')")
    public ResponseEntity<List<EmployeeResponseDTO>> getAll(){
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.getAll());
    }

    // GET By ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER','EMPLOYEE','INTERN') AND hasAuthority('CAN_READ_EMPLOYEE')")
    public ResponseEntity<EmployeeResponseDTO> getById(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.getById(id));
    }

    // UPDATE By ID
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER') AND hasAuthority('CAN_UPDATE_EMPLOYEE')")
    public ResponseEntity<EmployeeResponseDTO> update(@PathVariable Long id,@Valid @RequestBody EmployeeRequestDTO employeeRequestDTO){
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.update(id,employeeRequestDTO));
    }

    // DELETE By ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HR') AND hasAuthority('CAN_DELETE_EMPLOYEE')")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Specific Fields Update By ID
    @PatchMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','HR','MANAGER') AND hasAuthority('CAN_UPDATE_EMPLOYEE')")
    public ResponseEntity<EmployeeResponseDTO> patchUpdate(@PathVariable Long id, @RequestBody Map<String,Object> updates){
        return ResponseEntity.status(HttpStatus.OK).body(employeeService.patchUpdate(id,updates));
    }

}
