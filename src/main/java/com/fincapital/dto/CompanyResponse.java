package com.fincapital.dto;

public record CompanyResponse(Long id, String companyCode, String companyName, String mdName, String companyMobile,
                              String ownerMobile, String companyEmail, String address, String status,
                              Long defaultBranchId) {
}
