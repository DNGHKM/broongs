package com.broongs.controller;

import com.broongs.dto.auth.LoginRequestDTO;
import com.broongs.dto.auth.SignUpRequestDTO;
import com.broongs.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletRequest httpRequest, HttpServletResponse response) {
        String sessionId = authService.login(loginRequestDTO, httpRequest, response);
        return ResponseEntity.ok(Map.of(
                "message", "Login successful",
                "sessionId", sessionId
        ));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequestDTO request) {
        try {
            authService.register(request);
            return ResponseEntity.ok("회원가입 완료");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
