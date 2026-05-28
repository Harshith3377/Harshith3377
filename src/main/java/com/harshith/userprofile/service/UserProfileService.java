package com.harshith.userprofile.service;

import com.harshith.userprofile.dto.CreateUserProfileRequest;
import com.harshith.userprofile.dto.UpdateProfileAttributeRequest;
import com.harshith.userprofile.dto.UserProfileResponse;

public interface UserProfileService {

    UserProfileResponse createProfile(CreateUserProfileRequest request);

    UserProfileResponse getProfile(String userId);

    UserProfileResponse upsertAttribute(String userId, String key, UpdateProfileAttributeRequest request);

    void deleteAttribute(String userId, String key);
}
