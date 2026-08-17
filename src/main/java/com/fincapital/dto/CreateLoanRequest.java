package com.fincapital.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateLoanRequest(@NotNull Long companyId, @NotNull Long branchId, @NotNull Long customerId,
                                @NotNull @DecimalMin("0.01") BigDecimal loanAmount, @NotBlank String cycle,
                                @NotBlank String loanType, @NotNull @DecimalMin("0.00") BigDecimal interestRate,
                                Integer duration, @NotNull LocalDate startDate, Boolean fineEnabled,
                                BigDecimal fineAmount, Long createdByAgentId, Long createdByUserId) {
}
