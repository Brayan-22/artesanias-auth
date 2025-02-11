package com.artesanias.authenticationservice.controller;

import com.artesanias.authenticationservice.dto.AuthRequest;
import com.artesanias.authenticationservice.dto.AuthResponse;
import com.artesanias.authenticationservice.dto.LoginRequestDto;
import com.artesanias.authenticationservice.dto.RefreshTokenRequestDto;
import com.artesanias.authenticationservice.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid AuthRequest request, BindingResult result){
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody@Valid LoginRequestDto request,BindingResult result){
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/validate")
    public ResponseEntity<AuthResponse> validate(@RequestBody @Valid RefreshTokenRequestDto request,BindingResult result){
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(authService.refreshToken(request.getRefreshToken()));
    }
}
