package com.example.deepstacktravel.auth.dto;

import com.example.deepstacktravel.user.entity.User;
import com.example.deepstacktravel.user.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequest {

    @NotBlank
    @Size(min = 4, max = 20)
    private String username;

    @NotBlank
    @Size(min = 8, max = 20)
    private String password;

    @NotBlank
    @Size(max = 20)
    private String name;

    @NotBlank
    @Email
    @Size(max = 50)
    private String email;

    public User toEntity(String encodedPassword) {
        return User.builder()
                .username(username)
                .password(encodedPassword)
                .name(name)
                .email(email)
                .role(UserRole.USER)
                .build();
    }
}
