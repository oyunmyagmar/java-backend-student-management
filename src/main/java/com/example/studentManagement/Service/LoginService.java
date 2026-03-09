package com.example.studentManagement.Service;

import com.example.studentManagement.Dto.Request.LoginRequest;
import com.example.studentManagement.Entity.User;
import com.example.studentManagement.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        try {
            System.out.println("request init : " + request.getEmail());
            if (request.getEmail() == null) {
                throw new NullPointerException("Email hooson baij bolohgui");
            }
            // DB-ээс хэрэглэгчийг имэйлээр хайх (NextAuth-аас username талбарт имэйл ирж байгаа)
            if (!userRepository.existsByEmail(request.getEmail())) {
                System.out.println("user not found");
                throw new UsernameNotFoundException("User not register");
            }

            User userDetail = userRepository.findByEmail(request.getEmail());

            if (userDetail != null) {
                // Оруулсан нууц үгийг DB-д байгаа шифрлэгдсэн нууц үгтэй харьцуулах
                if (passwordEncoder.matches(request.getPassword(), userDetail.getPassword())) {
                    return userDetail; // Нууц үг таарвал User объектыг буцаана
                }
            }

            return userDetail;
        } catch (Exception e) {
            throw e;
        }
    }
}
