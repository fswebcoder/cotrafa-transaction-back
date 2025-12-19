package com.cotrafa.transaccional.domain.ports.out;

import com.cotrafa.transaccional.domain.model.PagedResult;
import com.cotrafa.transaccional.domain.model.Transaction;
import com.cotrafa.transaccional.domain.model.TransactionFilter;

public interface LoadTransactionPort {
    PagedResult<Transaction> loadTransactionsByUserId(Long userId, int page, int size, TransactionFilter filter);
}
