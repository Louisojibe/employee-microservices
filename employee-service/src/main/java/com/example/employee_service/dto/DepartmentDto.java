package com.example.employee_service.dto;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;

@Data
public class DepartmentDto {
    private Long id;
    
    @NotEmpty
    private String name;
}