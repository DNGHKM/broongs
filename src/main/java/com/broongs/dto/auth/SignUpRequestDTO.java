package com.broongs.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

@Getter
public class SignUpRequestDTO {
    @NotNull
    private String email;
    @NotNull
    private String password;
    @NotNull
    @Length(min = 11, max = 11)
    private String phone;
    @NotNull
    private String nickname;
}
