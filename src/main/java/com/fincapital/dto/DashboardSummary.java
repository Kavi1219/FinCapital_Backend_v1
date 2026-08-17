package com.fincapital.dto;

import java.math.BigDecimal;

public record DashboardSummary(BigDecimal expectedToday, BigDecimal collectedToday, BigDecimal pendingOverdue,
                               BigDecimal upcoming7Days, long activeLoans, BigDecimal todayExpenses) {
}
