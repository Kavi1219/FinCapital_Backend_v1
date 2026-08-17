package com.fincapital.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "customer_ratings")
public class CustomerRating extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    private Customer customer;
    @Column(name = "rating", precision = 3, scale = 2)
    private BigDecimal rating;
    @Column(name = "rating_score")
    private Integer ratingScore;
    @Column(name = "total_loans", nullable = false)
    private Integer totalLoans = 0;
    @Column(name = "completed_loans", nullable = false)
    private Integer completedLoans = 0;
    @Column(name = "on_time_payments", nullable = false)
    private Integer onTimePayments = 0;
    @Column(name = "late_payments", nullable = false)
    private Integer latePayments = 0;
    @Column(name = "overdue_count", nullable = false)
    private Integer overdueCount = 0;
    @Column(name = "fine_count", nullable = false)
    private Integer fineCount = 0;
    @Column(name = "review", length = 1000)
    private String review;
    @Column(name = "calculated_at")
    private LocalDateTime calculatedAt;

    public void setCustomer(Customer v) {
        customer = v;
    }
}
