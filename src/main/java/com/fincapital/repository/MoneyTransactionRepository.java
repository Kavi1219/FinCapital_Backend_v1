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
    // GET HIGHEST VISIBLE TRANSACTION CODE FOR COMPANY
    //
    // Because transaction code uses fixed 8-digit numbering,
    // text sorting works correctly:
    //
    // SFCTXN-00000008
    // SFCTXN-00000009
    // SFCTXN-00000010
    // =========================================================

    Optional<MoneyTransaction>
    findTopByCompany_IdOrderByTransactionCodeDesc(
            Long companyId
    );
}