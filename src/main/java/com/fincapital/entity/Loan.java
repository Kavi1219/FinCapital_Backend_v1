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
