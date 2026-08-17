package com.fincapital.dto;

import jakarta.validation.constraints.*;

public record CreateBranchRequest(@NotNull Long companyId, @NotBlank String branchName, String location,
                                  String address) {
}
