package com.example.deepstacktravel.auth.service;

import com.example.deepstacktravel.common.security.JwtTokenProvider;
import com.example.deepstacktravel.auth.dto.JwtAuthenticationResponse;
import com.example.deepstacktravel.auth.dto.LoginRequest;
import com.example.deepstacktravel.auth.dto.SignUpRequest;
import com.example.deepstacktravel.user.entity.User;
import com.example.deepstacktravel.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public void signup(SignUpRequest signUpRequest) {
        if (userRepository.findByUsername(signUpRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken!");
        }
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already taken!");
        }


        User user = signUpRequest.toEntity(passwordEncoder.encode(signUpRequest.getPassword()));
        userRepository.save(user);
    }

    public JwtAuthenticationResponse login(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        String accessToken = jwtTokenProvider.createToken(authentication);

        return new JwtAuthenticationResponse(accessToken);
    }
}
