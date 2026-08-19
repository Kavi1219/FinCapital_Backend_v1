package com.fincapital.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loans")
public class Loan extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @Column(name = "loan_code", nullable = false, length = 40)
    private String loanCode;
    @Column(name = "loan_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal loanAmount;
    @Column(name = "cycle", nullable = false, length = 20)
    private String cycle;
    @Column(name = "loan_type", nullable = false, length = 20)
    private String loanType;
    @Column(name = "interest_rate", nullable = false, precision = 8, scale = 4)
    private BigDecimal interestRate;
    @Column(name = "interest_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal interestAmount;
    @Column(name = "amount_given", nullable = false, precision = 18, scale = 2)
    private BigDecimal amountGiven;
    @Column(name = "interest_taken_upfront", nullable = false)
    private Boolean interestTakenUpfront = false;
    @Column(name = "total_repayment", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalRepayment;
    @Column(name = "duration_unit", nullable = false, length = 20)
    private String durationUnit;
    @Column(name = "collected_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal collectedAmount = BigDecimal.ZERO;
    @Column(name = "principal_pending", nullable = false, precision = 18, scale = 2)
    private BigDecimal principalPending = BigDecimal.ZERO;
    @Column(name = "pending_due", nullable = false, precision = 18, scale = 2)
    private BigDecimal pendingDue = BigDecimal.ZERO;
    @Column(name = "fine_due", nullable = false, precision = 18, scale = 2)
    private BigDecimal fineDue = BigDecimal.ZERO;
    @Column(name = "fine_paid_total", nullable = false, precision = 18, scale = 2)
    private BigDecimal finePaidTotal = BigDecimal.ZERO;
    @Column(name = "preclose_amount", precision = 18, scale = 2)
    private BigDecimal precloseAmount;
    @Column(name = "preclosed_at")
    private java.time.LocalDateTime preclosedAt;
    @Column(name = "closed_at")
    private java.time.LocalDateTime closedAt;
    @Column(name = "duration", nullable = false)
    private Integer duration;
    @Column(name = "collection_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal collectionAmount;
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "finish_date")
    private LocalDate finishDate;
    @Column(name = "fine_enabled", nullable = false)
    private Boolean fineEnabled = true;
    @Column(name = "fine_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal fineAmount = BigDecimal.ZERO;
    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE";
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_agent_id")
    private Agent createdByAgent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private AppUser createdByUser;

    public Long getId() {
        return id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company v) {
        company = v;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch v) {
        branch = v;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer v) {
        customer = v;
    }

    public String getLoanCode() {
        return loanCode;
    }

    public void setLoanCode(String v) {
        loanCode = v;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(BigDecimal v) {
        loanAmount = v;
    }

    public String getCycle() {
        return cycle;
    }

    public void setCycle(String v) {
        cycle = v;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String v) {
        loanType = v;
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(BigDecimal v) {
        interestRate = v;
    }

    public BigDecimal getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(BigDecimal v) {
        interestAmount = v;
    }

    public BigDecimal getAmountGiven() {
        return amountGiven;
    }

    public void setAmountGiven(BigDecimal v) {
        amountGiven = v;
    }


    public Boolean getInterestTakenUpfront() { return interestTakenUpfront; }
    public void setInterestTakenUpfront(Boolean v) { interestTakenUpfront = v; }
    public BigDecimal getTotalRepayment() { return totalRepayment; }
    public void setTotalRepayment(BigDecimal v) { totalRepayment = v; }
    public String getDurationUnit() { return durationUnit; }
    public void setDurationUnit(String v) { durationUnit = v; }
    public BigDecimal getCollectedAmount() { return collectedAmount; }
    public void setCollectedAmount(BigDecimal v) { collectedAmount = v; }
    public BigDecimal getPrincipalPending() { return principalPending; }
    public void setPrincipalPending(BigDecimal v) { principalPending = v; }
    public BigDecimal getPendingDue() { return pendingDue; }
    public void setPendingDue(BigDecimal v) { pendingDue = v; }
    public BigDecimal getFineDue() { return fineDue; }
    public void setFineDue(BigDecimal v) { fineDue = v; }
    public BigDecimal getFinePaidTotal() { return finePaidTotal; }
    public void setFinePaidTotal(BigDecimal v) { finePaidTotal = v; }
    public BigDecimal getPrecloseAmount() { return precloseAmount; }
    public void setPrecloseAmount(BigDecimal v) { precloseAmount = v; }
    public java.time.LocalDateTime getPreclosedAt() { return preclosedAt; }
    public void setPreclosedAt(java.time.LocalDateTime v) { preclosedAt = v; }
    public java.time.LocalDateTime getClosedAt() { return closedAt; }
    public void setClosedAt(java.time.LocalDateTime v) { closedAt = v; }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer v) {
        duration = v;
    }

    public BigDecimal getCollectionAmount() {
        return collectionAmount;
    }

    public void setCollectionAmount(BigDecimal v) {
        collectionAmount = v;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate v) {
        startDate = v;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate v) {
        finishDate = v;
    }

    public Boolean getFineEnabled() {
        return fineEnabled;
    }

    public void setFineEnabled(Boolean v) {
        fineEnabled = v;
    }

    public BigDecimal getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(BigDecimal v) {
        fineAmount = v;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String v) {
        status = v;
    }

    public Agent getCreatedByAgent() {
        return createdByAgent;
    }

    public void setCreatedByAgent(Agent v) {
        createdByAgent = v;
    }

    public AppUser getCreatedByUser() {
        return createdByUser;
    }

    public void setCreatedByUser(AppUser v) {
        createdByUser = v;
    }
}
