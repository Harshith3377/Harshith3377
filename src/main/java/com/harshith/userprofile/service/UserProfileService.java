package com.harshith.userprofile.service;

import com.harshith.userprofile.dto.CreateUserProfileRequest;
import com.harshith.userprofile.dto.UpdateProfileAttributeRequest;
import com.harshith.userprofile.dto.UserProfileResponse;

public interface UserProfileService {

    UserProfileResponse createUser(CreateUserProfileRequest request);

    UserProfileResponse getUser(String userId);

    UserProfileResponse updateUser(String userId, String key, UpdateProfileAttributeRequest request);

    void deleteUser(String userId);

    void deleteAttribute(String userId, String key);
}
