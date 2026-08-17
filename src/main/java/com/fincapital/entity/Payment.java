package com.fincapital.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;
    @Column(name = "payment_code", nullable = false, length = 50)
    private String paymentCode;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private LoanSchedule schedule;
    @Column(name = "payment_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal paymentAmount = BigDecimal.ZERO;
    @Column(name = "fine_paid", nullable = false, precision = 18, scale = 2)
    private BigDecimal finePaid = BigDecimal.ZERO;
    @Column(name = "total_received", insertable = false, updatable = false, precision = 18, scale = 2)
    private BigDecimal totalReceived;
    @Column(name = "payment_method", nullable = false, length = 30)
    private String paymentMethod = "CASH";
    @Column(name = "notes", length = 500)
    private String notes;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collected_by_agent_id")
    private Agent collectedByAgent;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "collected_by_user_id")
    private AppUser collectedByUser;
    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (paymentDate == null) paymentDate = LocalDateTime.now();
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

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String v) {
        paymentCode = v;
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

    public LoanSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(LoanSchedule v) {
        schedule = v;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal v) {
        paymentAmount = v;
    }

    public BigDecimal getFinePaid() {
        return finePaid;
    }

    public void setFinePaid(BigDecimal v) {
        finePaid = v;
    }

    public BigDecimal getTotalReceived() {
        return totalReceived;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String v) {
        paymentMethod = v;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String v) {
        notes = v;
    }

    public Agent getCollectedByAgent() {
        return collectedByAgent;
    }

    public void setCollectedByAgent(Agent v) {
        collectedByAgent = v;
    }

    public AppUser getCollectedByUser() {
        return collectedByUser;
    }

    public void setCollectedByUser(AppUser v) {
        collectedByUser = v;
    }

    public LocalDateTime getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDateTime v) {
        paymentDate = v;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
