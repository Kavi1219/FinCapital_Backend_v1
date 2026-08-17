package com.fincapital.dto;

import jakarta.validation.constraints.*;

public record CreateCompanyRequest(@NotBlank String companyName, @NotBlank String mdName,
                                   @NotBlank String companyMobile, @NotBlank String ownerMobile,
                                   @Email @NotBlank String companyEmail, @NotBlank String branchName,
                                   String branchLocation, @NotBlank String address, @NotBlank String password) {
}
