package com.cotrafa.transaccional.domain.ports.in;

import com.cotrafa.transaccional.domain.model.Transaction;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

public interface TransferUseCase {
    Transaction transfer(TransferRequest request);

    @Data
    @Builder
    public static class TransferRequest {
        private String sourceAccountNumber;
        private String destinationAccountNumber;
        private BigDecimal amount;
        private String cus;
    }
}
