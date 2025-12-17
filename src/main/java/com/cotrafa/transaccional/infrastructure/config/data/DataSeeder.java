package com.cotrafa.transaccional.infrastructure.config.data;

import com.cotrafa.transaccional.domain.model.Account;
import com.cotrafa.transaccional.domain.model.User;
import com.cotrafa.transaccional.domain.model.enums.DocumentType;
import com.cotrafa.transaccional.domain.ports.out.LoadUserPort;
import com.cotrafa.transaccional.domain.ports.out.UpdateAccountPort;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final LoadUserPort loadUserPort;
    private final UpdateAccountPort updateAccountPort;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (loadUserPort.loadUserByUsername("admin").isEmpty()) {
            User user = User.builder()
                    .name("Admin")
                    .lastName("User")
                    .documentType(DocumentType.CC)
                    .documentNumber("1234567890")
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .build();
            User savedUser = loadUserPort.saveUser(user);
            System.out.println("Admin user created: admin / admin123");

            // Create a default account for admin
            Account account = Account.builder()
                    .accountNumber("10000001")
                    .alias("Cuenta Principal")
                    .balance(BigDecimal.ZERO)
                    .user(savedUser)
                    .build();
            updateAccountPort.updateAccount(account);
            System.out.println("Default account created: 10000001");
        }
    }
}
