package com.fincapital.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "branches")
public class Branch extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    @Column(name = "branch_code", nullable = false, length = 30)
    private String branchCode;
    @Column(name = "branch_name", nullable = false, length = 150)
    private String branchName;
    @Column(name = "location", length = 200)
    private String location;
    @Column(name = "address", length = 500)
    private String address;
    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE";

    public Long getId() {
        return id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company v) {
        company = v;
    }

    public String getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(String v) {
        branchCode = v;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String v) {
        branchName = v;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String v) {
        location = v;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String v) {
        address = v;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String v) {
        status = v;
    }
}
