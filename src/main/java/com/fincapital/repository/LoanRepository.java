package com.fincapital.repository;

import com.fincapital.entity.Loan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository
        extends JpaRepository<Loan, Long> {

    List<Loan>
    findByCompany_IdAndBranch_IdOrderByIdDesc(
            Long companyId,
            Long branchId
    );

    // =========================================================
    // CUSTOMER PROFILE
    // =========================================================

    List<Loan>
    findByCustomer_IdOrderByIdDesc(
            Long customerId
    );

    long countByCompany_Id(
            Long companyId
    );

    long countByCompany_IdAndBranch_IdAndStatus(
            Long companyId,
            Long branchId,
            String status
    );
}