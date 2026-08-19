package com.fincapital.service;

import com.fincapital.dto.*;
import com.fincapital.entity.*;
import com.fincapital.exception.NotFoundException;
import com.fincapital.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository payments;
    private final LoanScheduleRepository schedules;
    private final CompanyRepository companies;
    private final BranchRepository branches;
    private final CustomerRepository customers;
    private final LoanRepository loans;
    private final AgentRepository agents;
    private final AppUserRepository users;
    private final CodeService codes;
    private final TransactionService tx;

    public PaymentService(
            PaymentRepository a,
            LoanScheduleRepository b,
            CompanyRepository c,
            BranchRepository d,
            CustomerRepository e,
            LoanRepository f,
            AgentRepository g,
            AppUserRepository h,
            CodeService i,
            TransactionService j
    ) {
        payments = a;
        schedules = b;
        companies = c;
        branches = d;
        customers = e;
        loans = f;
        agents = g;
        users = h;
        codes = i;
        tx = j;
    }

    // =========================================================
    // CREATE PAYMENT
    // =========================================================

    @Transactional
    public PaymentResponse create(
            CreatePaymentRequest r
    ) {

        Company company = companies
                .findById(r.companyId())
                .orElseThrow(
                        () -> new NotFoundException(
                                "Company not found"
                        )
                );

        Branch branch = branches
                .findById(r.branchId())
                .orElseThrow(
                        () -> new NotFoundException(
                                "Branch not found"
                        )
                );

        Customer customer = customers
                .findById(r.customerId())
                .orElseThrow(
                        () -> new NotFoundException(
                                "Customer not found"
                        )
                );

        Loan loan = loans
                .findById(r.loanId())
                .orElseThrow(
                        () -> new NotFoundException(
                                "Loan not found"
                        )
                );

        if (!"ACTIVE".equals(loan.getStatus())) {
            throw new IllegalArgumentException(
                    "Payments can only be added to active loans"
            );
        }

        LoanSchedule selectedSchedule =
                r.scheduleId() == null
                        ? null
                        : schedules
                        .findById(r.scheduleId())
                        .orElseThrow(
                                () -> new NotFoundException(
                                        "Schedule not found"
                                )
                        );

        // =====================================================
        // VALIDATION
        // =====================================================

        if (r.paymentAmount() == null) {
            throw new IllegalArgumentException(
                    "Payment amount is required"
            );
        }

        if (
                r.paymentAmount()
                        .compareTo(BigDecimal.ZERO) < 0
        ) {
            throw new IllegalArgumentException(
                    "Payment amount cannot be negative"
            );
        }

        if (
                selectedSchedule != null &&
                        !selectedSchedule
                                .getLoan()
                                .getId()
                                .equals(loan.getId())
        ) {
            throw new IllegalArgumentException(
                    "Schedule does not belong to this loan"
            );
        }

        BigDecimal finePaid =
                r.finePaid() == null
                        ? BigDecimal.ZERO
                        : r.finePaid();

        if (
                finePaid.compareTo(
                        BigDecimal.ZERO
                ) < 0
        ) {
            throw new IllegalArgumentException(
                    "Fine amount cannot be negative"
            );
        }

        LocalDate actualPaymentDate =
                r.paymentDate() == null
                        ? LocalDate.now()
                        : r.paymentDate();

        // =====================================================
        // COLLECTED BY
        // =====================================================

        Agent agent =
                r.collectedByAgentId() == null
                        ? null
                        : agents
                        .findById(
                                r.collectedByAgentId()
                        )
                        .orElseThrow(
                                () -> new NotFoundException(
                                        "Agent not found"
                                )
                        );

        AppUser user =
                r.collectedByUserId() == null
                        ? null
                        : users
                        .findById(
                                r.collectedByUserId()
                        )
                        .orElseThrow(
                                () -> new NotFoundException(
                                        "User not found"
                                )
                        );

        // =====================================================
        // CREATE PAYMENT RECORD
        // =====================================================

        Payment payment =
                new Payment();

        payment.setCompany(
                company
        );

        payment.setBranch(
                branch
        );

        payment.setPaymentCode(
                codes.paymentCode(
                        company.getCompanyCode(),

                        payments.countByCompany_Id(
                                company.getId()
                        ) + 1
                )
        );

        payment.setCustomer(
                customer
        );

        payment.setLoan(
                loan
        );

        payment.setSchedule(
                selectedSchedule
        );

        payment.setPaymentAmount(
                r.paymentAmount()
        );

        payment.setFinePaid(
                finePaid
        );

        payment.setPaymentMethod(
                r.paymentMethod() == null
                        ? "CASH"
                        : r.paymentMethod()
                        .toUpperCase()
        );

        payment.setNotes(
                r.notes()
        );

        payment.setCollectedByAgent(
                agent
        );

        payment.setCollectedByUser(
                user
        );

        payment.setPaymentDate(
                actualPaymentDate
                        .atStartOfDay()
        );

        payment =
                payments.save(
                        payment
                );

        // =====================================================
        // LOAD ALL SCHEDULES
        // =====================================================

        List<LoanSchedule> loanSchedules =
                schedules
                        .findByLoan_IdOrderByInstallmentNumber(
                                loan.getId()
                        );

        // =====================================================
        // APPLY PAYMENT
        //
        // OLD PENDING FIRST
        // THEN CURRENT/FUTURE INSTALLMENT
        // =====================================================

        BigDecimal remainingPayment =
                r.paymentAmount();

        for (
                LoanSchedule schedule :
                loanSchedules
        ) {

            if (
                    remainingPayment.compareTo(
                            BigDecimal.ZERO
                    ) <= 0
            ) {
                break;
            }

            BigDecimal dueAmount =
                    safe(
                            schedule.getDueAmount()
                    );

            BigDecimal alreadyPaid =
                    safe(
                            schedule.getPaidAmount()
                    );

            BigDecimal schedulePending =
                    dueAmount
                            .subtract(
                                    alreadyPaid
                            )
                            .max(
                                    BigDecimal.ZERO
                            );

            // Already paid
            if (
                    schedulePending.compareTo(
                            BigDecimal.ZERO
                    ) <= 0
            ) {
                schedule.setStatus(
                        "PAID"
                );

                continue;
            }

            BigDecimal amountToApply =
                    remainingPayment.min(
                            schedulePending
                    );

            BigDecimal newPaidAmount =
                    alreadyPaid.add(
                            amountToApply
                    );

            schedule.setPaidAmount(
                    newPaidAmount
            );

            // =================================================
            // FULL PAYMENT
            // =================================================

            if (
                    newPaidAmount.compareTo(
                            dueAmount
                    ) >= 0
            ) {

                schedule.setPaidAmount(
                        dueAmount
                );

                schedule.setStatus(
                        "PAID"
                );

                schedule.setPaidDate(
                        actualPaymentDate
                );
            }

            // =================================================
            // PARTIAL PAYMENT
            // =================================================

            else if (
                    newPaidAmount.compareTo(
                            BigDecimal.ZERO
                    ) > 0
            ) {

                schedule.setStatus(
                        "PARTIAL"
                );

                schedule.setPaidDate(
                        null
                );
            }

            schedules.save(
                    schedule
            );

            remainingPayment =
                    remainingPayment.subtract(
                            amountToApply
                    );
        }

        // =====================================================
        // FINE
        // =====================================================

        if (
                selectedSchedule != null &&
                        finePaid.compareTo(
                                BigDecimal.ZERO
                        ) > 0
        ) {

            BigDecimal existingFinePaid =
                    safe(
                            selectedSchedule
                                    .getFinePaid()
                    );

            selectedSchedule.setFinePaid(
                    existingFinePaid.add(
                            finePaid
                    )
            );

            schedules.save(
                    selectedSchedule
            );
        }

        // =====================================================
        // COLLECTION TRANSACTION
        // =====================================================

        if (
                r.paymentAmount()
                        .compareTo(
                                BigDecimal.ZERO
                        ) > 0
        ) {

            tx.create(
                    company,
                    branch,
                    "COLLECTION",
                    payment.getId(),
                    customer,
                    loan,
                    r.paymentAmount(),
                    "IN",
                    "Collection from " +
                            customer.getCustomerName(),
                    agent,
                    user
            );
        }

        // =====================================================
        // FINE TRANSACTION
        // =====================================================

        if (
                finePaid.compareTo(
                        BigDecimal.ZERO
                ) > 0
        ) {

            tx.create(
                    company,
                    branch,
                    "FINE",
                    payment.getId(),
                    customer,
                    loan,
                    finePaid,
                    "IN",
                    "Fine received from " +
                            customer.getCustomerName(),
                    agent,
                    user
            );
        }

        // =====================================================
        // TOTAL CASH COLLECTED
        //
        // For EMI:
        // collectedAmount = repayment collected
        //
        // For IO:
        // collectedAmount = interest received
        //
        // Principal is tracked separately by principalPending.
        // =====================================================

        BigDecimal currentCollected =
                safe(
                        loan.getCollectedAmount()
                );

        BigDecimal newCollected =
                currentCollected.add(
                        r.paymentAmount()
                );

        loan.setCollectedAmount(
                newCollected
        );

        // =====================================================
        // PRINCIPAL / OUTSTANDING
        // =====================================================

        if (
                "IO".equals(
                        loan.getLoanType()
                )
        ) {

            /*
             * IO RULE
             *
             * Interest collection must NEVER
             * reduce principal.
             *
             * Example:
             *
             * Principal Pending = ₹10,000
             *
             * Interest paid = ₹200
             *
             * Principal Pending still = ₹10,000
             */

            BigDecimal existingPrincipal =
                    loan.getPrincipalPending() == null
                            ? safe(
                            loan.getLoanAmount()
                    )
                            : loan.getPrincipalPending();

            loan.setPrincipalPending(
                    existingPrincipal
            );
        }

        else {

            /*
             * EMI RULE
             *
             * Collection reduces repayment outstanding.
             */

            BigDecimal totalRepayment =
                    loan.getTotalRepayment() == null
                            ? safe(
                            loan.getLoanAmount()
                    )
                            : loan.getTotalRepayment();

            BigDecimal principalPending =
                    totalRepayment
                            .subtract(
                                    newCollected
                            )
                            .max(
                                    BigDecimal.ZERO
                            );

            loan.setPrincipalPending(
                    principalPending
            );
        }

        // =====================================================
        // FINE PAID TOTAL
        // =====================================================

        BigDecimal currentFinePaid =
                safe(
                        loan.getFinePaidTotal()
                );

        loan.setFinePaidTotal(
                currentFinePaid.add(
                        finePaid
                )
        );

        // =====================================================
        // CALCULATE ACTUAL DUE / PENDING
        //
        // Only schedules whose due date has arrived
        // are included in pending.
        // =====================================================

        BigDecimal totalPendingDue =
                BigDecimal.ZERO;

        for (
                LoanSchedule schedule :
                loanSchedules
        ) {

            if (
                    schedule.getDueDate() == null
            ) {
                continue;
            }

            if (
                    schedule
                            .getDueDate()
                            .isAfter(
                                    actualPaymentDate
                            )
            ) {
                continue;
            }

            BigDecimal dueAmount =
                    safe(
                            schedule.getDueAmount()
                    );

            BigDecimal paidAmount =
                    safe(
                            schedule.getPaidAmount()
                    );

            BigDecimal pending =
                    dueAmount
                            .subtract(
                                    paidAmount
                            )
                            .max(
                                    BigDecimal.ZERO
                            );

            totalPendingDue =
                    totalPendingDue.add(
                            pending
                    );
        }

        loan.setPendingDue(
                totalPendingDue
        );

        // =====================================================
        // CHECK SCHEDULE STATUS
        // =====================================================

        boolean allSchedulesPaid =
                loanSchedules
                        .stream()
                        .allMatch(
                                schedule ->
                                        "PAID".equals(
                                                schedule.getStatus()
                                        )
                        );

        // =====================================================
        // EMI AUTO CLOSE
        // =====================================================

        if (
                "EMI".equals(
                        loan.getLoanType()
                )
                        &&
                        safe(
                                loan.getPrincipalPending()
                        ).compareTo(
                                BigDecimal.ZERO
                        ) <= 0
                        &&
                        allSchedulesPaid
        ) {

            loan.setStatus(
                    "CLOSED"
            );

            loan.setClosedAt(
                    LocalDateTime.now()
            );

            loan.setPrincipalPending(
                    BigDecimal.ZERO
            );

            loan.setPendingDue(
                    BigDecimal.ZERO
            );
        }

        // =====================================================
        // IO DOES NOT AUTO CLOSE
        //
        // Even if every interest installment is paid,
        // principal is still outstanding.
        //
        // Loan remains ACTIVE until principal is returned /
        // preclosed.
        // =====================================================

        if (
                "IO".equals(
                        loan.getLoanType()
                )
        ) {

            loan.setStatus(
                    "ACTIVE"
            );

            loan.setClosedAt(
                    null
            );
        }

        loans.save(
                loan
        );

        return map(
                payment
        );
    }

    // =========================================================
    // GET ALL PAYMENTS
    // =========================================================

    @Transactional(readOnly = true)
    public List<PaymentResponse> all(
            Long companyId,
            Long branchId
    ) {

        return payments
                .findByCompany_IdAndBranch_IdOrderByPaymentDateDesc(
                        companyId,
                        branchId
                )
                .stream()
                .map(
                        this::map
                )
                .toList();
    }

    // =========================================================
    // SAFE BIGDECIMAL
    // =========================================================

    private BigDecimal safe(
            BigDecimal value
    ) {

        return value == null
                ? BigDecimal.ZERO
                : value;
    }

    // =========================================================
    // RESPONSE
    // =========================================================

    private PaymentResponse map(
            Payment payment
    ) {

        BigDecimal paymentAmount =
                safe(
                        payment.getPaymentAmount()
                );

        BigDecimal finePaid =
                safe(
                        payment.getFinePaid()
                );

        return new PaymentResponse(

                payment.getId(),

                payment.getPaymentCode(),

                payment
                        .getCustomer()
                        .getId(),

                payment
                        .getCustomer()
                        .getCustomerCode(),

                payment
                        .getLoan()
                        .getId(),

                payment
                        .getLoan()
                        .getLoanCode(),

                payment.getSchedule() == null
                        ? null
                        : payment
                        .getSchedule()
                        .getId(),

                paymentAmount,

                finePaid,

                paymentAmount.add(
                        finePaid
                ),

                payment.getPaymentMethod(),

                payment.getPaymentDate()
        );
    }
}