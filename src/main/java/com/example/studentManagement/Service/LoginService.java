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
import com.example.studentManagement.Utils.JwtUtils; // 1. Импорт нэмэх


@Service
public class LoginService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    public UserResponse login(LoginRequest loginRequest) {
        if (loginRequest.getEmail() == null || loginRequest.getEmail().isEmpty()) {
            throw new RuntimeException("Имэйл хаяг хоосон байж болохгүй.");
        }


        if (loginRequest.getPassword() == null || loginRequest.getPassword().isEmpty()) {
            throw new RuntimeException("Нууц үг хоосон байж болохгүй.");
        }

        if (!userRepository.existsByEmail(loginRequest.getEmail())) {
            System.out.println("Бүртгэлгүй хэрэглэгч байна.");
            throw new UsernameNotFoundException("Бүртгэлгүй хэрэглэгч байна.");
        }

        User user = userRepository.findByEmail(loginRequest.getEmail());


        if (user == null || !passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Нууц үг буруу байна.");
        }

        if (user.getStatus() == UserStatus.UNACTIVE) {
            throw new RuntimeException("Таны бүртгэл хараахан идэвхжээгүй байна. Имэйлээ баталгаажуулна уу.");
        }

        String token = jwtUtils.generateToken(user.getEmail());

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .token(token)
                .message("Амжилттай нэвтэрлээ.")
                .build();
    }
}
//  System.out.println("request init : " + request.getEmail());
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