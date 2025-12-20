package com.cotrafa.transaccional.domain.ports.in;

import com.cotrafa.transaccional.domain.model.Transaction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

public interface DepositUseCase {
    Transaction deposit(DepositRequest request);

    @Data
    @Builder
    class DepositRequest {
        @NotBlank
        private String accountNumber;

        @NotNull
        @Positive
        private BigDecimal amount;
    }
}
