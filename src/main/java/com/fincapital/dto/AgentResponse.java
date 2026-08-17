package com.fincapital.dto;

public record AgentResponse(Long id, Long companyId, Long branchId, String employeeCode, String agentName,
                            String mobile, String profilePhotoUrl, Boolean mobileVerified, String approvalStatus,
                            String rejectionReason) {
}
