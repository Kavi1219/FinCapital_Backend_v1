package com.fincapital.service;

import com.fincapital.entity.*;
import com.fincapital.repository.MoneyTransactionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class TransactionService {

    private final MoneyTransactionRepository repo;

    private final CodeService codes;

    public TransactionService(
            MoneyTransactionRepository r,
            CodeService c
    ) {
        repo = r;
        codes = c;
    }

    // =========================================================
    // CREATE MONEY TRANSACTION
    // =========================================================

    @Transactional
    public synchronized MoneyTransaction create(
            Company company,
            Branch branch,
            String type,
            Long ref,
            Customer customer,
            Loan loan,
            BigDecimal amount,
            String direction,
            String description,
            Agent agent,
            AppUser user
    ) {

        // =====================================================
        // FIND LAST VISIBLE TRANSACTION CODE
        // =====================================================

        long lastNumber =
                repo
                        .findTopByCompany_IdOrderByTransactionCodeDesc(
                                company.getId()
                        )
                        .map(
                                transaction ->
                                        codes.transactionNumber(
                                                transaction
                                                        .getTransactionCode()
                                        )
                        )
                        .orElse(0L);

        long nextNumber =
                lastNumber + 1;

        String transactionCode =
                codes.transactionCode(
                        company.getCompanyCode(),
                        nextNumber
                );

        // =====================================================
        // CREATE TRANSACTION
        // =====================================================

        MoneyTransaction t =
                new MoneyTransaction();

        t.setCompany(
                company
        );

        t.setBranch(
                branch
        );

        t.setTransactionCode(
                transactionCode
        );

        t.setTransactionType(
                type
        );

        t.setReferenceId(
                ref
        );

        t.setCustomer(
                customer
        );

        t.setLoan(
                loan
        );

        t.setAmount(
                amount
        );

        t.setDirection(
                direction
        );

        t.setDescription(
                description
        );

        t.setCreatedByAgent(
                agent
        );

        t.setCreatedByUser(
                user
        );

        return repo.save(
                t
        );
    }
}