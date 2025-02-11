package com.artesanias.authenticationservice.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private String expiration;

    private Key key;

    @PostConstruct
    public void initKey() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public Claims getClaims(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(token).getBody();
    }

    public Date getExpirationDate(String token){
        return getClaims(token).getExpiration();
    }

    public String generate(String userId,String role, String tokenType){
        Map<String,String> claims = Map.of("id",userId,"role",role);
        long expMillis = "ACCESS".equalsIgnoreCase(tokenType)
                ? Long.parseLong(expiration)*1000
                : Long.parseLong(expiration)*1000*5;
        final Date now = new Date();
        final Date exp = new Date(now.getTime()+expMillis);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(claims.get("id"))
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key)
                .compact();
    }
    public String getUserId(String token) {
        return getClaims(token).get("id", String.class);
    }

    public String getRole(String token) {
        return getClaims(token).get("role", String.class);
    }
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return !isExpired(token);
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isExpired(String token){
        return getExpirationDate(token).before(new Date());
    }
}
