package com.harshith.userprofile.dto;

import java.util.Map;

public record UserProfileResponse(
        String userId,
        Map<String, String> attributes
) {
}
