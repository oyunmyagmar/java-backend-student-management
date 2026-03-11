package com.example.studentManagement.Service;

import com.example.studentManagement.Dto.Request.UserRequest;
import com.example.studentManagement.Dto.Response.UserResponse;
import com.example.studentManagement.Entity.User;
import com.example.studentManagement.Repository.UserRepository;
import com.example.studentManagement.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserRequest request) {
        try {
            if (userRepository.existsByEmail(request.getEmail())) {
//                System.out.println("User ");
                throw new RuntimeException("Энэ имэйл аль хэдийн бүртгэгдсэн байна.");
            }

            String code = String.valueOf((int) ((Math.random() * 900000) + 100000));
            System.out.println("DEBUG: Баталгаажуулах код: " + code);

            User user = new User();
            user.setUsername(request.getEmail());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setActivationCode(code);
            user.setStatus(UserStatus.INACTIVE);


            userRepository.save(user);

            return UserResponse.builder()
                    .message("Баталгаажуулах код илгээгдлээ.")
                    .username(user.getUsername())
                    .build();
//            UserResponse userResponse = new UserResponse();
//            userResponse.setMessage("Амжилттай");
//            userResponse.setToken("example bearer token");
//            userResponse.setUsername(user.getUsername());
//            return userResponse;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public boolean activateUser(String email, String code) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getActivationCode().equals(code)) {
            user.setStatus(UserStatus.ACTIVE); // Идэвхжүүлэх
            user.setActivationCode(null);      // Кодыг устгах
            userRepository.save(user);
            return true;
        }
        return false;
    }

}
