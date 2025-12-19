package com.cotrafa.transaccional.application.service;

import com.cotrafa.transaccional.domain.model.Account;
import com.cotrafa.transaccional.domain.model.Transaction;
import com.cotrafa.transaccional.domain.model.PagedResult;
import com.cotrafa.transaccional.domain.model.TransactionFilter;
import com.cotrafa.transaccional.domain.model.enums.TransactionStatus;
import com.cotrafa.transaccional.domain.ports.in.DepositUseCase;
import com.cotrafa.transaccional.domain.ports.in.GetUserAccountsUseCase;
import com.cotrafa.transaccional.domain.ports.in.GetUserTransactionsUseCase;
import com.cotrafa.transaccional.domain.ports.in.TransferUseCase;
import com.cotrafa.transaccional.domain.ports.out.LoadAccountPort;
import com.cotrafa.transaccional.domain.ports.out.LoadTransactionPort;
import com.cotrafa.transaccional.domain.ports.out.SaveTransactionPort;
import com.cotrafa.transaccional.domain.ports.out.UpdateAccountPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService
        implements DepositUseCase, GetUserAccountsUseCase, TransferUseCase, GetUserTransactionsUseCase {

    private final LoadAccountPort loadAccountPort;
    private final UpdateAccountPort updateAccountPort;
    private final SaveTransactionPort saveTransactionPort;
    private final LoadTransactionPort loadTransactionPort;

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

    @Override
    public List<Account> getUserAccounts(Long userId) {
        return loadAccountPort.loadAccountsByUserId(userId);
    }

    @Override
    @Transactional
    public Transaction transfer(TransferRequest request) {
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be greater than zero");
        }

        if (request.getSourceAccountNumber().equals(request.getDestinationAccountNumber())) {
            throw new IllegalArgumentException("Source and destination accounts cannot be the same");
        }

        Account sourceAccount = loadAccountPort.loadAccountByNumber(request.getSourceAccountNumber())
                .orElseThrow(() -> new RuntimeException("Source account not found"));

        Account destinationAccount = loadAccountPort.loadAccountByNumber(request.getDestinationAccountNumber())
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        if (sourceAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient funds in source account");
        }

        // Debit source account
        sourceAccount.setBalance(sourceAccount.getBalance().subtract(request.getAmount()));
        updateAccountPort.updateAccount(sourceAccount);

        // Credit destination account
        destinationAccount.setBalance(destinationAccount.getBalance().add(request.getAmount()));
        updateAccountPort.updateAccount(destinationAccount);

        // Record transaction
        Transaction transaction = Transaction.builder()
                .sourceAccount(sourceAccount)
                .destinationAccount(destinationAccount)
                .amount(request.getAmount())
                .cus(request.getCus())
                .status(TransactionStatus.SUCCESS)
                .timestamp(LocalDateTime.now())
                .build();

        return saveTransactionPort.saveTransaction(transaction);
    }

    @Override
    public PagedResult<Transaction> getUserTransactions(Long userId, int page, int size, TransactionFilter filter) {
        return loadTransactionPort.loadTransactionsByUserId(userId, page, size, filter);
    }
}
