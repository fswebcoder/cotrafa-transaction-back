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
                    .name("Fabio ")
                    .lastName("Sánchez Sánchez")
                    .documentType(DocumentType.CC)
                    .documentNumber("10278896896")
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .build();
            User savedUser = loadUserPort.saveUser(user);
            System.out.println("Admin user created: admin / admin123");

            // Create a default account for admin
            createAccount("10000001", "copeahorros", new BigDecimal("1000000"), savedUser);
        }

        createUser("Mateo", "Arenas", "1001", "mateo", "mateo123", "20000001", "Ahorros Mateo", new BigDecimal("500000"));
        createUser("Andres", "Castro", "1002", "andres", "andres123", "20000002", "Nomina Andres", new BigDecimal("1500000"));
        createUser("Kelly", "Embale", "1003", "kelly", "kelly123", "20000003", "Vacaciones Kelly", new BigDecimal("3000000"));
        createUser("Rafael", "Quintana", "1004", "rafael", "rafael123", "20000004", "Inversion Rafael", new BigDecimal("750000"));
    }

    private void createUser(String name, String lastName, String docNumber, String username, String password, 
                            String accNumber, String accAlias, BigDecimal balance) {
        if (loadUserPort.loadUserByUsername(username).isEmpty()) {
            User user = User.builder()
                    .name(name)
                    .lastName(lastName)
                    .documentType(DocumentType.CC)
                    .documentNumber(docNumber)
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .build();
            User savedUser = loadUserPort.saveUser(user);
            System.out.println("User created: " + username);
            
            createAccount(accNumber, accAlias, balance, savedUser);
        }
    }

    private void createAccount(String number, String alias, BigDecimal balance, User user) {
        Account account = Account.builder()
                .accountNumber(number)
                .alias(alias)
                .balance(balance)
                .user(user)
                .build();
        updateAccountPort.updateAccount(account);
        System.out.println("Account created: " + number);
    }
}
