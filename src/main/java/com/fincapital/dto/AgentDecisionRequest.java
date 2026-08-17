package com.fincapital.dto;

public record AgentDecisionRequest(Long approvedByUserId, String rejectionReason) {
}
