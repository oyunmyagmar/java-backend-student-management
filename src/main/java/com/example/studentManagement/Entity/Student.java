package com.example.studentManagement.Entity;

import com.example.studentManagement.enums.StudentStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "students")
@Data
public class Student {
    @Id
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private StudentStatus status;
}
