package com.fincapital.service;

import com.fincapital.dto.*;
import com.fincapital.entity.*;
import com.fincapital.exception.NotFoundException;
import com.fincapital.repository.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

@Service
public class CustomerService {

    private final CustomerRepository customers;
    private final JaminRepository jamins;
    private final CustomerRatingRepository ratings;
    private final CompanyRepository companies;
    private final BranchRepository branches;
    private final AgentRepository agents;
    private final AppUserRepository users;
    private final CodeService codes;

    private final LoanRepository loans;
    private final LoanScheduleRepository schedules;
    private final MoneyTransactionRepository transactions;
    private final DocumentRepository documents;

    public CustomerService(
            CustomerRepository a,
            JaminRepository b,
            CustomerRatingRepository c,
            CompanyRepository d,
            BranchRepository e,
            AgentRepository f,
            AppUserRepository g,
            CodeService h,
            LoanRepository i,
            LoanScheduleRepository j,
            MoneyTransactionRepository k,
            DocumentRepository l
    ) {

        customers = a;
        jamins = b;
        ratings = c;
        companies = d;
        branches = e;
        agents = f;
        users = g;
        codes = h;

        loans = i;
        schedules = j;
        transactions = k;
        documents = l;
    }

    // =========================================================
    // CREATE CUSTOMER
    // =========================================================

    @Transactional
    public CustomerResponse create(
            CreateCustomerRequest r
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

        Customer customer =
                new Customer();

        customer.setCompany(
                company
        );

        customer.setBranch(
                branch
        );

        customer.setCustomerCode(
                codes.customerCode(
                        company.getCompanyCode(),
                        customers.countByCompany_Id(
                                company.getId()
                        ) + 1
                )
        );

        customer.setCustomerName(
                r.name()
        );

        customer.setMobile(
                r.mobile()
        );

        customer.setFatherName(
                r.fatherName()
        );

        customer.setWork(
                r.work()
        );

        customer.setAddress(
                r.address()
        );

        customer.setProfilePhotoUrl(
                r.profilePhotoUrl()
        );

        customer.setDocumentPhotoUrl(
                r.documentPhotoUrl()
        );

        if (
                r.createdByAgentId() != null
        ) {

            customer.setCreatedByAgent(
                    agents
                            .findById(
                                    r.createdByAgentId()
                            )
                            .orElseThrow(
                                    () -> new NotFoundException(
                                            "Agent not found"
                                    )
                            )
            );
        }

        if (
                r.createdByUserId() != null
        ) {

            customer.setCreatedByUser(
                    users
                            .findById(
                                    r.createdByUserId()
                            )
                            .orElseThrow(
                                    () -> new NotFoundException(
                                            "User not found"
                                    )
                            )
            );
        }

        customer =
                customers.save(
                        customer
                );

        // =====================================================
        // JAMIN
        // =====================================================

        JaminRequest request =
                r.jamin();

        Jamin jamin =
                new Jamin();

        jamin.setCustomer(
                customer
        );

        jamin.setJaminName(
                request.name()
        );

        jamin.setMobile(
                request.mobile()
        );

        jamin.setFatherName(
                request.fatherName()
        );

        jamin.setWork(
                request.work()
        );

        jamin.setAddress(
                request.address()
        );

        jamin.setRelationship(
                request.relationship()
        );

        jamin.setProfilePhotoUrl(
                request.profilePhotoUrl()
        );

        jamin.setDocumentPhotoUrl(
                request.documentPhotoUrl()
        );

        jamins.save(
                jamin
        );

        // =====================================================
        // CUSTOMER RATING
        // =====================================================

        CustomerRating rating =
                new CustomerRating();

        rating.setCustomer(
                customer
        );

        ratings.save(
                rating
        );

        return map(
                customer,
                jamin
        );
    }

    // =========================================================
    // GET ALL CUSTOMERS
    // =========================================================

    @Transactional(readOnly = true)
    public List<CustomerResponse> all(
            Long companyId,
            Long branchId
    ) {

        return customers
                .findByCompany_IdAndBranch_IdOrderByIdDesc(
                        companyId,
                        branchId
                )
                .stream()
                .map(
                        customer ->
                                map(
                                        customer,
                                        jamins
                                                .findFirstByCustomer_Id(
                                                        customer.getId()
                                                )
                                                .orElse(null)
                                )
                )
                .toList();
    }

    // =========================================================
    // GET ONE CUSTOMER
    // =========================================================

    @Transactional(readOnly = true)
    public CustomerResponse one(
            Long id
    ) {

        Customer customer =
                customers
                        .findById(id)
                        .orElseThrow(
                                () -> new NotFoundException(
                                        "Customer not found"
                                )
                        );

        return map(
                customer,
                jamins
                        .findFirstByCustomer_Id(id)
                        .orElse(null)
        );
    }

    // =========================================================
    // CUSTOMER PROFILE
    // =========================================================

    @Transactional(readOnly = true)
    public CustomerProfileResponse profile(
            Long customerId
    ) {

        Customer customer =
                customers
                        .findById(customerId)
                        .orElseThrow(
                                () -> new NotFoundException(
                                        "Customer not found"
                                )
                        );

        Jamin jamin =
                jamins
                        .findFirstByCustomer_Id(
                                customerId
                        )
                        .orElse(null);

        // =====================================================
        // CUSTOMER DOCUMENTS
        // =====================================================

        List<CustomerProfileResponse.DocumentInfo>
                customerDocuments =
                documents
                        .findByCustomer_IdOrderByUploadedAtDesc(
                                customerId
                        )
                        .stream()
                        .map(
                                this::mapDocument
                        )
                        .toList();

        // =====================================================
        // JAMIN DOCUMENTS
        // =====================================================

        List<CustomerProfileResponse.DocumentInfo>
                jaminDocuments =
                jamin == null
                        ? List.of()
                        : documents
                        .findByJamin_IdOrderByUploadedAtDesc(
                                jamin.getId()
                        )
                        .stream()
                        .map(
                                this::mapDocument
                        )
                        .toList();

        // =====================================================
        // CUSTOMER LOANS
        // =====================================================

        List<Loan> customerLoans =
                loans
                        .findByCustomer_IdOrderByIdDesc(
                                customerId
                        );

        // =====================================================
        // CUSTOMER HISTORY
        // =====================================================

        List<MoneyTransaction> history =
                transactions
                        .findByCustomer_IdOrderByTransactionDateDesc(
                                customerId
                        );

        // =====================================================
        // LOAN COUNTS
        // =====================================================

        long activeLoanCount =
                customerLoans
                        .stream()
                        .filter(
                                loan ->
                                        "ACTIVE".equals(
                                                loan.getStatus()
                                        )
                        )
                        .count();

        long closedLoanCount =
                customerLoans
                        .stream()
                        .filter(
                                loan ->
                                        "CLOSED".equals(
                                                loan.getStatus()
                                        )
                                                ||
                                                "PRECLOSED".equals(
                                                        loan.getStatus()
                                                )
                        )
                        .count();

        // =====================================================
        // OVERALL OUTSTANDING
        // =====================================================

        BigDecimal overallOutstanding =
                customerLoans
                        .stream()
                        .filter(
                                loan ->
                                        "ACTIVE".equals(
                                                loan.getStatus()
                                        )
                        )
                        .map(
                                loan ->
                                        safe(
                                                loan.getPrincipalPending()
                                        )
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        // =====================================================
        // OVERALL PENDING
        // =====================================================

        BigDecimal overallPending =
                customerLoans
                        .stream()
                        .filter(
                                loan ->
                                        "ACTIVE".equals(
                                                loan.getStatus()
                                        )
                        )
                        .map(
                                loan ->
                                        safe(
                                                loan.getPendingDue()
                                        )
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        // =====================================================
        // CUSTOMER STATUS
        // =====================================================

        String customerStatus;

        if (customerLoans.isEmpty()) {

            // Newly created customer with no loan yet
            customerStatus = "ACTIVE";

        } else if (activeLoanCount > 0) {

            // At least one loan is currently active
            customerStatus = "ACTIVE";

        } else {

            // Customer has loans, but all are closed/preclosed
            customerStatus = "CLOSED";
        }

        // =====================================================
        // LOANS RESPONSE
        // =====================================================

        List<CustomerProfileResponse.LoanInfo>
                loanResponses =
                customerLoans
                        .stream()
                        .map(
                                loan ->
                                        mapLoan(
                                                loan,
                                                history
                                        )
                        )
                        .toList();

        // =====================================================
        // TRANSACTION RESPONSE
        // =====================================================

        List<CustomerProfileResponse.TransactionInfo>
                transactionResponses =
                history
                        .stream()
                        .map(
                                this::mapTransaction
                        )
                        .toList();

        // =====================================================
        // JAMIN RESPONSE
        // =====================================================

        CustomerProfileResponse.JaminInfo
                jaminInfo =
                jamin == null
                        ? null
                        : new CustomerProfileResponse.JaminInfo(

                        jamin.getJaminName(),

                        jamin.getMobile(),

                        jamin.getFatherName(),

                        jamin.getWork(),

                        jamin.getAddress(),

                        jamin.getProfilePhotoUrl(),

                        jamin.getDocumentPhotoUrl()
                );

        // =====================================================
        // FINAL PROFILE RESPONSE
        // =====================================================

        return new CustomerProfileResponse(

                customer.getId(),

                customer.getCustomerCode(),

                customer.getCustomerName(),

                customer.getMobile(),

                customer.getFatherName(),

                customer.getWork(),

                customer.getAddress(),

                customer.getProfilePhotoUrl(),

                customer.getDocumentPhotoUrl(),

                customer.getCreatedAt(),

                customerStatus,

                activeLoanCount,

                closedLoanCount,

                overallOutstanding,

                overallPending,

                jaminInfo,

                customerDocuments,

                jaminDocuments,

                loanResponses,

                transactionResponses
        );
    }

    // =========================================================
    // MAP DOCUMENT
    // =========================================================

    private CustomerProfileResponse.DocumentInfo mapDocument(
            Document document
    ) {

        return new CustomerProfileResponse.DocumentInfo(

                document.getId(),

                document.getOwnerType(),

                document.getDocumentType(),

                document.getDocumentName(),

                document.getFileUrl(),

                document.getMimeType(),

                document.getUploadedAt()
        );
    }

    // =========================================================
    // MAP PROFILE LOAN
    // =========================================================
    private CustomerProfileResponse.LoanInfo mapLoan(
            Loan loan,
            List<MoneyTransaction> history
    ) {

        List<LoanSchedule> loanSchedules =
                schedules
                        .findByLoan_IdOrderByInstallmentNumber(
                                loan.getId()
                        );

        // =====================================================
        // CLOSED / PRECLOSED CHECK
        // =====================================================

        boolean loanClosed =
                "CLOSED".equals(
                        loan.getStatus()
                )
                        ||
                        "PRECLOSED".equals(
                                loan.getStatus()
                        );

        // =====================================================
        // PAID COUNT
        // =====================================================

        long paidCount =
                loanSchedules
                        .stream()
                        .filter(
                                schedule ->
                                        "PAID".equals(
                                                schedule.getStatus()
                                        )
                        )
                        .count();

        // =====================================================
        // OUTSTANDING COUNT
        // CLOSED / PRECLOSED = ALWAYS 0
        // =====================================================

        long outstandingCount;

        if (loanClosed) {

            outstandingCount = 0;

        } else {

            outstandingCount =
                    loanSchedules
                            .stream()
                            .filter(
                                    schedule ->
                                            !"PAID".equals(
                                                    schedule.getStatus()
                                            )
                            )
                            .count();
        }

        // =====================================================
        // PENDING COUNT
        // CLOSED / PRECLOSED = ALWAYS 0
        // =====================================================

        LocalDate today =
                LocalDate.now();

        long pendingCount;

        if (loanClosed) {

            pendingCount = 0;

        } else {

            pendingCount =
                    loanSchedules
                            .stream()
                            .filter(
                                    schedule -> {

                                        if (
                                                "PARTIAL".equals(
                                                        schedule.getStatus()
                                                )
                                                        ||
                                                        "OVERDUE".equals(
                                                                schedule.getStatus()
                                                        )
                                        ) {
                                            return true;
                                        }

                                        return
                                                "PENDING".equals(
                                                        schedule.getStatus()
                                                )
                                                        &&
                                                        schedule.getDueDate() != null
                                                        &&
                                                        !schedule
                                                                .getDueDate()
                                                                .isAfter(today);
                                    }
                            )
                            .count();
        }

        // =====================================================
        // FINE PAID COUNT
        // =====================================================

        long finePaidCount =
                loanSchedules
                        .stream()
                        .filter(
                                schedule ->
                                        safe(
                                                schedule.getFinePaid()
                                        )
                                                .compareTo(
                                                        BigDecimal.ZERO
                                                ) > 0
                        )
                        .count();

        // =====================================================
        // LATE PAYMENT COUNT
        // =====================================================

        long latePaymentCount =
                loanSchedules
                        .stream()
                        .filter(
                                schedule ->
                                        schedule.getDueDate() != null
                                                &&
                                                schedule.getPaidDate() != null
                                                &&
                                                schedule
                                                        .getPaidDate()
                                                        .isAfter(
                                                                schedule.getDueDate()
                                                        )
                        )
                        .count();

        // =====================================================
        // TOTAL AMOUNT RECEIVED
        // =====================================================

        BigDecimal totalAmountReceived =
                history
                        .stream()
                        .filter(
                                transaction ->
                                        transaction.getLoan() != null
                                                &&
                                                transaction
                                                        .getLoan()
                                                        .getId()
                                                        .equals(
                                                                loan.getId()
                                                        )
                                                &&
                                                "IN".equals(
                                                        transaction.getDirection()
                                                )
                        )
                        .map(
                                transaction ->
                                        safe(
                                                transaction.getAmount()
                                        )
                        )
                        .reduce(
                                BigDecimal.ZERO,
                                BigDecimal::add
                        );

        // =====================================================
        // CLOSE TYPE
        // =====================================================

        String closeType = null;

        if (
                "PRECLOSED".equals(
                        loan.getStatus()
                )
        ) {

            closeType =
                    "PRECLOSE";

        } else if (
                "CLOSED".equals(
                        loan.getStatus()
                )
        ) {

            boolean principalReturn =
                    history
                            .stream()
                            .anyMatch(
                                    transaction ->
                                            transaction.getLoan() != null
                                                    &&
                                                    transaction
                                                            .getLoan()
                                                            .getId()
                                                            .equals(
                                                                    loan.getId()
                                                            )
                                                    &&
                                                    "PRINCIPAL_RETURN".equals(
                                                            transaction.getTransactionType()
                                                    )
                            );

            closeType =
                    principalReturn
                            ? "PRINCIPAL_RETURN"
                            : "DUE_CLOSED";
        }

        // =====================================================
        // CLOSE DATE
        // =====================================================

        java.time.LocalDateTime closeDate =
                loan.getPreclosedAt() != null
                        ? loan.getPreclosedAt()
                        : loan.getClosedAt();

        // =====================================================
        // RESPONSE
        // =====================================================

        return new CustomerProfileResponse.LoanInfo(

                loan.getId(),

                loan.getLoanCode(),

                loan.getCreatedAt(),

                loan.getStatus(),

                loan.getLoanAmount(),

                loan.getCycle(),

                cycleDisplay(
                        loan
                ),

                loan.getLoanType(),

                loan.getInterestRate(),

                loan.getInterestAmount(),

                loan.getInterestTakenUpfront(),

                loan.getAmountGiven(),

                loan.getTotalRepayment(),

                loan.getDuration(),

                loan.getDurationUnit(),

                loan.getCollectionAmount(),

                safe(
                        loan.getCollectedAmount()
                ),

                safe(
                        loan.getPrincipalPending()
                ),

                safe(
                        loan.getPendingDue()
                ),

                safe(
                        loan.getFineDue()
                ),

                safe(
                        loan.getFinePaidTotal()
                ),

                outstandingCount,

                paidCount,

                pendingCount,

                finePaidCount,

                latePaymentCount,

                totalAmountReceived,

                closeType,

                closeDate
        );
    }
    // =========================================================
    // MAP TRANSACTION
    // =========================================================

    private CustomerProfileResponse.TransactionInfo mapTransaction(
            MoneyTransaction transaction
    ) {

        String type =
                transactionDisplayType(
                        transaction.getTransactionType()
                );

        String collectedBy =
                getCollectedBy(
                        transaction
                );

        return new CustomerProfileResponse.TransactionInfo(

                transaction.getId(),

                transaction.getTransactionCode(),

                transaction.getTransactionDate(),

                transaction.getLoan() == null
                        ? null
                        : transaction
                        .getLoan()
                        .getId(),

                transaction.getLoan() == null
                        ? null
                        : transaction
                        .getLoan()
                        .getLoanCode(),

                type,

                transaction.getTransactionType(),

                safe(
                        transaction.getAmount()
                ),

                transaction.getDirection(),

                collectedBy,

                transaction.getDescription()
        );
    }

    // =========================================================
    // TRANSACTION DISPLAY TYPE
    // =========================================================

    private String transactionDisplayType(
            String rawType
    ) {

        if (rawType == null) {
            return "-";
        }

        return switch (rawType) {

            case "LOAN_DISBURSEMENT" ->
                    "BORROW";

            case "COLLECTION" ->
                    "DUE";

            case "FINE" ->
                    "FINE";

            case "PRECLOSE" ->
                    "PRECLOSE";

            case "PRINCIPAL_RETURN" ->
                    "PRINCIPAL_RETURN";

            default ->
                    rawType;
        };
    }

    // =========================================================
    // COLLECTED BY
    // =========================================================

    private String getCollectedBy(
            MoneyTransaction transaction
    ) {

        if (
                transaction.getCreatedByAgent() != null
        ) {

            return "Agent " +
                    transaction
                            .getCreatedByAgent()
                            .getId();
        }

        if (
                transaction.getCreatedByUser() != null
        ) {

            return "User " +
                    transaction
                            .getCreatedByUser()
                            .getId();
        }

        return "Owner";
    }

    // =========================================================
    // CYCLE DISPLAY
    // =========================================================

    private String cycleDisplay(
            Loan loan
    ) {

        if (
                loan.getCycle() == null
        ) {
            return "-";
        }

        if (
                "DAILY".equals(
                        loan.getCycle()
                )
        ) {
            return "Daily";
        }

        if (
                "WEEKLY".equals(
                        loan.getCycle()
                )
        ) {

            String day =
                    loan.getStartDate()
                            .getDayOfWeek()
                            .getDisplayName(
                                    TextStyle.FULL,
                                    Locale.ENGLISH
                            );

            return day +
                    " / Weekly";
        }

        if (
                "MONTHLY".equals(
                        loan.getCycle()
                )
        ) {

            int day =
                    loan.getStartDate()
                            .getDayOfMonth();

            return ordinal(day) +
                    " / Monthly";
        }

        return loan.getCycle();
    }

    // =========================================================
    // ORDINAL
    // =========================================================

    private String ordinal(
            int number
    ) {

        if (
                number >= 11 &&
                        number <= 13
        ) {
            return number + "th";
        }

        return switch (
                number % 10
                ) {

            case 1 ->
                    number + "st";

            case 2 ->
                    number + "nd";

            case 3 ->
                    number + "rd";

            default ->
                    number + "th";
        };
    }

    // =========================================================
    // SAFE DECIMAL
    // =========================================================

    private BigDecimal safe(
            BigDecimal value
    ) {

        return value == null
                ? BigDecimal.ZERO
                : value;
    }

    // =========================================================
    // BASIC CUSTOMER RESPONSE
    // =========================================================

    private CustomerResponse map(
            Customer customer,
            Jamin jamin
    ) {

        return new CustomerResponse(

                customer.getId(),

                customer
                        .getCompany()
                        .getId(),

                customer
                        .getBranch()
                        .getId(),

                customer.getCustomerCode(),

                customer.getCustomerName(),

                customer.getMobile(),

                customer.getFatherName(),

                customer.getWork(),

                customer.getAddress(),

                customer.getProfilePhotoUrl(),

                customer.getDocumentPhotoUrl(),

                customer.getStatus(),

                customer.getCreatedAt(),

                jamin == null
                        ? null
                        : jamin.getJaminName(),

                jamin == null
                        ? null
                        : jamin.getMobile(),

                jamin == null
                        ? null
                        : jamin.getFatherName(),

                jamin == null
                        ? null
                        : jamin.getWork(),

                jamin == null
                        ? null
                        : jamin.getAddress(),

                jamin == null
                        ? null
                        : jamin.getProfilePhotoUrl(),

                jamin == null
                        ? null
                        : jamin.getDocumentPhotoUrl()
        );
    }
}