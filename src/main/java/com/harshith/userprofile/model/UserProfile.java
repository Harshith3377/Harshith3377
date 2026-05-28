package com.harshith.userprofile.model;

import java.time.Instant;
import java.util.Map;
import java.util.Objects;

public record UserProfile(
        String userId,
        Map<String, String> attributes,
        Instant createdAt,
        Instant updatedAt
) {
    public UserProfile {
        attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
        createdAt = Objects.requireNonNull(createdAt, "createdAt must not be null");
        updatedAt = Objects.requireNonNull(updatedAt, "updatedAt must not be null");
    }

    public UserProfile(String userId, Map<String, String> attributes) {
        this(userId, attributes, Instant.EPOCH, Instant.EPOCH);
    }
}
