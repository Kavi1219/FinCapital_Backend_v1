package com.fincapital.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateLoanRequest(@NotNull Long companyId, @NotNull Long branchId, @NotNull Long customerId,
                                @NotNull @DecimalMin("0.01") BigDecimal loanAmount, @NotBlank String cycle,
                                String loanType, @NotNull @DecimalMin("0.00") BigDecimal interestRate,
                                @NotNull @Min(1) Integer duration, @NotNull LocalDate startDate,
                                Boolean interestTakenUpfront, Boolean fineEnabled, BigDecimal fineAmount,
                                Long createdByAgentId, Long createdByUserId) {}
