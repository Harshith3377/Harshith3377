package com.harshith.userprofile.dto;

import java.time.Instant;
import java.util.Map;

public record UserProfileResponse(
        String userId,
        Map<String, String> attributes,
        Instant createdAt,
        Instant updatedAt
) {
}
