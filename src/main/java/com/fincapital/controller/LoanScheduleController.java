package com.fincapital.controller;

import com.fincapital.dto.LoanScheduleResponse;
import com.fincapital.entity.LoanSchedule;
import com.fincapital.exception.NotFoundException;
import com.fincapital.repository.LoanRepository;
import com.fincapital.repository.LoanScheduleRepository;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanScheduleController {

    private final LoanScheduleRepository schedules;
    private final LoanRepository loans;

    public LoanScheduleController(
            LoanScheduleRepository schedules,
            LoanRepository loans
    ) {
        this.schedules = schedules;
        this.loans = loans;
    }

    @GetMapping("/{loanId}/schedules")
    @Transactional(readOnly = true)
    public List<LoanScheduleResponse> getSchedules(
            @PathVariable Long loanId
    ) {

        if (!loans.existsById(loanId)) {
            throw new NotFoundException(
                    "Loan not found"
            );
        }

        return schedules
                .findByLoan_IdOrderByInstallmentNumber(
                        loanId
                )
                .stream()
                .map(this::map)
                .toList();
    }

    private LoanScheduleResponse map(
            LoanSchedule s
    ) {

        BigDecimal dueAmount =
                s.getDueAmount() == null
                        ? BigDecimal.ZERO
                        : s.getDueAmount();

        BigDecimal paidAmount =
                s.getPaidAmount() == null
                        ? BigDecimal.ZERO
                        : s.getPaidAmount();

        BigDecimal pendingAmount =
                dueAmount
                        .subtract(paidAmount)
                        .max(BigDecimal.ZERO);

        BigDecimal fineAmount =
                s.getFineAmount() == null
                        ? BigDecimal.ZERO
                        : s.getFineAmount();

        BigDecimal finePaid =
                s.getFinePaid() == null
                        ? BigDecimal.ZERO
                        : s.getFinePaid();

        return new LoanScheduleResponse(
                s.getId(),
                s.getInstallmentNumber(),
                s.getDueDate(),
                dueAmount,
                paidAmount,
                pendingAmount,
                fineAmount,
                finePaid,
                s.getStatus(),
                s.getPaidDate()
        );
    }
}