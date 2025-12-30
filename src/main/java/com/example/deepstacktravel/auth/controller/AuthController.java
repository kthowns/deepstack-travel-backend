package com.example.deepstacktravel.auth.controller;

import com.example.deepstacktravel.auth.dto.JwtAuthenticationResponse;
import com.example.deepstacktravel.auth.dto.LoginRequest;
import com.example.deepstacktravel.auth.dto.SignUpRequest;
import com.example.deepstacktravel.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "회원가입", description = "사용자 정보를 입력하여 회원가입을 진행합니다.")
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@Valid @RequestBody SignUpRequest signUpRequest) {
        authService.signup(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "로그인", description = "사용자 이메일과 비밀번호로 로그인을 진행하고 JWT 토큰을 발급받습니다.")
    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        JwtAuthenticationResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }
}
