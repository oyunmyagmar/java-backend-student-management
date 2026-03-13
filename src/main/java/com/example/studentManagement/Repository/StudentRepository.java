package com.example.studentManagement.Repository;

import com.example.studentManagement.Entity.Student;
import com.example.studentManagement.enums.StudentStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
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

    // Шүүлтүүрт зориулсан шинэ Query
    @Query("{ 'isDeleted': false, " +
            "  $and: [ " +
            "    { $or: [ { 'status': ?1 }, { $expr: { $eq: [?1, null] } } ] }, " + // Статус ирвэл шүүнэ, үгүй бол алгасна
            "    { $or: [ " +
            "      { 'firstName': { $regex: ?0, $options: 'i' } }, " + // Case-insensitive хайлт
            "      { 'lastName': { $regex: ?0, $options: 'i' } }, " +
            "      { 'email': { $regex: ?0, $options: 'i' } } " +
            "    ] } " +
            "  ] " +
            "}")
    List<Student> findByFilters(String search, String status);
}