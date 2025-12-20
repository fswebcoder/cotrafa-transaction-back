package com.cotrafa.transaccional.domain.model;

import com.cotrafa.transaccional.domain.model.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    private Long id;
    private Account sourceAccount;
    private Account destinationAccount;
    private String movement;
    private BigDecimal amount;
    private String cus;
    private TransactionStatus status;
    private LocalDateTime timestamp;
}
