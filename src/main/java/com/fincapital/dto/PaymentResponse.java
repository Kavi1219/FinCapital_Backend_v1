package com.fincapital.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(Long id, String paymentCode, Long customerId, String customerCode, Long loanId,
                              String loanCode, Long scheduleId, BigDecimal paymentAmount, BigDecimal finePaid,
                              BigDecimal totalReceived, String paymentMethod, LocalDateTime paymentDate) {
}
