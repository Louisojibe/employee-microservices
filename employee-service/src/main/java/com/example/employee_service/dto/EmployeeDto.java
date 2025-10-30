package com.example.employee_service.dto;

import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

@Data
public class EmployeeDto {
    private Long id;
    
    @NotEmpty
    private String firstName;
    
    @NotEmpty
    private String lastName;
    
    @NotEmpty
    @Email
    private String email;
    
    private String status;
    private Long departmentId; // Only send the ID
    private String departmentName; // Useful for GET responses
}