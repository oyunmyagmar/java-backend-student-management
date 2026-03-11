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

    public List<Student> getRecentStudents() {
        try {
            return studentRepository.findAllByIsDeletedFalseOrderByIdDesc();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
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

    public void deleteStudent(String id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Оюутан олдсонгүй!"));

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


}

