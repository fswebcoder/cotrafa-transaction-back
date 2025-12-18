package com.cotrafa.transaccional.domain.ports.out;

import com.cotrafa.transaccional.domain.model.User;
import java.util.List;
import java.util.Optional;

public interface LoadUserPort {
    Optional<User> loadUserByUsername(String username);

    List<User> loadAllUsers();

    User saveUser(User user);
}
