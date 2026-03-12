package com.example.studentManagement.Entity;

import com.example.studentManagement.enums.StudentStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "students")
@Data
public class Student {
    @Id
    private String id;

    @Indexed(unique = true)
    private String username;

    private String firstName;
    private String lastName;

    @Indexed(unique = true)
    private String email;

    private StudentStatus status;
    private Boolean isDeleted = false;
    
    private String avatarUrl;

}
