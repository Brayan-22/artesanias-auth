package com.artesanias.authenticationservice.services;

import com.artesanias.authenticationservice.dto.*;
import com.artesanias.authenticationservice.exceptions.NotValidTokenException;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthService {
    private static final Logger log = LoggerFactory.getLogger(AuthService.class);
    @Lazy
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    public AuthResponse register(AuthRequest request) {
        request.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        UserReponseDto registeredUser = restTemplate.postForObject("http://USER-SERVICE/user/register", request, UserReponseDto.class);
        if (registeredUser == null) {
            throw new NotValidTokenException("User already exists");
        }
        String accessToken = jwtUtil.generate(registeredUser.getId(), registeredUser.getRole(), "ACCESS");
        String refreshToken = jwtUtil.generate(registeredUser.getId(), registeredUser.getRole(), "REFRESH");
        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse login(LoginRequestDto request)throws NotValidTokenException {
        ValidateUserResponseDto user = restTemplate.postForObject("http://USER-SERVICE/user/validate", request, ValidateUserResponseDto.class);
        log.info("User: {}", user);
        if (user == null || !BCrypt.checkpw(request.getPassword(), user.getPassword())) {
            throw new NotValidTokenException("Invalid credentials ");
        }
        String accessToken = jwtUtil.generate(user.getId(), user.getRole(), "ACCESS");
        String refreshToken = jwtUtil.generate(user.getId(), user.getRole(), "REFRESH");
        return new AuthResponse(accessToken, refreshToken);
    }

    public AuthResponse refreshToken(String refreshToken) throws NotValidTokenException{
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new NotValidTokenException("Invalid refresh token");
        }
        String userId = jwtUtil.getUserId(refreshToken);
        String role = jwtUtil.getRole(refreshToken);
        String newAccessToken = jwtUtil.generate(userId, role, "ACCESS");
        return new AuthResponse(newAccessToken, refreshToken);
    }
}
