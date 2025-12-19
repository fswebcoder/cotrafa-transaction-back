package com.cotrafa.transaccional.domain.ports.in;

import com.cotrafa.transaccional.domain.model.PagedResult;
import com.cotrafa.transaccional.domain.model.Transaction;
import com.cotrafa.transaccional.domain.model.TransactionFilter;

public interface GetUserTransactionsUseCase {
    PagedResult<Transaction> getUserTransactions(Long userId, int page, int size, TransactionFilter filter);
}
