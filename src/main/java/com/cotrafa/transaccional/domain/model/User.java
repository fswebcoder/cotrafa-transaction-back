package com.cotrafa.transaccional.domain.model;

import com.cotrafa.transaccional.domain.model.enums.DocumentType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private Long id;
    private String name;
    private String lastName;
    private DocumentType documentType;
    private String documentNumber;
    @JsonIgnore
    private String username;
    @JsonIgnore
    private String password;
    private List<Account> accounts;
}
