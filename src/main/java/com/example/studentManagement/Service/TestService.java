package com.example.studentManagement.Service;

import com.example.studentManagement.Dto.Request.UserRequest;
import com.example.studentManagement.Entity.User;
import com.example.studentManagement.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TestService{

    @Autowired
    private UserRepository userRepository;

    public String createTestUser(UserRequest request) {
        if (request != null) {
            // 1. Request-ээс Entity рүү өгөгдлийг хөрвүүлэх
            // TODO : save to user table
            User user = new User();
            user.setName(request.getUsername());

            // 2. Бааз руу хадгалах
            userRepository.save(user);
            return "Хэрэглэгч амжилттай хадгалагдлаа: " + request.getUsername();
        } else {
            return "Request body хоосон байж болохгүй";
        }
    }

}
