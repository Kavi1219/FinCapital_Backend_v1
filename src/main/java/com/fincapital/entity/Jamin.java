package com.fincapital.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "jamins")
public class Jamin extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @Column(name = "jamin_name", nullable = false, length = 150)
    private String jaminName;
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
    @Column(name = "relationship", length = 100)
    private String relationship;

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer v) {
        customer = v;
    }

    public String getJaminName() {
        return jaminName;
    }

    public void setJaminName(String v) {
        jaminName = v;
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

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String v) {
        relationship = v;
    }
}
