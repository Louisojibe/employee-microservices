package com.example.employee_service.controller;

import com.example.employee_service.dto.EmployeeDto;
import com.example.employee_service.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.hamcrest.Matcher;

import com.example.employee_service.config.JwtHeaderAuthenticationFilter; // Import the filter
import com.example.employee_service.config.SecurityConfig; // Import the config

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// We use @WebMvcTest and specifically target the controller
// We also @Import our SecurityConfig and Filter so the security rules are applied
@WebMvcTest(EmployeeController.class)
@Import({SecurityConfig.class, JwtHeaderAuthenticationFilter.class})
@AutoConfigureMockMvc
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    // Test the "get my details" endpoint
    @Test
    void getMyDetails_WithEmployeeHeaders_ShouldReturnEmployee() throws Exception {
        // Arrange
        String userEmail = "employee@example.com";
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(1L);
        employeeDto.setEmail(userEmail);
        employeeDto.setFirstName("Test");
        employeeDto.setLastName("Employee");

        given(employeeService.getEmployeeByEmail(userEmail)).willReturn(employeeDto);

        // Act & Assert
        mockMvc.perform(get("/api/v1/employees/me")
                // We simulate the API Gateway by adding the required headers
                .header(JwtHeaderAuthenticationFilter.HEADER_USER_EMAIL, userEmail)
                .header(JwtHeaderAuthenticationFilter.HEADER_USER_ROLES, "ROLE_EMPLOYEE")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value(userEmail))
            .andExpect(jsonPath("$.firstName").value("Test"));
    }

    // Test that an unauthorized user gets 403 Forbidden
    @Test
    void getMyDetails_WithNoRoles_ShouldReturnForbidden() throws Exception {
        // Arrange
        String userEmail = "employee@example.com";

        // Act & Assert
        // We send the email header but NO roles header
        mockMvc.perform(get("/api/v1/employees/me")
                .header(JwtHeaderAuthenticationFilter.HEADER_USER_EMAIL, userEmail)
                .contentType(MediaType.APPLICATION_JSON))
            // Because no roles were provided, the 'authentication' object is empty
            // and Spring Security denies access
            .andExpect(status().isForbidden());
    }

    // Test that an admin can access an admin-only route
    @Test
    void getAllEmployees_WithAdminHeaders_ShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/v1/employees")
                .header(JwtHeaderAuthenticationFilter.HEADER_USER_EMAIL, "admin@example.com")
                .header(JwtHeaderAuthenticationFilter.HEADER_USER_ROLES, "ROLE_ADMIN")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    // Test that a non-admin gets 403 Forbidden on an admin-only route
    @Test
    void getAllEmployees_WithEmployeeHeaders_ShouldReturnForbidden() throws Exception {
        mockMvc.perform(get("/api/v1/employees")
                .header(JwtHeaderAuthenticationFilter.HEADER_USER_EMAIL, "employee@example.com")
                .header(JwtHeaderAuthenticationFilter.HEADER_USER_ROLES, "ROLE_EMPLOYEE")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isForbidden());
    }
}