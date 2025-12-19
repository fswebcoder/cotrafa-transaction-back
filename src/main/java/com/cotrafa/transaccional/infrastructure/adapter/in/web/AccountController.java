package com.cotrafa.transaccional.infrastructure.adapter.in.web;

import com.cotrafa.transaccional.domain.model.Account;
import com.cotrafa.transaccional.domain.model.PagedResult;
import com.cotrafa.transaccional.domain.model.TransactionFilter;
import com.cotrafa.transaccional.domain.ports.in.DepositUseCase;
import com.cotrafa.transaccional.domain.ports.in.GetUserAccountsUseCase;
import com.cotrafa.transaccional.domain.ports.in.GetUserTransactionsUseCase;
import com.cotrafa.transaccional.domain.ports.in.TransferUseCase;
import com.cotrafa.transaccional.infrastructure.adapter.in.web.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cotrafa.transaccional.domain.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final DepositUseCase depositUseCase;
    private final GetUserAccountsUseCase getUserAccountsUseCase;
    private final TransferUseCase transferUseCase;
    private final GetUserTransactionsUseCase getUserTransactionsUseCase;

    @PostMapping("/deposit")
    public ResponseEntity<ApiResponse<DepositUseCase.DepositRequest>> deposit(
            @RequestBody DepositUseCase.DepositRequest request) {
        depositUseCase.deposit(request);
        return ResponseEntity.ok(ApiResponse.success(request, "Deposit successful"));
    }

    @PostMapping("/transfer")
    public ResponseEntity<ApiResponse<TransferUseCase.TransferRequest>> transfer(
            @RequestBody TransferUseCase.TransferRequest request) {
        transferUseCase.transfer(request);
        return ResponseEntity.ok(ApiResponse.success(request, "Transfer successful"));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<Account>>> getUserAccounts(@PathVariable Long userId) {
        List<Account> accounts = getUserAccountsUseCase.getUserAccounts(userId);
        return ResponseEntity.ok(ApiResponse.success(accounts, "Accounts retrieved successfully"));
    }

    @GetMapping("/user/{userId}/transactions")
    public ResponseEntity<ApiResponse<PagedResult<Transaction>>> getUserTransactions(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) BigDecimal minAmount,
            @RequestParam(required = false) BigDecimal maxAmount,
            @RequestParam(required = false) String transactionType) {

        TransactionFilter filter = TransactionFilter.builder()
                .startDate(startDate)
                .endDate(endDate)
                .minAmount(minAmount)
                .maxAmount(maxAmount)
                .transactionType(transactionType)
                .build();

        PagedResult<Transaction> transactions = getUserTransactionsUseCase.getUserTransactions(userId, page, size,
                filter);
        return ResponseEntity.ok(ApiResponse.success(transactions, "Transactions retrieved successfully"));
    }
}
