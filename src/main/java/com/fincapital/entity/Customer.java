package com.fincapital.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class Customer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;
    @Column(name = "customer_code", nullable = false, length = 40)
    private String customerCode;
    @Column(name = "customer_name", nullable = false, length = 150)
    private String customerName;
    @Column(name = "mobile", nullable = false, length = 20)
    private String mobile;
    @Column(name = "father_name", length = 150)
    private String fatherName;
    @Column(name = "work", length = 150)
    private String work;
    @Column(name = "address", nullable = false, length = 500)
    private String address;
    @Column(name = "profile_photo_url", length = 500)
    private String profilePhotoUrl;
    @Column(name = "document_photo_url", length = 500)
    private String documentPhotoUrl;
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

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String v) {
        customerCode = v;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String v) {
        customerName = v;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String v) {
        mobile = v;
    }

    public String getFatherName() { return fatherName; }
    public void setFatherName(String v) { fatherName = v; }

    public String getWork() {
        return work;
    }

    public void setWork(String v) {
        work = v;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String v) {
        address = v;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    public void setProfilePhotoUrl(String v) {
        profilePhotoUrl = v;
    }

    public String getDocumentPhotoUrl() {
        return documentPhotoUrl;
    }

    public void setDocumentPhotoUrl(String v) {
        documentPhotoUrl = v;
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
