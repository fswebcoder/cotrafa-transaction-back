package com.cotrafa.transaccional.infrastructure.adapter.out.persistence.repository;

import com.cotrafa.transaccional.infrastructure.adapter.out.persistence.entity.TransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataTransactionRepository extends JpaRepository<TransactionEntity, Long>, JpaSpecificationExecutor<TransactionEntity> {
    List<TransactionEntity> findBySourceAccountIdOrDestinationAccountId(Long sourceAccountId, Long destinationAccountId);

    @org.springframework.data.jpa.repository.Query("SELECT t FROM TransactionEntity t " +
            "WHERE t.sourceAccount.user.id = :userId OR t.destinationAccount.user.id = :userId " +
            "ORDER BY t.timestamp DESC")
    List<TransactionEntity> findByUserId(@org.springframework.data.repository.query.Param("userId") Long userId);
}
