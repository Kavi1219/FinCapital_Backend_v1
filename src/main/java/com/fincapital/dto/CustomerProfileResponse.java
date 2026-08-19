package com.fincapital.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CustomerProfileResponse(

        Long id,
        String customerCode,
        String customerName,
        String mobile,
        String fatherName,
        String work,
        String address,
        String profilePhotoUrl,
        String documentPhotoUrl,
        LocalDateTime accountCreatedOn,

        String customerStatus,

        long activeLoanCount,
        long closedLoanCount,

        BigDecimal overallOutstanding,
        BigDecimal overallPending,

        JaminInfo jamin,

        List<DocumentInfo> customerDocuments,

        List<DocumentInfo> jaminDocuments,

        List<LoanInfo> loans,

        List<TransactionInfo> paymentHistory
) {

    // =========================================================
    // JAMIN
    // =========================================================

    public record JaminInfo(

            String name,
            String mobile,
            String fatherName,
            String work,
            String address,
            String profilePhotoUrl,
            String documentPhotoUrl

    ) {}

    // =========================================================
// DOCUMENT
// =========================================================

    public record DocumentInfo(

            Long id,

            String ownerType,

            String documentType,

            String documentName,

            String fileUrl,

            String mimeType,

            LocalDateTime uploadedAt

    ) {}

    // =========================================================
    // LOAN SLOT
    // =========================================================

    public record LoanInfo(

            Long id,

            String loanCode,

            LocalDateTime createdOn,

            String status,

            BigDecimal loanAmount,

            String cycle,

            String cycleDisplay,

            String loanType,

            BigDecimal interestRate,

            BigDecimal interestAmount,

            Boolean interestTakenUpfront,

            BigDecimal amountGiven,

            BigDecimal totalRepayment,

            Integer duration,

            String durationUnit,

            BigDecimal collectionAmount,

            BigDecimal collectedAmount,

            BigDecimal outstanding,

            BigDecimal pending,

            BigDecimal finePending,

            BigDecimal finePaid,

            long outstandingCount,

            long paidCount,

            long pendingCount,

            long finePaidCount,

            long latePaymentCount,

            BigDecimal totalAmountReceived,

            String closeType,

            LocalDateTime closedAt

    ) {}

    // =========================================================
    // CUSTOMER PAYMENT HISTORY
    // =========================================================

    public record TransactionInfo(

            Long id,

            String transactionCode,

            LocalDateTime date,

            Long loanId,

            String loanCode,

            String type,

            String rawType,

            BigDecimal amount,

            String direction,

            String collectedBy,

            String description

    ) {}
}