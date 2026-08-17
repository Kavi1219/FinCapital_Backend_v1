package com.fincapital.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class MoneyTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;
    @Column(name = "transaction_code", nullable = false, length = 50)
    private String transactionCode;
    @Column(name = "transaction_type", nullable = false, length = 40)
    private String transactionType;
    @Column(name = "reference_id")
    private Long referenceId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id")
    private Loan loan;
    @Column(name = "amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;
    @Column(name = "direction", nullable = false, length = 10)
    private String direction;
    @Column(name = "description", length = 500)
    private String description;
    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_agent_id")
    private Agent createdByAgent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private AppUser createdByUser;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (transactionDate == null) transactionDate = LocalDateTime.now();
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

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

    public String getTransactionCode() {
        return transactionCode;
    }

    public void setTransactionCode(String v) {
        transactionCode = v;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String v) {
        transactionType = v;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long v) {
        referenceId = v;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer v) {
        customer = v;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan v) {
        loan = v;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal v) {
        amount = v;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String v) {
        direction = v;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String v) {
        description = v;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime v) {
        transactionDate = v;
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
