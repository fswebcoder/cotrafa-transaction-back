package com.cotrafa.transaccional.domain.ports.in;

import com.cotrafa.transaccional.domain.model.Transaction;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

public interface TransferUseCase {
    Transaction transfer(TransferRequest request);

    @Data
    @Builder
    public static class TransferRequest {
        @NotBlank
        private String sourceAccountNumber;

        @NotBlank
        private String destinationAccountNumber;

        @NotNull
        @Positive
        private BigDecimal amount;
        private String cus;
    }
}
