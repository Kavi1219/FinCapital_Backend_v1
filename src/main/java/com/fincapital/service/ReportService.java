package com.fincapital.service;

import com.fincapital.dto.ReportSummaryResponse;
import com.fincapital.dto.TransactionResponse;
import com.fincapital.entity.MoneyTransaction;
import com.fincapital.repository.MoneyTransactionRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    private final MoneyTransactionRepository repo;

    public ReportService(
            MoneyTransactionRepository r
    ) {
        repo = r;
    }

    // =========================================================
    // OVERALL TRANSACTION REPORT
    // =========================================================

    @Transactional(readOnly = true)
    public List<TransactionResponse> overall(
            Long companyId,
            Long branchId,
            LocalDate from,
            LocalDate to
    ) {

        return getTransactions(
                companyId,
                branchId,
                from,
                to
        )
                .stream()
                .map(this::map)
                .toList();
    }

    // =========================================================
    // REPORT SUMMARY
    // =========================================================

    @Transactional(readOnly = true)
    public ReportSummaryResponse summary(
            Long companyId,
            Long branchId,
            LocalDate from,
            LocalDate to
    ) {

        List<MoneyTransaction> transactions =
                getTransactions(
                        companyId,
                        branchId,
                        from,
                        to
                );

        // =====================================================
        // TOTAL INCOMING
        // =====================================================

        BigDecimal totalIncoming =
                transactions
                        .stream()
                        .filter(
                                x ->
                                        "IN".equals(
                                                x.getDirection()
                                        )
                        )
                        .map(
                                x ->
                                        safe(
                                                x.getAmount()
                                        )
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        // =====================================================
        // TOTAL OUTGOING
        // =====================================================

        BigDecimal totalOutgoing =
                transactions
                        .stream()
                        .filter(
                                x ->
                                        "OUT".equals(
                                                x.getDirection()
                                        )
                        )
                        .map(
                                x ->
                                        safe(
                                                x.getAmount()
                                        )
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        // =====================================================
        // COLLECTIONS
        // =====================================================

        BigDecimal totalCollections =
                sumType(
                        transactions,
                        "COLLECTION"
                );

        // =====================================================
        // FINE
        // =====================================================

        BigDecimal totalFineReceived =
                sumType(
                        transactions,
                        "FINE"
                );

        // =====================================================
        // PRECLOSE
        // =====================================================

        BigDecimal totalPrecloseReceived =
                sumType(
                        transactions,
                        "PRECLOSE"
                );

        // =====================================================
        // PRINCIPAL RETURN
        // =====================================================

        BigDecimal totalPrincipalReturned =
                sumType(
                        transactions,
                        "PRINCIPAL_RETURN"
                );

        // =====================================================
        // LOAN DISBURSEMENTS
        // =====================================================

        BigDecimal totalLoanDisbursement =
                sumType(
                        transactions,
                        "LOAN_DISBURSEMENT"
                );

        // =====================================================
        // EXPENSES
        // =====================================================

        BigDecimal totalExpenses =
                sumType(
                        transactions,
                        "EXPENSE"
                );

        // =====================================================
        // DAILY COLLECTIONS
        // =====================================================

        BigDecimal dailyCollections =
                sumCollectionCycle(
                        transactions,
                        "DAILY"
                );

        // =====================================================
        // WEEKLY COLLECTIONS
        // =====================================================

        BigDecimal weeklyCollections =
                sumCollectionCycle(
                        transactions,
                        "WEEKLY"
                );

        // =====================================================
        // MONTHLY COLLECTIONS
        // =====================================================

        BigDecimal monthlyCollections =
                sumCollectionCycle(
                        transactions,
                        "MONTHLY"
                );

        // =====================================================
        // NET CASH FLOW
        // =====================================================

        BigDecimal netCashFlow =
                totalIncoming.subtract(
                        totalOutgoing
                );

        List<TransactionResponse> transactionResponses =
                transactions
                        .stream()
                        .map(this::map)
                        .toList();

        return new ReportSummaryResponse(

                from,
                to,

                totalIncoming,
                totalOutgoing,
                netCashFlow,

                totalCollections,
                totalFineReceived,
                totalPrecloseReceived,
                totalPrincipalReturned,

                totalLoanDisbursement,
                totalExpenses,

                dailyCollections,
                weeklyCollections,
                monthlyCollections,

                transactions.size(),

                transactionResponses
        );
    }

    // =========================================================
    // LOAD TRANSACTIONS
    // =========================================================

    private List<MoneyTransaction> getTransactions(
            Long companyId,
            Long branchId,
            LocalDate from,
            LocalDate to
    ) {

        if (from == null || to == null) {
            throw new IllegalArgumentException(
                    "From date and to date are required"
            );
        }

        if (to.isBefore(from)) {
            throw new IllegalArgumentException(
                    "To date cannot be before from date"
            );
        }

        return repo
                .findByCompany_IdAndBranch_IdAndTransactionDateBetweenOrderByTransactionDateDesc(
                        companyId,
                        branchId,
                        from.atStartOfDay(),
                        to.plusDays(1)
                                .atStartOfDay()
                                .minusNanos(1)
                );
    }

    // =========================================================
    // SUM BY TRANSACTION TYPE
    // =========================================================

    private BigDecimal sumType(
            List<MoneyTransaction> transactions,
            String type
    ) {

        return transactions
                .stream()
                .filter(
                        x ->
                                type.equals(
                                        x.getTransactionType()
                                )
                )
                .map(
                        x ->
                                safe(
                                        x.getAmount()
                                )
                )
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    // =========================================================
    // SUM COLLECTION BY LOAN CYCLE
    // =========================================================

    private BigDecimal sumCollectionCycle(
            List<MoneyTransaction> transactions,
            String cycle
    ) {

        return transactions
                .stream()
                .filter(
                        x ->
                                "COLLECTION".equals(
                                        x.getTransactionType()
                                )
                )
                .filter(
                        x ->
                                x.getLoan() != null
                )
                .filter(
                        x ->
                                cycle.equals(
                                        x.getLoan()
                                                .getCycle()
                                )
                )
                .map(
                        x ->
                                safe(
                                        x.getAmount()
                                )
                )
                .reduce(
                        BigDecimal.ZERO,
                        BigDecimal::add
                );
    }

    // =========================================================
    // SAFE BIGDECIMAL
    // =========================================================

    private BigDecimal safe(
            BigDecimal value
    ) {

        return value == null
                ? BigDecimal.ZERO
                : value;
    }

    // =========================================================
    // ENTITY -> RESPONSE
    // =========================================================

    private TransactionResponse map(
            MoneyTransaction x
    ) {

        return new TransactionResponse(

                x.getId(),

                x.getTransactionCode(),

                x.getTransactionType(),

                x.getCustomer() == null
                        ? null
                        : x.getCustomer()
                        .getId(),

                x.getCustomer() == null
                        ? null
                        : x.getCustomer()
                        .getCustomerCode(),

                x.getLoan() == null
                        ? null
                        : x.getLoan()
                        .getId(),

                x.getLoan() == null
                        ? null
                        : x.getLoan()
                        .getLoanCode(),

                x.getAmount(),

                x.getDirection(),

                x.getDescription(),

                x.getTransactionDate()
        );
    }
}