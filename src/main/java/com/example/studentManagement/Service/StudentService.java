package com.example.studentManagement.Service;

import com.example.studentManagement.Dto.Request.StudentRequest;
import com.example.studentManagement.Dto.Response.StudentResponse;
import com.example.studentManagement.Entity.Student;
import com.example.studentManagement.Repository.StudentRepository;
import com.example.studentManagement.enums.StudentStatus;
import org.bson.json.JsonObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.util.JSONPObject;

import java.util.List;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public StudentResponse createStudent(StudentRequest request) {
        if (studentRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Энэ имэйл хаяг бүртгэгдсэн байна.");
        }

        Student student = new Student();
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());
        student.setEmail(request.getEmail());
        student.setStatus(StudentStatus.UNACTIVE);

        Student savedStudent = studentRepository.save(student);

        return StudentResponse.builder()
                .id(savedStudent.getId())
                .firstName(savedStudent.getFirstName())
                .lastName(savedStudent.getLastName())
                .email(savedStudent.getEmail())
                .message("Амжилттай хадгаллаа.")
                .build();
    }

    public List<Student> getRecentStudents() {
        try {
            return studentRepository.findStudentByOrderByIdDesc();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public long getTotalStudentCount() {
        return studentRepository.count();
    }

    public void deleteStudent(String id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Оюутан олдсонгүй!");
        }
    }
}

