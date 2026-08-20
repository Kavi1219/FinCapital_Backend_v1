package com.fincapital.repository;

import com.fincapital.entity.MoneyTransaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MoneyTransactionRepository
        extends JpaRepository<
        MoneyTransaction,
        Long
        > {

    long countByCompany_Id(
            Long companyId
    );

    List<MoneyTransaction>
    findByCompany_IdAndBranch_IdAndTransactionDateBetweenOrderByTransactionDateDesc(
            Long companyId,
            Long branchId,
            LocalDateTime from,
            LocalDateTime to
    );

    // =========================================================
    // CUSTOMER PAYMENT HISTORY
    // =========================================================

    List<MoneyTransaction>
    findByCustomer_IdOrderByTransactionDateDesc(
            Long customerId
    );

    // =========================================================
    // NEXT TRANSACTION CODE
    // =========================================================

    Optional<MoneyTransaction>
    findTopByCompany_IdOrderByTransactionCodeDesc(
            Long companyId
    );
}