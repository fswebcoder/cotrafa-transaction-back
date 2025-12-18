package com.cotrafa.transaccional.domain.ports.out;

import com.cotrafa.transaccional.domain.model.Account;
import java.util.Optional;
import java.util.List;

public interface LoadAccountPort {
    Optional<Account> loadAccountByNumber(String accountNumber);
    List<Account> loadAccountsByUserId(Long userId);
}
