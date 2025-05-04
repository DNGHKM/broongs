package com.broongs.dto.auth;

import lombok.Getter;

@Getter
public class SignUpRequestDTO {
    private String email;
    private String password;
    private String phone;
    private String nickname;
}
