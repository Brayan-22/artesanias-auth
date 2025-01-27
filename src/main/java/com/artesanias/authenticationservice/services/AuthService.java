package com.artesanias.authenticationservice.services;

import com.artesanias.authenticationservice.dto.AuthRequest;
import com.artesanias.authenticationservice.dto.AuthResponse;
import com.artesanias.authenticationservice.dto.UserVo;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    public AuthResponse register(AuthRequest request){
        //realiza la validacion si el usuario existe en db
        request.setPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
        UserVo registeredUser = restTemplate.postForObject("http://user-service/users", request, UserVo.class);
        String accessToken = jwtUtil.generate(registeredUser.getId(), registeredUser.getRole(), "ACCESS");
        String refreshToken = jwtUtil.generate(registeredUser.getId(), registeredUser.getRole(), "REFRESH");
        return new AuthResponse(accessToken,refreshToken);
    }
}
