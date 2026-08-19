package com.fincapital.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public record CreatePaymentRequest(@NotNull Long companyId, @NotNull Long branchId, @NotNull Long customerId,
                                   @NotNull Long loanId, Long scheduleId, String paymentType,
                                   @NotNull @DecimalMin("0.00") BigDecimal paymentAmount, BigDecimal finePaid,
                                   LocalDate paymentDate, String paymentMethod, String notes, Long collectedByAgentId,
                                   Long collectedByUserId) {}
