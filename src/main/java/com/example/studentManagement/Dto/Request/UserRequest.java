package com.example.studentManagement.Dto.Request;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
//import lombok.Data;
import lombok.*;
import org.springframework.validation.annotation.Validated;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRequest {
    private String username;
    private String email;
}
