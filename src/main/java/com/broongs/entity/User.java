package com.broongs.entity;

import com.broongs.dto.auth.SignUpRequestDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true, length = 320)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 32)
    private String nickname;

    @Column(nullable = false, length = 30)
    private String phone;

    @Column(length = 255)
    private String provider;

    @Column(length = 255)
    private String providerId;

    @Column(nullable = false)
    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    protected User() {
    }

    public static User singUp(SignUpRequestDTO dto, PasswordEncoder encoder) {
        return User.builder()
                .email(dto.getEmail())
                .password(encoder.encode(dto.getPassword()))
                .nickname(dto.getNickname())
                .phone(dto.getPhone())
                .createAt(LocalDateTime.now())
                .updateAt(LocalDateTime.now())
                .build();
    }
}
