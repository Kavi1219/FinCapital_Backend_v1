package com.fincapital.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;
    @Column(name = "actor_type", length = 20)
    private String actorType;
    @Column(name = "actor_id")
    private Long actorId;
    @Column(name = "action", nullable = false, length = 100)
    private String action;
    @Column(name = "entity_type", length = 100)
    private String entityType;
    @Column(name = "entity_id")
    private Long entityId;
    @Column(name = "old_value", columnDefinition = "nvarchar(max)")
    private String oldValue;
    @Column(name = "new_value", columnDefinition = "nvarchar(max)")
    private String newValue;
    @Column(name = "ip_address", length = 50)
    private String ipAddress;
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        if (createdAt == null) createdAt = LocalDateTime.now();
    }
}
