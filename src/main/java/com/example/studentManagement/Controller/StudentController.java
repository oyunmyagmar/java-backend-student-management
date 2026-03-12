package com.example.studentManagement.Controller;

import com.example.studentManagement.Dto.Request.StudentRequest;
import com.example.studentManagement.Dto.Response.StudentResponse;
import com.example.studentManagement.Entity.Student;
import com.example.studentManagement.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Validated
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/recent")
    public ResponseEntity<List<Student>> getRecentStudents() {
        return ResponseEntity.ok(studentService.getRecentStudents());
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


    private final String UPLOAD_DIR = "C:/Users/Artvision/Documents/uploads/";

    @PostMapping("/students/{id}/upload-avatar")
    public ResponseEntity<?> uploadAvatar(@PathVariable String id, @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Файл сонгоогүй байна.");
        }

        try {
            // 1. Хавтас үүсгэх
            File directory = new File(UPLOAD_DIR);
            if (!directory.exists()) {
                directory.mkdirs(); // mkdirs() нь бүх шатны хавтаснуудыг үүсгэдэг
            }

            // 2. Файлын нэр (id_timestamp.extension)
            String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
            String fileName = id + "_" + System.currentTimeMillis() + "." + extension;
            Path path = Paths.get(UPLOAD_DIR + fileName);

            // 3. Файлыг хадгалах
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // 4. URL-ийг DB-д хадгалах (Бүрэн хаягаар нь)
            String fileUrl = "http://localhost:8086/uploads/" + fileName;
            studentService.updateStudentAvatar(id, fileUrl);

            return ResponseEntity.ok(Map.of(
                    "url", fileUrl,
                    "message", "Зураг амжилттай солигдлоо"
            ));
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Сервер дээр файл хадгалахад алдаа гарлаа: " + e.getMessage());
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
