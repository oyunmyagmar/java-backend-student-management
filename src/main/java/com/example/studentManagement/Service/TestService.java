package com.example.studentManagement.Service;

import com.example.studentManagement.Dto.Request.UserRequest;
import com.example.studentManagement.Entity.User;
import com.example.studentManagement.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // SecurityConfig-оос орж ирнэ

    public String createTestUser(UserRequest request) {
//        if (userRepository.existsByEmail(request.getEmail())) {
//            return "Алдаа: Энэ имэйл аль хэдийн бүртгэгдсэн байна!";
//        }
        if (request != null) {
            User user = new User();
            // Frontend-ээс ирсэн мэдээллийг Entity-ийн талбаруудад оноож байна
            // TODO : save to user table
            user.setUsername(request.getUsername());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            // 2. Бааз руу хадгалах
            userRepository.save(user);
            return "Хэрэглэгч амжилттай хадгалагдлаа: " + request.getUsername();
        } else {
            return "Request body хоосон байж болохгүй";
        }
    }

}
