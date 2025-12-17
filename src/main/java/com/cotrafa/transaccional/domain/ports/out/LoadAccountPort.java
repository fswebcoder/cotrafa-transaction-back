package com.cotrafa.transaccional.domain.ports.out;

import com.cotrafa.transaccional.domain.model.Account;
import java.util.Optional;

public interface LoadAccountPort {
    Optional<Account> loadAccountByNumber(String accountNumber);
}
