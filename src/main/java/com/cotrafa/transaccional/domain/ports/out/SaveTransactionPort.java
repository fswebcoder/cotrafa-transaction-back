package com.cotrafa.transaccional.domain.ports.out;

import com.cotrafa.transaccional.domain.model.Transaction;

public interface SaveTransactionPort {
    Transaction saveTransaction(Transaction transaction);
}
