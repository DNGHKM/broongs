package com.broongs.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Member {
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

    protected Member() {
    }

}
