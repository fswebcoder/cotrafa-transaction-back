package com.cotrafa.transaccional.infrastructure.adapter.in.web;

import com.cotrafa.transaccional.domain.model.Account;
import com.cotrafa.transaccional.domain.ports.in.DepositUseCase;
import com.cotrafa.transaccional.domain.ports.in.GetUserAccountsUseCase;
import com.cotrafa.transaccional.infrastructure.adapter.in.web.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final DepositUseCase depositUseCase;
    private final GetUserAccountsUseCase getUserAccountsUseCase;

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<DepositUseCase.DepositRequest>> deposit(@RequestBody DepositUseCase.DepositRequest request) {
        depositUseCase.deposit(request);
        return ResponseEntity.ok(ApiResponse.success(request, "Deposit successful"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Account>>> getUserAccounts(@PathVariable Long userId) {
        List<Account> accounts = getUserAccountsUseCase.getUserAccounts(userId);
        return ResponseEntity.ok(ApiResponse.success(accounts, "Accounts retrieved successfully"));
    }
}
