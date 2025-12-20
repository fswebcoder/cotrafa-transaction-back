package com.cotrafa.transaccional.infrastructure.adapter.out.persistence;

import com.cotrafa.transaccional.domain.model.PagedResult;
import com.cotrafa.transaccional.domain.model.TransactionFilter;
import com.cotrafa.transaccional.infrastructure.adapter.out.persistence.specification.TransactionSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.cotrafa.transaccional.domain.model.Account;
import com.cotrafa.transaccional.domain.model.Transaction;
import com.cotrafa.transaccional.domain.model.User;
import com.cotrafa.transaccional.domain.ports.out.LoadAccountPort;
import com.cotrafa.transaccional.domain.ports.out.LoadTransactionPort;
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
public class AccountPersistenceAdapter
        implements LoadAccountPort, UpdateAccountPort, SaveTransactionPort, LoadTransactionPort {

    private final SpringDataAccountRepository accountRepository;
    private final SpringDataTransactionRepository transactionRepository;

    @Override
    public PagedResult<Transaction> loadTransactionsByUserId(Long userId, int page, int size,
            TransactionFilter filter) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TransactionEntity> transactionPage = transactionRepository.findAll(
                TransactionSpecification.withFilter(userId, filter), pageable);

        List<Transaction> content = transactionPage.getContent().stream()
                .map(entity -> {
                    Transaction transaction = mapToTransactionDomain(entity);
                    transaction.setMovement(calculateMovement(entity, userId));
                    return transaction;
                })
                .collect(Collectors.toList());

        return PagedResult.<Transaction>builder()
                .content(content)
                .pageNumber(transactionPage.getNumber())
                .pageSize(transactionPage.getSize())
                .totalElements(transactionPage.getTotalElements())
                .totalPages(transactionPage.getTotalPages())
                .last(transactionPage.isLast())
                .first(transactionPage.isFirst())
                .build();
    }

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
                .user(User.builder().id(entity.getUser().getId()).build())
                .build();
    }

    private AccountEntity mapToEntity(Account account) {
        return AccountEntity.builder()
                .id(account.getId())
                .accountNumber(account.getAccountNumber())
                .alias(account.getAlias())
                .balance(account.getBalance())
                .user(UserEntity.builder().id(account.getUser().getId()).build())
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

    private String calculateMovement(TransactionEntity entity, Long userId) {
        boolean userIsSource = entity.getSourceAccount() != null
                && entity.getSourceAccount().getUser() != null
                && userId.equals(entity.getSourceAccount().getUser().getId());
        boolean userIsDestination = entity.getDestinationAccount() != null
                && entity.getDestinationAccount().getUser() != null
                && userId.equals(entity.getDestinationAccount().getUser().getId());

        if (userIsSource && userIsDestination) {
            return "INTERNA";
        }
        if (userIsSource) {
            return "SALIDA";
        }
        if (userIsDestination) {
            return "ENTRADA";
        }
        return null;
    }
}
