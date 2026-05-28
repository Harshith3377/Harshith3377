package com.harshith.userprofile.service.impl;

import com.harshith.userprofile.dto.CreateUserProfileRequest;
import com.harshith.userprofile.dto.UpdateProfileAttributeRequest;
import com.harshith.userprofile.dto.UserProfileResponse;
import com.harshith.userprofile.exception.BusinessLogicNotImplementedException;
import com.harshith.userprofile.repository.UserProfileRepository;
import com.harshith.userprofile.service.UserProfileService;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository) {
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public UserProfileResponse createProfile(CreateUserProfileRequest request) {
        throw notImplemented();
    }

    @Override
    public UserProfileResponse getProfile(String userId) {
        throw notImplemented();
    }

    @Override
    public UserProfileResponse upsertAttribute(
            String userId,
            String key,
            UpdateProfileAttributeRequest request
    ) {
        throw notImplemented();
    }

    @Override
    public void deleteAttribute(String userId, String key) {
        throw notImplemented();
    }

    private BusinessLogicNotImplementedException notImplemented() {
        return new BusinessLogicNotImplementedException(
                "User profile service behavior is intentionally not implemented yet."
        );
    }

    UserProfileRepository repository() {
        return userProfileRepository;
    }
}
