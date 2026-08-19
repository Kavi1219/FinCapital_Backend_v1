package com.fincapital.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jamin_id")
    private Jamin jamin;
    @Column(name = "owner_type", nullable = false, length = 20)
    private String ownerType = "CUSTOMER";
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agent_id")
    private Agent agent;
    @Column(name = "document_type", nullable = false, length = 50)
    private String documentType;
    @Column(name = "document_name", length = 250)
    private String documentName;
    @Column(name = "file_url", nullable = false, length = 1000)
    private String fileUrl;
    @Column(name = "mime_type", length = 100)
    private String mimeType;
    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    public Long getId() { return id; }
    public Company getCompany() { return company; }
    public void setCompany(Company v) { company = v; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer v) { customer = v; }
    public Jamin getJamin() { return jamin; }
    public void setJamin(Jamin v) { jamin = v; }
    public Agent getAgent() { return agent; }
    public void setAgent(Agent v) { agent = v; }
    public String getOwnerType() { return ownerType; }
    public void setOwnerType(String v) { ownerType = v; }
    public String getDocumentType() { return documentType; }
    public void setDocumentType(String v) { documentType = v; }
    public String getDocumentName() { return documentName; }
    public void setDocumentName(String v) { documentName = v; }
    public String getFileUrl() { return fileUrl; }
    public void setFileUrl(String v) { fileUrl = v; }
    public String getMimeType() { return mimeType; }
    public void setMimeType(String v) { mimeType = v; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }

    @PrePersist
    void prePersist() {
        if (uploadedAt == null) uploadedAt = LocalDateTime.now();
    }
}
