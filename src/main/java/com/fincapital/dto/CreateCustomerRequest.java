package com.fincapital.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

public record CreateCustomerRequest(@NotNull Long companyId, @NotNull Long branchId, @NotBlank String name,
                                    @NotBlank String mobile, String fatherName, String work, @NotBlank String address,
                                    String profilePhotoUrl, String documentPhotoUrl, Long createdByAgentId,
                                    Long createdByUserId, @Valid @NotNull JaminRequest jamin) {}
