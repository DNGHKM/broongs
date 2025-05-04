package com.broongs.service;

import com.broongs.dto.auth.LoginRequestDTO;
import com.broongs.dto.auth.SignUpRequestDTO;
import com.broongs.entity.User;
import com.broongs.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SecurityContextRepository securityContextRepository;

    public String login(LoginRequestDTO loginDTO, HttpServletRequest request, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword());

        authToken.setDetails(new WebAuthenticationDetails(request));

        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authentication);

        securityContextRepository.saveContext(securityContext, request, response);

        HttpSession session = request.getSession(true);

        return session.getId();
    }


    public void register(SignUpRequestDTO signUpRequestDTO) {
        if (userRepository.existsByEmail(signUpRequestDTO.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
        User user = User.singUp(signUpRequestDTO, passwordEncoder);
        userRepository.save(user);
    }


//    public void logout(HttpServletResponse response) {
//        ResponseCookie cookie = ResponseCookie.from(COOKIE_NAME, "")
//                .path("/")
//                .maxAge(0)
//                .httpOnly(true)
//                .secure(COOKIE_SECURE) // HTTPS 적용 시 true
//                .build();
//
//        response.addHeader("Set-Cookie", cookie.toString());
//    }
}
