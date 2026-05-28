package com.harshith.userprofile.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record CreateUserProfileRequest(
        @NotBlank String userId,
        @NotNull Map<String, String> attributes
) {
}
