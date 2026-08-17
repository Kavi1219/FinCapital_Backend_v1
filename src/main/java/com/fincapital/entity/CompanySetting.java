package com.fincapital.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "company_settings")
public class CompanySetting extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false, unique = true)
    private Company company;
    @Column(name = "daily_default_duration", nullable = false)
    private Integer dailyDefaultDuration = 100;
    @Column(name = "weekly_default_duration", nullable = false)
    private Integer weeklyDefaultDuration = 10;
    @Column(name = "monthly_default_duration", nullable = false)
    private Integer monthlyDefaultDuration = 10;
    @Column(name = "currency", nullable = false, length = 10)
    private String currency = "INR";
    @Column(name = "sms_enabled", nullable = false)
    private Boolean smsEnabled = false;
    @Column(name = "whatsapp_enabled", nullable = false)
    private Boolean whatsappEnabled = false;
    @Column(name = "email_enabled", nullable = false)
    private Boolean emailEnabled = true;

    public void setCompany(Company v) {
        company = v;
    }
}
