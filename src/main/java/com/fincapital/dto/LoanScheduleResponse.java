package com.fincapital.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanScheduleResponse(
        Long id,
        Integer installmentNumber,
        LocalDate dueDate,
        BigDecimal dueAmount,
        BigDecimal paidAmount,
        BigDecimal pendingAmount,
        BigDecimal fineAmount,
        BigDecimal finePaid,
        String status,
        LocalDate paidDate
) {}