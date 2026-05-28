package com.harshith.userprofile.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateProfileAttributeRequest(
        @NotBlank String value
) {
}
