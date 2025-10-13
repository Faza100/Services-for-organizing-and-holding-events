package com.example.my_progect.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.my_progect.model.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtTokenService {

    private final Long tokenLifeTime;

    private final SecretKeySpec signKey;

    private static final Logger log = LoggerFactory.getLogger(JwtTokenService.class);

    public JwtTokenService(
            @Value("${jwt.lifetime}") Long tokenLifeTime,
            @Value("${jwt.secret-key}") String signKey) {
        this.tokenLifeTime = tokenLifeTime;
        this.signKey = new SecretKeySpec(
                signKey.getBytes(StandardCharsets.UTF_8),
                SignatureAlgorithm.HS256.getJcaName());
    }

    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", user.getRole().name());
        Date issuedTime = new Date();
        Date expiredTime = new Date(issuedTime.getTime() + tokenLifeTime);
        return Jwts.builder()
                .claims(claims)
                .subject(user.getLogin())
                .issuedAt(issuedTime)
                .expiration(expiredTime)
                .signWith(signKey)
                .compact();
    }

    public boolean isTokenValid(String jwtToken) {
        try {
            Jwts.parser()
                    .setSigningKey(signKey)
                    .build()
                    .parse(jwtToken);
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    public String getLoginFromToken(String jwtToken) {
        return Jwts.parser()
                .setSigningKey(signKey)
                .build()
                .parseClaimsJws(jwtToken)
                .getPayload()
                .getSubject();
    }

    public String getRoleFromToken(String jwtToken) {
        var role = Jwts.parser()
                .setSigningKey(signKey)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody()
                .get("role", String.class);
        log.info("Extracted role from token: {}", role);
        return role;
    }

}
