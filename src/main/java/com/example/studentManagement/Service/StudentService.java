package com.example.studentManagement.Service;

import com.example.studentManagement.Dto.Request.StudentRequest;
import com.example.studentManagement.Dto.Response.StudentResponse;
import com.example.studentManagement.Entity.Student;
import com.example.studentManagement.Repository.StudentRepository;
import com.example.studentManagement.enums.StudentStatus;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.util.JSONPObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getRecentStudents() {
        try {
            return studentRepository.findAllByIsDeletedFalseOrderByIdDesc();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public StudentResponse getStudentById(String id) {
        return studentRepository.findByIdAndIsDeletedFalse(id)
                .map(student -> StudentResponse.builder()
                        .id(student.getId()).firstName(student.getFirstName())
                        .lastName(student.getLastName()).email(student.getEmail())
                        .status(student.getStatus().name())
                        .avatarUrl(student.getAvatarUrl())
                        .build()).orElseThrow(() -> new RuntimeException("Оюутан олдсонгүй!"));

    }

    public long getTotalStudentCount() {
        return studentRepository.countByIsDeletedFalse();
    }

    public long getActiveStudentCount() {
        return studentRepository.countByStatusAndIsDeletedFalse(StudentStatus.ACTIVE);
    }

    public long getInactiveStudentCount() {
        return studentRepository.countByStatusAndIsDeletedFalse(StudentStatus.INACTIVE);
    }

    public StudentResponse createStudent(StudentRequest request) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Энэ имэйл хаяг бүртгэгдсэн байна.");
        }

        Student student = new Student();
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setStatus(StudentStatus.INACTIVE);

        Student savedStudent = studentRepository.save(student);

        return StudentResponse.builder()
                .id(savedStudent.getId())
                .firstName(savedStudent.getFirstName())
                .lastName(savedStudent.getLastName())
                .email(savedStudent.getEmail())
                .message("Амжилттай хадгаллаа.")
                .build();
    }


    public void deleteStudent(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Оюутан олдсонгүй!"));

        student.setEmail(student.getEmail() + "_deleted_" + System.currentTimeMillis());
        student.setIsDeleted(true);
        studentRepository.save(student);
    }

    public StudentResponse updateStudent(String id, StudentRequest request) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Оюутан олдсонгүй!"));

        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());

        if (!student.getEmail().equals(request.getEmail()) && studentRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Энэ имэйл хаяг аль хэдийн бүртгэгдсэн байна.");
        }
        student.setEmail(request.getEmail());

        if (request.getStatus() != null) {
            try {
                student.setStatus(StudentStatus.valueOf(request.getStatus().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Буруу статус ирлээ: " + request.getStatus());
            }
        }

        Student updatedStudent = studentRepository.save(student);

        return StudentResponse.builder()
                .id(updatedStudent.getId())
                .firstName(updatedStudent.getFirstName())
                .lastName(updatedStudent.getLastName())
                .email(updatedStudent.getEmail())
                .status(updatedStudent.getStatus().name())
                .message("Амжилттай шинэчлэгдлээ.")
                .build();
    }

    public String uploadAndSaveAvatar(String id, MultipartFile file) throws IOException {
        // 1. Файл хадгалах байршил тохируулах
        String uploadDir = "C:/upload/";
        File folder = new File(uploadDir);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        // 2. Файлын нэрийг үүсгэх
        String filename = System.currentTimeMillis() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
        Path path = Paths.get(uploadDir + filename);
        // 3. Файлыг диск дээр хадгалах
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        // 4. DB-д хадгалах URL замыг бэлдэх
        String fileUrl = "/upload/" + filename;
        // 5. DB-д хадгалах логик
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Оюутан олдсонгүй: " + id));

        student.setAvatarUrl(fileUrl);
        studentRepository.save(student);
        return fileUrl; // Frontend-д хэрэгтэй учир URL-ыг буцаана
    }


    public List<Student> getFilteredStudents(String search, String status) {
        String searchPattern = (search == null) ? "" : search;

        // Хэрэв status "ALL" эсвэл хоосон байвал null болгож баазад мэдэгдэнэ
        String statusParam = (status == null || status.equals("ALL") || status.isEmpty()) ? null : status;

        return studentRepository.findByFilters(searchPattern, statusParam);
    }

}

