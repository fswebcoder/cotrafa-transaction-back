package com.cotrafa.transaccional.infrastructure.adapter.out.persistence.repository;

import com.cotrafa.transaccional.infrastructure.adapter.out.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataTransactionRepository extends JpaRepository<TransactionEntity, Long> {
    List<TransactionEntity> findBySourceAccountIdOrDestinationAccountId(Long sourceAccountId, Long destinationAccountId);
}
