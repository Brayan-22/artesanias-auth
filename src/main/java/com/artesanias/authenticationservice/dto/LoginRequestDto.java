package com.artesanias.authenticationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class LoginRequestDto {
    @Email
    private String email;
    @Size(min = 8, max = 32)
    private String password;
}
