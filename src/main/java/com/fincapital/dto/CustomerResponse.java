package com.fincapital.dto;

public record CustomerResponse(Long id, Long companyId, Long branchId, String customerCode, String customerName,
                               String mobile, String work, String address, String profilePhotoUrl,
                               String documentPhotoUrl, String status, String jaminName, String jaminMobile) {
}
