package com.cotrafa.transaccional.domain.ports.in;

import com.cotrafa.transaccional.domain.model.Account;
import java.util.List;

public interface GetUserAccountsUseCase {
    List<Account> getUserAccounts(Long userId);
}
