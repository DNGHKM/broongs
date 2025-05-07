package com.broongs.controller;

import com.broongs.dto.ApiResponse;
import com.broongs.dto.auth.LoginRequestDTO;
import com.broongs.dto.auth.LoginResponseDTO;
import com.broongs.dto.auth.SignUpRequestDTO;
import com.broongs.dto.auth.SignUpResponseDTO;
import com.broongs.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO, HttpServletRequest httpRequest, HttpServletResponse response) {
        try {
            LoginResponseDTO dto = authService.login(loginRequestDTO, httpRequest, response);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("로그인 성공", dto));
        } catch (Exception e) {
            log.error("error = {}", e.getMessage());
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("로그인 실패, 아이디나 패스워드를 확인하세요"));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@RequestBody SignUpRequestDTO request) {
        try {
            SignUpResponseDTO dto = authService.register(request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(ApiResponse.success("회원가입 완료", dto));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure("회원가입 실패"));
        }
    }
}
