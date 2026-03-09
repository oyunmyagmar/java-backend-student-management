package com.example.studentManagement.Controller;

import com.example.studentManagement.Dto.Request.UserRequest;
import com.example.studentManagement.Service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

//@Secured("ROLE_MANAGE_INVOICE")
@RestController
@RequestMapping("/api/basic")
@Validated
public class TestController {

    @Autowired
    TestService testService;

    @GetMapping()
    public ResponseEntity<?> test() {
        try {
            return ResponseEntity.ok("test shu");
        } catch (Exception e) {
            throw e;
        }
    }

    @PostMapping("create")
    public ResponseEntity<?> test(@RequestBody UserRequest request) {
        try {
            if (request != null) {
                String response = testService.createTestUser(request);
                // TODO : save to user table
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body("Error occured");
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
