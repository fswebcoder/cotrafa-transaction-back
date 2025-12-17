package com.cotrafa.transaccional.application.service;

import com.cotrafa.transaccional.domain.model.User;
import com.cotrafa.transaccional.domain.ports.in.LoginUseCase;
import com.cotrafa.transaccional.domain.ports.out.LoadUserPort;
import com.cotrafa.transaccional.infrastructure.config.security.JwtService;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class AuthService implements LoginUseCase {

    private final LoadUserPort loadUserPort;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {
        User user = loadUserPort.loadUserByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        org.springframework.security.core.userdetails.User userDetails = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>());

        String token = jwtService.generateToken(userDetails);

        return LoginResponse.builder()
                .token(token)
                .user(user)
                .build();
    }
}
