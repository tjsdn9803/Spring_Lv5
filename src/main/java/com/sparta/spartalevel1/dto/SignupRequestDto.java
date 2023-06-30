package com.sparta.spartalevel1.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
    @Pattern(regexp = "[a-z0-9]")
    private String username;
    @Pattern(regexp = "[A-z0-9]")
    private String password;

    private String email;
}
