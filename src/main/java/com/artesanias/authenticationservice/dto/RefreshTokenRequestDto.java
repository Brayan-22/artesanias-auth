package com.artesanias.authenticationservice.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RefreshTokenRequestDto {
    @NotBlank
    private String refreshToken;
}
