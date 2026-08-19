package com.fincapital.dto;

import java.math.BigDecimal;
import java.time.*;

public record LoanResponse(Long id, String loanCode, Long customerId, String customerCode, String customerName,
                           BigDecimal loanAmount, String cycle, String loanType, BigDecimal interestRate,
                           BigDecimal interestAmount, Boolean interestTakenUpfront, BigDecimal amountGiven,
                           BigDecimal totalRepayment, Integer duration, String durationUnit, BigDecimal collectionAmount,
                           BigDecimal collectedAmount, BigDecimal principalPending, BigDecimal pendingDue,
                           BigDecimal fineDue, BigDecimal finePaidTotal, LocalDate startDate, LocalDate finishDate,
                           Boolean fineEnabled, BigDecimal fineAmount, String status, BigDecimal precloseAmount,
                           LocalDateTime preclosedAt, LocalDateTime closedAt, LocalDateTime createdAt) {}
