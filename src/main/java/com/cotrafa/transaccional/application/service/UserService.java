package com.cotrafa.transaccional.application.service;

import com.cotrafa.transaccional.domain.model.User;
import com.cotrafa.transaccional.domain.ports.in.GetAllUsersUseCase;
import com.cotrafa.transaccional.domain.ports.out.LoadUserPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements GetAllUsersUseCase {

    private final LoadUserPort loadUserPort;

    @Override
    public List<User> getAllUsers() {
        return loadUserPort.loadAllUsers();
    }
}
