package com.example.studentManagement.Dto.Response;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponse {
    private String message;
    private String id;
    private String username;
    private String token;
}
