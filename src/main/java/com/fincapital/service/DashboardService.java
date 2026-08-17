package com.fincapital.service;

import com.fincapital.dto.DashboardSummary;
import com.fincapital.repository.*;
import org.springframework.stereotype.Service;

import java.time.*;

@Service
public class DashboardService {
    private final LoanScheduleRepository schedules;
    private final PaymentRepository payments;
    private final ExpenseRepository expenses;
    private final LoanRepository loans;

    public DashboardService(LoanScheduleRepository a, PaymentRepository b, ExpenseRepository c, LoanRepository d) {
        schedules = a;
        payments = b;
        expenses = c;
        loans = d;
    }

    public DashboardSummary summary(Long companyId, Long branchId) {
        LocalDate t = LocalDate.now();
        return new DashboardSummary(schedules.sumDueForDate(companyId, branchId, t), payments.sumReceivedBetween(companyId, branchId, t.atStartOfDay(), t.plusDays(1).atStartOfDay()), schedules.sumPendingOverdue(companyId, branchId, t), schedules.sumUpcoming(companyId, branchId, t.plusDays(1), t.plusDays(7)), loans.countByCompany_IdAndBranch_IdAndStatus(companyId, branchId, "ACTIVE"), expenses.sumForDate(companyId, branchId, t));
    }
}
