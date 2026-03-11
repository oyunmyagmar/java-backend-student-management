package com.example.studentManagement.Dto.Response;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class StudentResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String status;
    private String message;
    private Boolean isDeleted;
}
