package com.fincapital.dto;

import java.time.LocalDateTime;

public record CustomerResponse(Long id, Long companyId, Long branchId, String customerCode, String customerName,
                               String mobile, String fatherName, String work, String address, String profilePhotoUrl,
                               String documentPhotoUrl, String status, LocalDateTime createdAt, String jaminName,
                               String jaminMobile, String jaminFatherName, String jaminWork, String jaminAddress,
                               String jaminProfilePhotoUrl, String jaminDocumentPhotoUrl) {}
