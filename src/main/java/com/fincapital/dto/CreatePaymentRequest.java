package com.fincapital.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CreatePaymentRequest(@NotNull Long companyId, @NotNull Long branchId, @NotNull Long customerId,
                                   @NotNull Long loanId, Long scheduleId,
                                   @NotNull @DecimalMin("0.00") BigDecimal paymentAmount, BigDecimal finePaid,
                                   String paymentMethod, String notes, Long collectedByAgentId,
                                   Long collectedByUserId) {
}
