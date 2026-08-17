package com.fincapital.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateExpenseRequest(@NotNull Long companyId, @NotNull Long branchId, @NotBlank String purpose,
                                   @NotNull @DecimalMin("0.01") BigDecimal amount, LocalDate expenseDate, String notes,
                                   Long createdByAgentId, Long createdByUserId) {
}
