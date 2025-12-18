package com.cotrafa.transaccional.infrastructure.adapter.out.persistence;

import com.cotrafa.transaccional.domain.model.Account;
import com.cotrafa.transaccional.domain.model.Transaction;
import com.cotrafa.transaccional.domain.model.User;
import com.cotrafa.transaccional.domain.ports.out.LoadAccountPort;
import com.cotrafa.transaccional.domain.ports.out.SaveTransactionPort;
import com.cotrafa.transaccional.domain.ports.out.UpdateAccountPort;
import com.cotrafa.transaccional.infrastructure.adapter.out.persistence.entity.AccountEntity;
import com.cotrafa.transaccional.infrastructure.adapter.out.persistence.entity.TransactionEntity;
import com.cotrafa.transaccional.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.cotrafa.transaccional.infrastructure.adapter.out.persistence.repository.SpringDataAccountRepository;
import com.cotrafa.transaccional.infrastructure.adapter.out.persistence.repository.SpringDataTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AccountPersistenceAdapter implements LoadAccountPort, UpdateAccountPort, SaveTransactionPort {

    private final SpringDataAccountRepository accountRepository;
    private final SpringDataTransactionRepository transactionRepository;

    @Override
    public Optional<Account> loadAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).map(this::mapToDomain);
    }

    @Override
    public List<Account> loadAccountsByUserId(Long userId) {
        return accountRepository.findByUserId(userId).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Account updateAccount(Account account) {
        AccountEntity entity = mapToEntity(account);
        AccountEntity saved = accountRepository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        TransactionEntity entity = mapToTransactionEntity(transaction);
        TransactionEntity saved = transactionRepository.save(entity);
        return mapToTransactionDomain(saved);
    }

    private Account mapToDomain(AccountEntity entity) {
        return Account.builder()
                .id(entity.getId())
                .accountNumber(entity.getAccountNumber())
                .alias(entity.getAlias())
                .balance(entity.getBalance())
                .user(User.builder().id(entity.getUser().getId()).build()) // Simplified User
                .build();
    }

    private AccountEntity mapToEntity(Account account) {
        return AccountEntity.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .alias(account.getAlias())
                .balance(account.getBalance())
                .user(UserEntity.builder().id(account.getUser().getId()).build()) // Simplified User reference
                .build();
    }

    private TransactionEntity mapToTransactionEntity(Transaction transaction) {
        return TransactionEntity.builder()
                .id(transaction.getId())
                .sourceAccount(
                        transaction.getSourceAccount() != null ? mapToEntity(transaction.getSourceAccount()) : null)
                .destinationAccount(mapToEntity(transaction.getDestinationAccount()))
                .amount(transaction.getAmount())
                .cus(transaction.getCus())
                .status(transaction.getStatus())
                .timestamp(transaction.getTimestamp())
                .build();
    }

    private Transaction mapToTransactionDomain(TransactionEntity entity) {
        return Transaction.builder()
                .id(entity.getId())
                .sourceAccount(entity.getSourceAccount() != null ? mapToDomain(entity.getSourceAccount()) : null)
                .destinationAccount(mapToDomain(entity.getDestinationAccount()))
                .amount(entity.getAmount())
                .cus(entity.getCus())
                .status(entity.getStatus())
                .timestamp(entity.getTimestamp())
                .build();
    }
}
