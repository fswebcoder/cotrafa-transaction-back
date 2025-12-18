package com.cotrafa.transaccional.domain.ports.in;

import com.cotrafa.transaccional.domain.model.User;
import java.util.List;

public interface GetAllUsersUseCase {
    List<User> getAllUsers();
}
