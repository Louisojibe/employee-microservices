package com.example.auth_service.dto;
import lombok.Data;
@Data
public class LoginDto {
    private String usernameOrEmail;
    private String password;
}