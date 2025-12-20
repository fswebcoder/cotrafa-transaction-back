package com.cotrafa.transaccional.application.service;

import com.cotrafa.transaccional.domain.model.Account;
import com.cotrafa.transaccional.domain.model.Transaction;
import com.cotrafa.transaccional.domain.model.User;
import com.cotrafa.transaccional.domain.ports.in.DepositUseCase;
import com.cotrafa.transaccional.domain.ports.in.TransferUseCase;
import com.cotrafa.transaccional.domain.ports.out.LoadAccountPort;
import com.cotrafa.transaccional.domain.ports.out.LoadTransactionPort;
import com.cotrafa.transaccional.domain.ports.out.SaveTransactionPort;
import com.cotrafa.transaccional.domain.ports.out.UpdateAccountPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private LoadAccountPort loadAccountPort;

    @Mock
    private UpdateAccountPort updateAccountPort;

    @Mock
    private SaveTransactionPort saveTransactionPort;

    @Mock
    private LoadTransactionPort loadTransactionPort;

    @InjectMocks
    private AccountService accountService;

    @Test
    void depositGeneratesCus() {
        Account account = Account.builder()
                .id(1L)
                .accountNumber("123")
                .alias("Main")
                .balance(new BigDecimal("100.00"))
                .user(User.builder().id(10L).build())
                .build();

        when(loadAccountPort.loadAccountByNumber("123")).thenReturn(Optional.of(account));
        when(updateAccountPort.updateAccount(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(saveTransactionPort.saveTransaction(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        DepositUseCase.DepositRequest request = DepositUseCase.DepositRequest.builder()
                .accountNumber("123")
                .amount(new BigDecimal("50.00"))
                .build();

        accountService.deposit(request);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(saveTransactionPort).saveTransaction(transactionCaptor.capture());
        Transaction savedTransaction = transactionCaptor.getValue();

        assertThat(savedTransaction.getCus()).isNotBlank();
    }

    @Test
    void transferGeneratesCusWhenBlank() {
        Account sourceAccount = Account.builder()
                .id(1L)
                .accountNumber("111")
                .alias("Source")
                .balance(new BigDecimal("100.00"))
                .user(User.builder().id(10L).build())
                .build();

        Account destinationAccount = Account.builder()
                .id(2L)
                .accountNumber("222")
                .alias("Destination")
                .balance(new BigDecimal("10.00"))
                .user(User.builder().id(11L).build())
                .build();

        when(loadAccountPort.loadAccountByNumber("111")).thenReturn(Optional.of(sourceAccount));
        when(loadAccountPort.loadAccountByNumber("222")).thenReturn(Optional.of(destinationAccount));
        when(updateAccountPort.updateAccount(any(Account.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(saveTransactionPort.saveTransaction(any(Transaction.class))).thenAnswer(invocation -> invocation.getArgument(0));

        TransferUseCase.TransferRequest request = TransferUseCase.TransferRequest.builder()
                .sourceAccountNumber("111")
                .destinationAccountNumber("222")
                .amount(new BigDecimal("50.00"))
                .cus(" ")
                .build();

        accountService.transfer(request);

        ArgumentCaptor<Transaction> transactionCaptor = ArgumentCaptor.forClass(Transaction.class);
        verify(saveTransactionPort).saveTransaction(transactionCaptor.capture());
        Transaction savedTransaction = transactionCaptor.getValue();

        assertThat(savedTransaction.getCus()).isNotBlank();
    }
}

