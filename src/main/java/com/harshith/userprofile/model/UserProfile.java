package com.harshith.userprofile.model;

import java.util.Map;

public record UserProfile(
        String userId,
        Map<String, String> attributes
) {
    public UserProfile {
        attributes = attributes == null ? Map.of() : Map.copyOf(attributes);
    }
}
