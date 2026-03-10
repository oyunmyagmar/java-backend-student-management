package com.example.studentManagement.Controller;

import com.example.studentManagement.Dto.Request.StudentRequest;
import com.example.studentManagement.Dto.Request.UserRequest;
import com.example.studentManagement.Dto.Response.StudentResponse;
import com.example.studentManagement.Entity.Student;
import com.example.studentManagement.Service.LoginService;
import com.example.studentManagement.Service.StudentService;
import com.example.studentManagement.Service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Validated
public class StudentController {

    @Autowired
    private StudentService studentService;

    @PostMapping("/create-student")
    public ResponseEntity<?> createStudent(@RequestBody StudentRequest request) {
        StudentResponse result = studentService.createStudent(request);

        return ResponseEntity.ok(Map.of(
                "result", true,
                "message", result.getMessage(), // "Амжилттай бүртгэгдлээ"
                "data", result
        ));
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Student>> getRecentStudents() {
        return ResponseEntity.ok(studentService.getRecentStudents());
    }

    @GetMapping("/count")
    public ResponseEntity<Long> getStudentCount() {
        long count = studentService.getTotalStudentCount();
        return ResponseEntity.ok(count);
    }

    @DeleteMapping("/delete-student/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable String id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.ok().body("Амжилттай устгагдлаа");
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

}
