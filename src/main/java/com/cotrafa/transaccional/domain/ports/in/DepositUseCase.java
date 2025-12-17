package com.cotrafa.transaccional.domain.ports.in;

import com.cotrafa.transaccional.domain.model.Transaction;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

public interface DepositUseCase {
    Transaction deposit(DepositRequest request);

    @Data
    @Builder
    class DepositRequest {
        private String accountNumber;
        private BigDecimal amount;
    }
}
