package com.example.studentManagement.Controller;

import com.example.studentManagement.Dto.Request.StudentRequest;
import com.example.studentManagement.Dto.Response.StudentResponse;
import com.example.studentManagement.Entity.Student;
import com.example.studentManagement.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Validated
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/recent")
    public ResponseEntity<List<Student>> getRecentStudents(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String status) {
        return ResponseEntity.ok(studentService.getFilteredStudents(search, status));
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable String id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/counts")
    public ResponseEntity<?> getStudentCount() {
        try {
            long total = studentService.getTotalStudentCount();
            long active = studentService.getActiveStudentCount();
            long inactive = studentService.getInactiveStudentCount();
            return ResponseEntity.ok(Map.of(
                    "total", total,
                    "active", active,
                    "inactive", inactive
            ));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PostMapping("/create-student")
    public ResponseEntity<?> createStudent(@RequestBody StudentRequest request) {
        StudentResponse result = studentService.createStudent(request);

        return ResponseEntity.ok(Map.of(
                "result", true,
                "message", result.getMessage(), // "Амжилттай бүртгэгдлээ"
                "data", result
        ));
    }

    @PostMapping("/students/{id}/upload-avatar")
    public ResponseEntity<?> uploadAvatar(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        // Энгийн шалгалтууд контроллер дээр үлдэж болно
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("result", false, "message", "Файл сонгоогүй байна."));
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest().body(Map.of("result", false, "message", "Зөвхөн зураг оруулна уу."));
        }

        try {
            // Бүх хүнд логикийг Service-ээс дуудаж байна
            String fileUrl = studentService.uploadAndSaveAvatar(id, file);

            return ResponseEntity.ok(Map.of(
                    "result", true,
                    "url", fileUrl,
                    "message", "Аватар амжилттай шинэчлэгдлээ"
            ));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("result", false, "message", "Файл хадгалахад алдаа гарлаа: " + e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(Map.of("result", false, "message", e.getMessage()));
        }
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

    @PutMapping("/update-student/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable String id, @RequestBody StudentRequest request) {
        try {
            StudentResponse result = studentService.updateStudent(id, request);

            return ResponseEntity.ok(Map.of(
                    "result", true,
                    "message", result.getMessage(),
                    "data", result
            ));
        } catch (RuntimeException e) {
            // Оюутан олдохгүй эсвэл бусад логик алдаа гарвал
            return ResponseEntity.status(404).body(Map.of(
                    "result", false,
                    "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                    "result", false,
                    "message", "Сервер талд алдаа гарлаа"
            ));
        }
    }


}
