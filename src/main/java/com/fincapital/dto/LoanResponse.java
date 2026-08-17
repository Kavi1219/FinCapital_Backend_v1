package com.fincapital.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanResponse(Long id, String loanCode, Long customerId, String customerCode, String customerName,
                           BigDecimal loanAmount, String cycle, String loanType, BigDecimal interestRate,
                           BigDecimal interestAmount, BigDecimal amountGiven, Integer duration,
                           BigDecimal collectionAmount, LocalDate startDate, LocalDate finishDate, Boolean fineEnabled,
                           BigDecimal fineAmount, String status) {
}
