package com.fincapital.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "loan_schedules")
public class LoanSchedule extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;
    @Column(name = "installment_number", nullable = false)
    private Integer installmentNumber;
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
    @Column(name = "due_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal dueAmount;
    @Column(name = "paid_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;
    @Column(name = "fine_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal fineAmount = BigDecimal.ZERO;
    @Column(name = "fine_paid", nullable = false, precision = 18, scale = 2)
    private BigDecimal finePaid = BigDecimal.ZERO;
    @Column(name = "status", nullable = false, length = 20)
    private String status = "PENDING";
    @Column(name = "paid_date")
    private LocalDate paidDate;

    public Long getId() {
        return id;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan v) {
        loan = v;
    }

    public Integer getInstallmentNumber() {
        return installmentNumber;
    }

    public void setInstallmentNumber(Integer v) {
        installmentNumber = v;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate v) {
        dueDate = v;
    }

    public BigDecimal getDueAmount() {
        return dueAmount;
    }

    public void setDueAmount(BigDecimal v) {
        dueAmount = v;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal v) {
        paidAmount = v;
    }

    public BigDecimal getFineAmount() {
        return fineAmount;
    }

    public void setFineAmount(BigDecimal v) {
        fineAmount = v;
    }

    public BigDecimal getFinePaid() {
        return finePaid;
    }

    public void setFinePaid(BigDecimal v) {
        finePaid = v;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String v) {
        status = v;
    }

    public LocalDate getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDate v) {
        paidDate = v;
    }
}
