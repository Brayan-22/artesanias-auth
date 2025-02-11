package com.artesanias.authenticationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    @NotBlank
    private String nombre;
    @NotBlank
    private String apellido;
    @Size(min = 8, max = 32)
    private String password;
    @Email
    private String email;
    @NotBlank
    private String direccion;
}
