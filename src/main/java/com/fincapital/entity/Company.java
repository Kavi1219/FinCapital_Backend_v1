package com.fincapital.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "companies")
public class Company extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "company_code", nullable = false, unique = true, length = 20)
    private String companyCode;
    @Column(name = "company_name", nullable = false, length = 150)
    private String companyName;
    @Column(name = "md_name", nullable = false, length = 150)
    private String mdName;
    @Column(name = "company_mobile", nullable = false, unique = true, length = 20)
    private String companyMobile;
    @Column(name = "owner_mobile", nullable = false, length = 20)
    private String ownerMobile;
    @Column(name = "company_email", nullable = false, unique = true, length = 180)
    private String companyEmail;
    @Column(name = "address", nullable = false, length = 500)
    private String address;
    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE";

    public Long getId() {
        return id;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String v) {
        companyCode = v;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String v) {
        companyName = v;
    }

    public String getMdName() {
        return mdName;
    }

    public void setMdName(String v) {
        mdName = v;
    }

    public String getCompanyMobile() {
        return companyMobile;
    }

    public void setCompanyMobile(String v) {
        companyMobile = v;
    }

    public String getOwnerMobile() {
        return ownerMobile;
    }

    public void setOwnerMobile(String v) {
        ownerMobile = v;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String v) {
        companyEmail = v;
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
