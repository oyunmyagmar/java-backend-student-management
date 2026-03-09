package com.example.studentManagement.Controller;

import com.example.studentManagement.Dto.Request.UserRequest;
import com.example.studentManagement.Service.TestService;
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

    @PostMapping("/send-code")
    public ResponseEntity<?> sendCode(@RequestBody UserRequest request) {
        try {
            // Энд код илгээх логикоо бичнэ (Жишээ нь: testService.sendEmailCode(request))
            String result = testService.createTestUser(request);
            return ResponseEntity.ok("Баталгаажуулах код амжилттай илгээгдлээ." + result);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Алдаа гарлаа: " + e.getMessage());
        }
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activate(@RequestBody Map<String, String> body) {
        try {
            // Frontend-ээс ирсэн JSON-оос утгуудыг салгаж авах
            String code = body.get("code");
            String username = body.get("username");

            // Одоохондоо логик ороогүй тул шууд хариу буцаана
            return ResponseEntity.ok("Бүртгэл амжилттай баталгаажлаа. Хэрэглэгч: " + username);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Идэвхжүүлэхэд алдаа гарлаа.");
        }
    }
}