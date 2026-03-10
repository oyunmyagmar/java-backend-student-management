package com.example.studentManagement.Service;

import com.example.studentManagement.Dto.Request.LoginRequest;
import com.example.studentManagement.Dto.Response.UserResponse;
import com.example.studentManagement.Entity.User;
import com.example.studentManagement.Repository.UserRepository;
import com.example.studentManagement.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.studentManagement.Utils.JwtUtil; // 1. Импорт нэмэх

import java.util.Optional;

@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public UserResponse login(LoginRequest loginRequest) {
        try {
            User user = userRepository.findByEmail(loginRequest.getEmail());

            if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                if (user.getStatus() != UserStatus.ACTIVE) {
                    throw new RuntimeException("Бүртгэл идэвхжээгүй байна. Имэйлээ шалгана уу.");
                }

                // JWT үүсгэх (Энд өөрийн JwtUtil-ээ дуудна)
                String token = jwtUtil.generateToken(user.getUsername());

                return UserResponse.builder()
                        .token(token)
                        .username(user.getUsername())
                        .message("Амжилттай нэвтэрлээ")
                        .build();
            }
            throw new RuntimeException("Нэвтрэх нэр эсвэл нууц үг буруу.");
//            System.out.println("request init : " + request.getEmail());
//            if (request.getEmail() == null) {
//                throw new NullPointerException("Email hooson baij bolohgui");
//            }
//            // DB-ээс хэрэглэгчийг имэйлээр хайх (NextAuth-аас username талбарт имэйл ирж байгаа)
//            if (!userRepository.existsByEmail(request.getEmail())) {
//                System.out.println("user not found");
//                throw new UsernameNotFoundException("User not register");
//            }
//
//            User userDetail = userRepository.findByEmail(request.getEmail());
//
//            if (userDetail != null) {
//                // Оруулсан нууц үгийг DB-д байгаа шифрлэгдсэн нууц үгтэй харьцуулах
//                if (passwordEncoder.matches(request.getPassword(), userDetail.getPassword())) {
//                    return userDetail; // Нууц үг таарвал User объектыг буцаана
//                }
//            }
//
//            return userDetail;
        } catch (Exception e) {
            throw e;
        }
    }
}
