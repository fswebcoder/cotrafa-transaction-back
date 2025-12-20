package com.cotrafa.transaccional.infrastructure.adapter.in.web;

import com.cotrafa.transaccional.domain.ports.in.DepositUseCase;
import com.cotrafa.transaccional.domain.ports.in.GetAllUsersUseCase;
import com.cotrafa.transaccional.domain.ports.in.GetUserAccountsUseCase;
import com.cotrafa.transaccional.domain.ports.in.GetUserTransactionsUseCase;
import com.cotrafa.transaccional.domain.ports.in.LoginUseCase;
import com.cotrafa.transaccional.domain.ports.in.TransferUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityAccessTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LoginUseCase loginUseCase;

    @MockBean
    private GetAllUsersUseCase getAllUsersUseCase;

    @MockBean
    private DepositUseCase depositUseCase;

    @MockBean
    private GetUserAccountsUseCase getUserAccountsUseCase;

    @MockBean
    private TransferUseCase transferUseCase;

    @MockBean
    private GetUserTransactionsUseCase getUserTransactionsUseCase;

    @Test
    void pingIsPublic() throws Exception {
        mockMvc.perform(get("/api/ping"))
                .andExpect(status().isOk());
    }

    @Test
    void protectedEndpointsRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/accounts/user/1"))
                .andExpect(status().isUnauthorized());
    }
}
