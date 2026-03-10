package com.example.studentManagement.Repository;

import com.example.studentManagement.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    
    boolean existsByEmail(String email);

    User findByEmail(String email);

}
