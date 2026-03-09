package com.example.studentManagement.Dto.Request;


import com.fasterxml.jackson.annotation.JsonAnyGetter;
//import lombok.Data;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.validation.annotation.Validated;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRequest {
    @NotNull
    private String username;

    @NotNull
    private String firstName;

    private String lastName;
    private String email;
    private String password;
}
