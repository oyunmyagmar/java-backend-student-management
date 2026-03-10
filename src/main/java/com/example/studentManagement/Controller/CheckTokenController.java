package com.example.studentManagement.Controller;

import com.example.studentManagement.Utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class CheckTokenController {

    @Autowired
    private JwtUtils jwtUtils;

    @GetMapping("/check-token")
    public ResponseEntity<?> checkToken(@RequestHeader("Authorization") String authHeader) {
        System.out.println(">>> CHECK TOKEN API CALLED: " + LocalDateTime.now()); // Энийг нэм
        // 1. Header-ээс "Bearer " хэсгийг салгаж авах
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Token missing");
        }

        String token = authHeader.substring(7);

        try {
            // 2. Токен дотроос username-ийг гаргаж авах
            String username = jwtUtils.extractUsername(token);

            // 3. Таны бичсэн isTokenValid-ийг ашиглаж шалгах
            if (jwtUtils.isTokenValid(token, username)) {
                return ResponseEntity.ok().body(Map.of("valid", true, "username", username));
            } else {
                return ResponseEntity.status(401).body(Map.of("valid", false, "message", "Token expired"));
            }
        } catch (Exception e) {
            // Токен эвдэрсэн эсвэл буруу байвал
            return ResponseEntity.status(401).body(Map.of("valid", false, "message", "Invalid token"));
        }
    }
}