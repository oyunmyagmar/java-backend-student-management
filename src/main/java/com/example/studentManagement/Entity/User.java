package com.example.studentManagement.Entity;

import com.example.studentManagement.enums.UserStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
@Data
public class User {
    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    private String firstName;
    private String lastName;

    @Indexed(unique = true)
    private String email;

    private String password;
    private String activationCode;
    private UserStatus status;
    private Boolean isDeleted = false;
}
