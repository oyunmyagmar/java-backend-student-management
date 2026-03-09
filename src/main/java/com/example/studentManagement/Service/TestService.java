package com.example.studentManagement.Service;

import com.example.studentManagement.Dto.Request.UserRequest;
import com.example.studentManagement.Dto.Response.UserResponse;
import com.example.studentManagement.Entity.User;
import com.example.studentManagement.Repository.UserRepository;
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
    private PasswordEncoder passwordEncoder; // SecurityConfig-оос орж ирнэ

    public UserResponse createTestUser(UserRequest request) {
        try {
            if (userRepository.existsByEmail(request.getEmail())) {
                System.out.println("User ");
                throw new UsernameNotFoundException("User with username " + request.getUsername() + " not found");
            }

            User user = new User();
            // Frontend-ээс ирсэн мэдээллийг Entity-ийн талбаруудад оноож байна
            // TODO : save to user table
            user.setUsername(request.getEmail());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));

            // 2. Бааз руу хадгалах
            userRepository.save(user);
            UserResponse userResponse = new UserResponse();
            userResponse.setMessage("Амжилттай");
            userResponse.setToken("example bearer token");
            userResponse.setUsername(user.getUsername());
            return userResponse;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}
