package com.fincapital.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "agents")
public class Agent extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;
    @Column(name = "employee_code", length = 40)
    private String employeeCode;
    @Column(name = "agent_name", nullable = false, length = 150)
    private String agentName;
    @Column(name = "mobile", nullable = false, length = 20)
    private String mobile;
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;
    @Column(name = "profile_photo_url", nullable = false, length = 500)
    private String profilePhotoUrl;
    @Column(name = "mobile_verified", nullable = false)
    private Boolean mobileVerified = false;
    @Column(name = "approval_status", nullable = false, length = 30)
    private String approvalStatus = "PENDING_OTP";
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private AppUser approvedBy;
    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
    @Column(name = "rejection_reason", length = 500)
    private String rejectionReason;

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

    public String getEmployeeCode() {
        return employeeCode;
    }

    public void setEmployeeCode(String v) {
        employeeCode = v;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String v) {
        agentName = v;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String v) {
        mobile = v;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String v) {
        passwordHash = v;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String v) {
        profilePhotoUrl = v;
    }

    public Boolean getMobileVerified() {
        return mobileVerified;
    }

    public void setMobileVerified(Boolean v) {
        mobileVerified = v;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String v) {
        approvalStatus = v;
    }

    public AppUser getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(AppUser v) {
        approvedBy = v;
    }

    public LocalDateTime getApprovedAt() {
        return approvedAt;
    }

    public void setApprovedAt(LocalDateTime v) {
        approvedAt = v;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String v) {
        rejectionReason = v;
    }
}
