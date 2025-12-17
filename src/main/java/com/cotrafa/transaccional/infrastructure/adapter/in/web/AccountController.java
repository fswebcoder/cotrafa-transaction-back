package com.cotrafa.transaccional.infrastructure.adapter.in.web;

import com.cotrafa.transaccional.domain.ports.in.DepositUseCase;
import com.cotrafa.transaccional.infrastructure.adapter.in.web.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final DepositUseCase depositUseCase;

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<DepositUseCase.DepositRequest>> deposit(@RequestBody DepositUseCase.DepositRequest request) {
        depositUseCase.deposit(request);
        return ResponseEntity.ok(ApiResponse.success(request, "Deposit successful"));
    }
}
