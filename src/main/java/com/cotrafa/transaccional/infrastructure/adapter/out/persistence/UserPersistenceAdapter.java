package com.cotrafa.transaccional.infrastructure.adapter.out.persistence;

import com.cotrafa.transaccional.domain.model.Account;
import com.cotrafa.transaccional.domain.model.User;
import com.cotrafa.transaccional.domain.ports.out.LoadUserPort;
import com.cotrafa.transaccional.infrastructure.adapter.out.persistence.entity.AccountEntity;
import com.cotrafa.transaccional.infrastructure.adapter.out.persistence.entity.UserEntity;
import com.cotrafa.transaccional.infrastructure.adapter.out.persistence.repository.SpringDataUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements LoadUserPort {

    private final SpringDataUserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Optional<User> loadUserByUsername(String username) {
        return userRepository.findByUsername(username).map(this::mapToDomain);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> loadAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public User saveUser(User user) {
        UserEntity entity = mapToEntity(user);
        UserEntity saved = userRepository.save(entity);
        return mapToDomain(saved);
    }

    private User mapToDomain(UserEntity entity) {
        return User.builder()
                .id(entity.getId())
                .name(entity.getName())
                .lastName(entity.getLastName())
                .documentType(entity.getDocumentType())
                .documentNumber(entity.getDocumentNumber())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .accounts(entity.getAccounts() != null ? entity.getAccounts().stream()
                        .map(this::mapAccountToDomain)
                        .collect(Collectors.toList())
                        : Collections.emptyList())
                .build();
    }

    private Account mapAccountToDomain(AccountEntity entity) {
        return Account.builder()
                .id(entity.getId())
                .accountNumber(entity.getAccountNumber())
                .alias(entity.getAlias())
                .balance(entity.getBalance())
                .build();
    }

    private UserEntity mapToEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .documentType(user.getDocumentType())
                .documentNumber(user.getDocumentNumber())
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}
