package com.example.employee_service.repository;

import com.example.employee_service.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    // For "view my own details"
    Optional<Employee> findByEmail(String email);
    
    // For "view employees in my department"
    List<Employee> findByDepartmentId(Long departmentId);
}