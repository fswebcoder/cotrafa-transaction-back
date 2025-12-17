package com.cotrafa.transaccional.application.service;

import com.cotrafa.transaccional.domain.model.Account;
import com.cotrafa.transaccional.domain.model.Transaction;
import com.cotrafa.transaccional.domain.model.enums.TransactionStatus;
import com.cotrafa.transaccional.domain.ports.in.DepositUseCase;
import com.cotrafa.transaccional.domain.ports.out.LoadAccountPort;
import com.cotrafa.transaccional.domain.ports.out.SaveTransactionPort;
import com.cotrafa.transaccional.domain.ports.out.UpdateAccountPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AccountService implements DepositUseCase {

    private final LoadAccountPort loadAccountPort;
    private final UpdateAccountPort updateAccountPort;
    private final SaveTransactionPort saveTransactionPort;

    @Override
    @Transactional
    public Transaction deposit(DepositRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be greater than zero");
        }

        Account account = loadAccountPort.loadAccountByNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Update balance
        account.setBalance(account.getBalance().add(request.getAmount()));
        updateAccountPort.updateAccount(account);

        // Record transaction
        Transaction transaction = Transaction.builder()
                .sourceAccount(null)
                .destinationAccount(account)
                .amount(request.getAmount())
                .status(TransactionStatus.SUCCESS)
                .timestamp(LocalDateTime.now())
                .build();

        return saveTransactionPort.saveTransaction(transaction);
    }
}
