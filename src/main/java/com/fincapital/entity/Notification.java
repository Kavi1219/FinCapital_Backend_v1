package com.fincapital.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id")
    private Agent agent;
    @Column(name = "title", nullable = false, length = 200)
    private String title;
    @Column(name = "message", nullable = false, length = 1000)
    private String message;
    @Column(name = "notification_type", length = 50)
    private String notificationType;
    @Column(name = "is_read", nullable = false)
    private Boolean read = false;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(name = "read_at")
    private LocalDateTime readAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }

    public void setCompany(Company v) {
        company = v;
    }

    public void setAgent(Agent v) {
        agent = v;
    }

    public void setTitle(String v) {
        title = v;
    }

    public void setMessage(String v) {
        message = v;
    }

    public void setNotificationType(String v) {
        notificationType = v;
    }
}
