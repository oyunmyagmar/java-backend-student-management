package com.example.studentManagement.Repository;

import com.example.studentManagement.Entity.Student;
import com.example.studentManagement.enums.StudentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    boolean existsByEmail(String email);

    List<Student> findAllByOrderByIdDesc();

    long count();

    long countByStatus(StudentStatus status);
}