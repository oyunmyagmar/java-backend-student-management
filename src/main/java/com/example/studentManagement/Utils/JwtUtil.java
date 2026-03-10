package com.example.studentManagement.Utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    // Туршилтын нууц түлхүүр (Бодит төсөлд 256-битээс багагүй урт байх ёстой)
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final int expirationMs = 86400000; // 24 цаг

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }
}