package com.cotrafa.transaccional.domain.ports.out;

import com.cotrafa.transaccional.domain.model.Account;

public interface UpdateAccountPort {
    Account updateAccount(Account account);
}
