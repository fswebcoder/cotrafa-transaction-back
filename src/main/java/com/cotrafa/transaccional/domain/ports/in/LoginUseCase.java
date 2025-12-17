package com.cotrafa.transaccional.domain.ports.in;

import com.cotrafa.transaccional.domain.model.User;
import lombok.Builder;
import lombok.Data;

public interface LoginUseCase {
    LoginResponse login(LoginRequest request);

    @Data
    @Builder
    class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    @Builder
    class LoginResponse {
        private String token;
        private User user;
    }
}
