package com.cotrafa.transaccional.infrastructure.adapter.out.persistence.repository;

import com.cotrafa.transaccional.infrastructure.adapter.out.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface SpringDataAccountRepository extends JpaRepository<AccountEntity, Long> {
    Optional<AccountEntity> findByAccountNumber(String accountNumber);

    List<AccountEntity> findByUserId(Long userId);
}
