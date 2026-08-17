package com.fincapital.dto;

public record BranchResponse(Long id, Long companyId, String branchCode, String branchName, String location,
                             String address, String status) {
}
