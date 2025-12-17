package com.cotrafa.transaccional.domain.ports.out;

import com.cotrafa.transaccional.domain.model.User;
import java.util.Optional;

public interface LoadUserPort {
    Optional<User> loadUserByUsername(String username);

    User saveUser(User user);
}
