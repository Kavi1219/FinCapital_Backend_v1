package com.fincapital.service;

import com.fincapital.dto.*;
import com.fincapital.entity.*;
import com.fincapital.exception.NotFoundException;
import com.fincapital.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
public class LoanService {

    private final LoanRepository loans;
    private final LoanScheduleRepository schedules;
    private final CompanyRepository companies;
    private final BranchRepository branches;
    private final CustomerRepository customers;
    private final AgentRepository agents;
    private final AppUserRepository users;
    private final CodeService codes;
    private final TransactionService tx;

    public LoanService(
            LoanRepository a,
            LoanScheduleRepository b,
            CompanyRepository c,
            BranchRepository d,
            CustomerRepository e,
            AgentRepository f,
            AppUserRepository g,
            CodeService h,
            TransactionService i
    ) {
        loans = a;
        schedules = b;
        companies = c;
        branches = d;
        customers = e;
        agents = f;
        users = g;
        codes = h;
        tx = i;
    }

    // =========================================================
    // CREATE LOAN
    // =========================================================

    @Transactional
    public LoanResponse create(
            CreateLoanRequest r
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

        String cycle =
                r.cycle()
                        .toUpperCase();

        String type =
                r.loanType() == null ||
                        r.loanType().isBlank()
                        ? "EMI"
                        : r.loanType()
                        .toUpperCase();

        if (
                !List.of(
                        "DAILY",
                        "WEEKLY",
                        "MONTHLY"
                ).contains(cycle)
        ) {
            throw new IllegalArgumentException(
                    "Invalid cycle"
            );
        }

        if (
                !List.of(
                        "EMI",
                        "IO"
                ).contains(type)
        ) {
            throw new IllegalArgumentException(
                    "Invalid loan type"
            );
        }

        int duration =
                r.duration();

        if (duration <= 0) {
            throw new IllegalArgumentException(
                    "Duration must be greater than zero"
            );
        }

        boolean interestTakenUpfront =
                Boolean.TRUE.equals(
                        r.interestTakenUpfront()
                );

        // =====================================================
        // INTEREST
        // =====================================================

        BigDecimal interestAmount =
                r.loanAmount()
                        .multiply(
                                r.interestRate()
                        )
                        .divide(
                                new BigDecimal("100"),
                                2,
                                RoundingMode.HALF_UP
                        );

        // =====================================================
        // AMOUNT GIVEN
        // =====================================================

        BigDecimal amountGiven =
                interestTakenUpfront
                        ? r.loanAmount()
                        .subtract(
                                interestAmount
                        )
                        : r.loanAmount();

        if (
                amountGiven.compareTo(
                        BigDecimal.ZERO
                ) < 0
        ) {
            throw new IllegalArgumentException(
                    "Interest amount cannot be greater than loan amount"
            );
        }

        BigDecimal totalRepayment;

        BigDecimal collectionAmount;

        BigDecimal principalPending;

        // =====================================================
        // EMI CALCULATION
        // =====================================================

        if ("EMI".equals(type)) {

            /*
             * INTEREST TAKEN = YES
             *
             * Loan        ₹20,000
             * Interest     ₹3,000
             * Given       ₹17,000
             * Repayment   ₹20,000
             *
             *
             * INTEREST TAKEN = NO
             *
             * Loan        ₹20,000
             * Interest     ₹3,000
             * Given       ₹20,000
             * Repayment   ₹23,000
             */

            totalRepayment =
                    interestTakenUpfront
                            ? r.loanAmount()
                            : r.loanAmount()
                            .add(
                                    interestAmount
                            );

            collectionAmount =
                    totalRepayment.divide(
                            BigDecimal.valueOf(
                                    duration
                            ),
                            2,
                            RoundingMode.HALF_UP
                    );

            principalPending =
                    totalRepayment;
        }

        // =====================================================
        // IO CALCULATION
        // =====================================================

        else {

            /*
             * IO = INTEREST ONLY
             *
             * Example:
             *
             * Loan Amount       ₹10,000
             * Interest          2%
             * Interest Amount     ₹200
             *
             * Interest Taken YES:
             *
             * Amount Given      ₹9,800
             *
             * Weekly Collection ₹200
             *
             * Principal Pending ₹10,000
             *
             * Interest payments do NOT reduce principal.
             */

            totalRepayment =
                    r.loanAmount();

            collectionAmount =
                    interestAmount;

            principalPending =
                    r.loanAmount();
        }

        // =====================================================
        // DURATION UNIT
        // =====================================================

        String durationUnit =
                switch (cycle) {

                    case "DAILY" ->
                            "Days";

                    case "WEEKLY" ->
                            "Weeks";

                    default ->
                            "Months";
                };

        // =====================================================
        // CREATE LOAN
        // =====================================================

        Loan loan =
                new Loan();

        loan.setCompany(
                company
        );

        loan.setBranch(
                branch
        );

        loan.setCustomer(
                customer
        );

        loan.setLoanCode(
                codes.loanCode(
                        company.getCompanyCode(),

                        loans.countByCompany_Id(
                                company.getId()
                        ) + 1
                )
        );

        loan.setLoanAmount(
                r.loanAmount()
        );

        loan.setCycle(
                cycle
        );

        loan.setLoanType(
                type
        );

        loan.setInterestRate(
                r.interestRate()
        );

        loan.setInterestAmount(
                interestAmount
        );

        loan.setInterestTakenUpfront(
                interestTakenUpfront
        );

        loan.setAmountGiven(
                amountGiven
        );

        loan.setTotalRepayment(
                totalRepayment
        );

        loan.setDuration(
                duration
        );

        loan.setDurationUnit(
                durationUnit
        );

        loan.setCollectionAmount(
                collectionAmount
        );

        loan.setCollectedAmount(
                BigDecimal.ZERO
        );

        loan.setPrincipalPending(
                principalPending
        );

        loan.setPendingDue(
                BigDecimal.ZERO
        );

        loan.setFineDue(
                BigDecimal.ZERO
        );

        loan.setFinePaidTotal(
                BigDecimal.ZERO
        );

        loan.setStartDate(
                r.startDate()
        );

        loan.setFinishDate(
                add(
                        r.startDate(),
                        cycle,
                        duration
                )
        );

        loan.setFineEnabled(
                r.fineEnabled() == null
                        ? true
                        : r.fineEnabled()
        );

        loan.setFineAmount(
                r.fineAmount() == null
                        ? BigDecimal.ZERO
                        : r.fineAmount()
        );

        // =====================================================
        // CREATED BY
        // =====================================================

        Agent agent = null;

        AppUser user = null;

        if (
                r.createdByAgentId() != null
        ) {

            agent = agents
                    .findById(
                            r.createdByAgentId()
                    )
                    .orElseThrow(
                            () -> new NotFoundException(
                                    "Agent not found"
                            )
                    );

            loan.setCreatedByAgent(
                    agent
            );
        }

        if (
                r.createdByUserId() != null
        ) {

            user = users
                    .findById(
                            r.createdByUserId()
                    )
                    .orElseThrow(
                            () -> new NotFoundException(
                                    "User not found"
                            )
                    );

            loan.setCreatedByUser(
                    user
            );
        }

        // =====================================================
        // SAVE LOAN
        // =====================================================

        loan =
                loans.save(
                        loan
                );

        // =====================================================
        // CREATE SCHEDULE
        // =====================================================

        for (
                int i = 1;
                i <= duration;
                i++
        ) {

            LoanSchedule schedule =
                    new LoanSchedule();

            schedule.setLoan(
                    loan
            );

            schedule.setInstallmentNumber(
                    i
            );

            schedule.setDueDate(
                    add(
                            r.startDate(),
                            cycle,
                            i
                    )
            );

            schedule.setDueAmount(
                    collectionAmount
            );

            schedule.setStatus(
                    "PENDING"
            );

            schedules.save(
                    schedule
            );
        }

        // =====================================================
        // BORROW / LOAN DISBURSEMENT
        // =====================================================

        tx.create(
                company,
                branch,
                "LOAN_DISBURSEMENT",
                loan.getId(),
                customer,
                loan,
                amountGiven,
                "OUT",
                "Loan amount given to " +
                        customer.getCustomerName(),
                agent,
                user
        );

        return map(
                loan
        );
    }

    // =========================================================
    // PRECLOSE
    // =========================================================

    @Transactional
    public LoanResponse preclose(
            Long id,
            Long collectedByAgentId,
            Long collectedByUserId,
            LocalDate date
    ) {

        Loan loan = loans
                .findById(id)
                .orElseThrow(
                        () -> new NotFoundException(
                                "Loan not found"
                        )
                );

        if (
                !"ACTIVE".equals(
                        loan.getStatus()
                )
        ) {
            throw new IllegalArgumentException(
                    "Only active loans can be preclosed"
            );
        }

        // =====================================================
        // BUSINESS DATE
        // =====================================================

        LocalDate actualDate =
                date == null
                        ? LocalDate.now()
                        : date;

        BigDecimal amount;

        // =====================================================
        // IO PRECLOSE
        // Principal only
        // =====================================================

        if (
                "IO".equals(
                        loan.getLoanType()
                )
        ) {

            amount =
                    loan.getPrincipalPending() == null
                            ? loan.getLoanAmount()
                            : loan.getPrincipalPending();
        }

        // =====================================================
        // EMI PRECLOSE
        // Remaining repayment
        // =====================================================

        else {

            amount =
                    loan.getPrincipalPending() == null
                            ? loan.getTotalRepayment()
                            .subtract(
                                    safe(
                                            loan.getCollectedAmount()
                                    )
                            )
                            .max(
                                    BigDecimal.ZERO
                            )
                            : loan.getPrincipalPending();
        }

        Agent agent =
                collectedByAgentId == null
                        ? null
                        : agents
                        .findById(
                                collectedByAgentId
                        )
                        .orElseThrow(
                                () -> new NotFoundException(
                                        "Agent not found"
                                )
                        );

        AppUser user =
                collectedByUserId == null
                        ? null
                        : users
                        .findById(
                                collectedByUserId
                        )
                        .orElseThrow(
                                () -> new NotFoundException(
                                        "User not found"
                                )
                        );

        loan.setPrecloseAmount(
                amount
        );

        // Actual preclose business date
        loan.setPreclosedAt(
                actualDate.atStartOfDay()
        );

        loan.setStatus(
                "PRECLOSED"
        );

        loan.setCollectedAmount(
                safe(
                        loan.getCollectedAmount()
                ).add(
                        amount
                )
        );

        loan.setPrincipalPending(
                BigDecimal.ZERO
        );

        loan.setPendingDue(
                BigDecimal.ZERO
        );

        loans.save(
                loan
        );

        tx.create(
                loan.getCompany(),
                loan.getBranch(),
                "PRECLOSE",
                loan.getId(),
                loan.getCustomer(),
                loan,
                amount,
                "IN",
                "Loan preclosed by " +
                        loan.getCustomer()
                                .getCustomerName(),
                agent,
                user,
                actualDate.atStartOfDay()
        );

        return map(
                loan
        );
    }

    // =========================================================
    // RETURN PRINCIPAL - IO LOAN
    // =========================================================

    @Transactional
    public LoanResponse returnPrincipal(
            Long id,
            Long collectedByAgentId,
            Long collectedByUserId,
            LocalDate date
    ) {

        Loan loan = loans
                .findById(id)
                .orElseThrow(
                        () -> new NotFoundException(
                                "Loan not found"
                        )
                );

        if (
                !"ACTIVE".equals(
                        loan.getStatus()
                )
        ) {
            throw new IllegalArgumentException(
                    "Only active loans can return principal"
            );
        }

        if (
                !"IO".equals(
                        loan.getLoanType()
                )
        ) {
            throw new IllegalArgumentException(
                    "Principal return is only allowed for IO loans"
            );
        }

        // =====================================================
        // BUSINESS DATE
        // =====================================================

        LocalDate actualDate =
                date == null
                        ? LocalDate.now()
                        : date;

        BigDecimal principalAmount =
                loan.getPrincipalPending() == null
                        ? loan.getLoanAmount()
                        : loan.getPrincipalPending();

        if (
                principalAmount.compareTo(
                        BigDecimal.ZERO
                ) <= 0
        ) {
            throw new IllegalArgumentException(
                    "No principal pending for this loan"
            );
        }

        Agent agent =
                collectedByAgentId == null
                        ? null
                        : agents
                        .findById(
                                collectedByAgentId
                        )
                        .orElseThrow(
                                () -> new NotFoundException(
                                        "Agent not found"
                                )
                        );

        AppUser user =
                collectedByUserId == null
                        ? null
                        : users
                        .findById(
                                collectedByUserId
                        )
                        .orElseThrow(
                                () -> new NotFoundException(
                                        "User not found"
                                )
                        );

        // =====================================================
        // RETURN PRINCIPAL
        // =====================================================

        loan.setPrincipalPending(
                BigDecimal.ZERO
        );

        loan.setPendingDue(
                BigDecimal.ZERO
        );

        loan.setStatus(
                "CLOSED"
        );

        // Actual principal-return business date
        loan.setClosedAt(
                actualDate.atStartOfDay()
        );

        loans.save(
                loan
        );

        // =====================================================
        // CREATE PRINCIPAL RETURN TRANSACTION
        // =====================================================

        tx.create(
                loan.getCompany(),
                loan.getBranch(),
                "PRINCIPAL_RETURN",
                loan.getId(),
                loan.getCustomer(),
                loan,
                principalAmount,
                "IN",
                "Principal returned by " +
                        loan.getCustomer()
                                .getCustomerName(),
                agent,
                user,
                actualDate.atStartOfDay()
        );

        return map(
                loan
        );
    }

    // =========================================================
    // GET ALL
    // =========================================================

    @Transactional(readOnly = true)
    public List<LoanResponse> all(
            Long companyId,
            Long branchId
    ) {

        return loans
                .findByCompany_IdAndBranch_IdOrderByIdDesc(
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
    // GET ONE
    // =========================================================

    @Transactional(readOnly = true)
    public LoanResponse one(
            Long id
    ) {

        Loan loan = loans
                .findById(id)
                .orElseThrow(
                        () -> new NotFoundException(
                                "Loan not found"
                        )
                );

        return map(
                loan
        );
    }

    // =========================================================
    // DATE CALCULATION
    // =========================================================

    private LocalDate add(
            LocalDate date,
            String cycle,
            int number
    ) {

        return switch (cycle) {

            case "DAILY" ->
                    date.plusDays(
                            number
                    );

            case "WEEKLY" ->
                    date.plusWeeks(
                            number
                    );

            case "MONTHLY" ->
                    date.plusMonths(
                            number
                    );

            default ->
                    date;
        };
    }

    // =========================================================
    // SAFE BIG DECIMAL
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

    private LoanResponse map(
            Loan loan
    ) {

        return new LoanResponse(

                loan.getId(),

                loan.getLoanCode(),

                loan.getCustomer()
                        .getId(),

                loan.getCustomer()
                        .getCustomerCode(),

                loan.getCustomer()
                        .getCustomerName(),

                loan.getLoanAmount(),

                loan.getCycle(),

                loan.getLoanType(),

                loan.getInterestRate(),

                loan.getInterestAmount(),

                loan.getInterestTakenUpfront(),

                loan.getAmountGiven(),

                loan.getTotalRepayment(),

                loan.getDuration(),

                loan.getDurationUnit(),

                loan.getCollectionAmount(),

                loan.getCollectedAmount(),

                loan.getPrincipalPending(),

                loan.getPendingDue(),

                loan.getFineDue(),

                loan.getFinePaidTotal(),

                loan.getStartDate(),

                loan.getFinishDate(),

                loan.getFineEnabled(),

                loan.getFineAmount(),

                loan.getStatus(),

                loan.getPrecloseAmount(),

                loan.getPreclosedAt(),

                loan.getClosedAt(),

                loan.getCreatedAt()
        );
    }
}