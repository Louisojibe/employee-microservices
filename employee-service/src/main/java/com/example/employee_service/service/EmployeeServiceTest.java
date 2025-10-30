package com.example.employee_service.service;

import com.example.employee_service.dto.EmployeeDto;
import com.example.employee_service.exception.ResourceNotFoundException;
import com.example.employee_service.model.Employee;
import com.example.employee_service.repository.DepartmentRepository;
import com.example.employee_service.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @Test
    void getEmployeeById_WhenEmployeeExists_ShouldReturnEmployeeDto() {
        // Arrange
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Test");
        employee.setLastName("User");
        employee.setEmail("test@example.com");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // Act
        EmployeeDto result = employeeService.getEmployeeById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(employee.getId(), result.getId());
        assertEquals(employee.getEmail(), result.getEmail());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void getEmployeeById_WhenEmployeeNotExists_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeById(1L);
        });
        
        verify(employeeRepository, times(1)).findById(1L);
    }
}