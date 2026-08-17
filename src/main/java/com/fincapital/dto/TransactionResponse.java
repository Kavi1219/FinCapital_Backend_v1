package com.fincapital.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionResponse(Long id, String transactionCode, String transactionType, Long customerId,
                                  String customerCode, Long loanId, String loanCode, BigDecimal amount,
                                  String direction, String description, LocalDateTime transactionDate) {
}
