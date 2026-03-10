package com.example.studentManagement.Controller;

import com.example.studentManagement.Dto.Request.LoginRequest;
import com.example.studentManagement.Dto.Request.UserRequest;
import com.example.studentManagement.Dto.Response.UserResponse;
import com.example.studentManagement.Entity.User;
import com.example.studentManagement.Service.LoginService;
import com.example.studentManagement.Service.TestService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    @Autowired
    TestService testService;
    @Autowired
    private LoginService loginService;

    @PostMapping("/send-code")
    public ResponseEntity<?> sendCode(@RequestBody UserRequest request) {
        try {
            UserResponse result = testService.createUser(request);
            return ResponseEntity.ok(Map.of("result", true, "data", result.getMessage()));
//            return ResponseEntity.ok("Баталгаажуулах код амжилттай илгээгдлээ." + result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("result", false, "message", e.getMessage()));
//            return ResponseEntity.internalServerError().body("Алдаа гарлаа: " + e.getMessage());
        }
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activate(@RequestBody Map<String, String> body) {
        String email = body.get("username");
        String code = body.get("code");

        boolean isActivated = testService.activateUser(email, code);
        if (isActivated) {
            return ResponseEntity.ok(Map.of("result", true, "data", "Амжилттай идэвхжлээ."));
        } else {
            return ResponseEntity.badRequest().body(Map.of("result", false, "message", "Буруу код байна."));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            UserResponse response = loginService.login(loginRequest);
            return ResponseEntity.ok(Map.of(
                    "result", true,
                    "data", response
            ));
//            User user = loginService.login(loginRequest);
//
//            if (user != null) {
//                user.setPassword(null);
//                return ResponseEntity.ok(user);
//            } else {
//                return ResponseEntity.status(401).body("Имэйл эсвэл нууц үг буруу байна.");
//            }
        } catch (Exception e) {
//            return ResponseEntity.internalServerError().body("Сервер дээр алдаа гарлаа: " + e.getMessage());
// Алдаа гарвал (нууц үг буруу эсвэл идэвхжээгүй бол)
            return ResponseEntity.status(401).body(Map.of(
                    "result", false,
                    "message", e.getMessage()
            ));
        }

    }
}