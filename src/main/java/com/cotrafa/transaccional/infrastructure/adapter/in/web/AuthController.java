package com.cotrafa.transaccional.infrastructure.adapter.in.web;

import com.cotrafa.transaccional.domain.ports.in.LoginUseCase;
import com.cotrafa.transaccional.infrastructure.adapter.in.web.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginUseCase loginUseCase;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginUseCase.LoginResponse>> login(@RequestBody LoginUseCase.LoginRequest request) {
        LoginUseCase.LoginResponse response = loginUseCase.login(request);
        return ResponseEntity.ok(ApiResponse.success(response, "Login successful"));
    }
}
