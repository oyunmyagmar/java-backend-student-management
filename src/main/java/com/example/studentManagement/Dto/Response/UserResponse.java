package com.example.studentManagement.Dto.Response;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponse {
    private String id;
    private String username;
    private String token;
    private String message;
    private Boolean isDeleted;
}
