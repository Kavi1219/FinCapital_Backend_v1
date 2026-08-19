package com.fincapital.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ReportSummaryResponse(

        LocalDate from,
        LocalDate to,

        BigDecimal totalIncoming,
        BigDecimal totalOutgoing,
        BigDecimal netCashFlow,

        BigDecimal totalCollections,
        BigDecimal totalFineReceived,
        BigDecimal totalPrecloseReceived,
        BigDecimal totalPrincipalReturned,

        BigDecimal totalLoanDisbursement,
        BigDecimal totalExpenses,

        BigDecimal dailyCollections,
        BigDecimal weeklyCollections,
        BigDecimal monthlyCollections,

        long transactionCount,

        List<TransactionResponse> transactions
) {}