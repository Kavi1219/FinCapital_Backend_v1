package com.fincapital.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExpenseResponse(Long id, String expenseCode, String purpose, BigDecimal amount, LocalDate expenseDate,
                              String notes) {
}
