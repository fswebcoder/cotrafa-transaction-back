package com.cotrafa.transaccional.infrastructure.adapter.out.persistence.specification;

import com.cotrafa.transaccional.domain.model.TransactionFilter;
import com.cotrafa.transaccional.infrastructure.adapter.out.persistence.entity.AccountEntity;
import com.cotrafa.transaccional.infrastructure.adapter.out.persistence.entity.TransactionEntity;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification {

    public static Specification<TransactionEntity> withFilter(Long userId, TransactionFilter filter) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // User filter (source or destination account belongs to user)
            Join<TransactionEntity, AccountEntity> sourceAccount = root.join("sourceAccount", JoinType.LEFT);
            Join<TransactionEntity, AccountEntity> destinationAccount = root.join("destinationAccount", JoinType.LEFT);

            Predicate userIsSource = cb.equal(sourceAccount.get("user").get("id"), userId);
            Predicate userIsDestination = cb.equal(destinationAccount.get("user").get("id"), userId);

            if (filter.getTransactionType() != null && !filter.getTransactionType().isEmpty()) {
                if ("SENT".equalsIgnoreCase(filter.getTransactionType())) {
                    predicates.add(userIsSource);
                } else if ("RECEIVED".equalsIgnoreCase(filter.getTransactionType())) {
                    predicates.add(userIsDestination);
                } else {
                    predicates.add(cb.or(userIsSource, userIsDestination));
                }
            } else {
                predicates.add(cb.or(userIsSource, userIsDestination));
            }

            // Date range
            if (filter.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), filter.getStartDate().atStartOfDay()));
            }
            if (filter.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("timestamp"), filter.getEndDate().atTime(23, 59, 59)));
            }

            // Amount range
            if (filter.getMinAmount() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("amount"), filter.getMinAmount()));
            }
            if (filter.getMaxAmount() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("amount"), filter.getMaxAmount()));
            }

            query.orderBy(cb.desc(root.get("timestamp")));

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
