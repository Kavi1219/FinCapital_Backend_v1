package com.fincapital.dto;

import jakarta.validation.constraints.*;

public record JaminRequest(@NotBlank String name, @NotBlank String mobile, String fatherName, String work, @NotBlank String address,
                           String relationship, String profilePhotoUrl, String documentPhotoUrl) {}
