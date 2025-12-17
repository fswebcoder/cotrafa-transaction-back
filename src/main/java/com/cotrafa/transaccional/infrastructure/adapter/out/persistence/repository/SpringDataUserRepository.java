package com.cotrafa.transaccional.infrastructure.adapter.out.persistence.repository;

import com.cotrafa.transaccional.infrastructure.adapter.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpringDataUserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByDocumentNumber(String documentNumber);
}
