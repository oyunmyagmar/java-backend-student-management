package com.example.studentManagement.Service;

import com.example.studentManagement.Dto.Request.LoginRequest;
import com.example.studentManagement.Entity.User;
import com.example.studentManagement.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // SecurityConfig-оос орж ирнэ

    public User login(LoginRequest request) {
        // DB-ээс хэрэглэгчийг имэйлээр хайх (NextAuth-аас username талбарт имэйл ирж байгаа)
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Оруулсан нууц үгийг DB-д байгаа шифрлэгдсэн нууц үгтэй харьцуулах
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return user; // Нууц үг таарвал User объектыг буцаана
            }
        }

        // Хэрэв олдохгүй эсвэл нууц үг буруу бол null эсвэл Exception шиднэ
        return null;
    }

}
