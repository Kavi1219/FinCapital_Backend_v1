package com.fincapital.dto;

import jakarta.validation.constraints.*;

public record RegisterAgentRequest(@NotNull Long companyId, @NotNull Long branchId, @NotBlank String agentName,
                                   @NotBlank String mobile, @NotBlank String password,
                                   @NotBlank String profilePhotoUrl) {
}
