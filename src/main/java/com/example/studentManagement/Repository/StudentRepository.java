package com.example.studentManagement.Repository;

import com.example.studentManagement.Entity.Student;
import com.example.studentManagement.enums.StudentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {
    boolean existsByEmail(String email);

    List<Student> findAllByIsDeletedFalseOrderByIdDesc();

    long countByIsDeletedFalse();

    long countByStatusAndIsDeletedFalse(StudentStatus status);

    Optional<Student> findByIdAndIsDeletedFalse(String id);
}