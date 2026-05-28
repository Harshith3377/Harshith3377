package com.harshith.userprofile.service.impl;

import com.harshith.userprofile.dto.CreateUserProfileRequest;
import com.harshith.userprofile.dto.UpdateProfileAttributeRequest;
import com.harshith.userprofile.dto.UserProfileResponse;
import com.harshith.userprofile.exception.UserProfileNotFoundException;
import com.harshith.userprofile.model.UserProfile;
import com.harshith.userprofile.repository.UserProfileRepository;
import com.harshith.userprofile.service.UserProfileService;
import java.time.Clock;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserProfileRepository userProfileRepository;
    private final Clock clock;

    public UserProfileServiceImpl(UserProfileRepository userProfileRepository, Clock clock) {
        this.userProfileRepository = userProfileRepository;
        this.clock = clock;
    }

    @Override
    public UserProfileResponse createUser(CreateUserProfileRequest request) {
        validateCreateRequest(request);
        Instant now = clock.instant();
        UserProfile profile = new UserProfile(request.userId(), request.attributes(), now, now);
        return toResponse(userProfileRepository.create(profile));
    }

    @Override
    public UserProfileResponse getUser(String userId) {
        return toResponse(findProfile(userId));
    }

    @Override
    public UserProfileResponse updateUser(
            String userId,
            String key,
            UpdateProfileAttributeRequest request
    ) {
        validateText(key, "key");
        validateUpdateRequest(request);

        String validUserId = validateText(userId, "userId");
        UserProfile updatedProfile = userProfileRepository.update(validUserId, existingProfile -> {
            Map<String, String> updatedAttributes = new HashMap<>(existingProfile.attributes());
            updatedAttributes.put(key, request.value());
            return new UserProfile(
                    existingProfile.userId(),
                    updatedAttributes,
                    existingProfile.createdAt(),
                    clock.instant()
            );
        });
        return toResponse(updatedProfile);
    }

    @Override
    public void deleteUser(String userId) {
        String validUserId = validateText(userId, "userId");
        if (!userProfileRepository.deleteByUserId(validUserId)) {
            throw new UserProfileNotFoundException(validUserId);
        }
    }

    @Override
    public void deleteAttribute(String userId, String key) {
        String validUserId = validateText(userId, "userId");
        String validKey = validateText(key, "key");

        userProfileRepository.update(validUserId, existingProfile -> {
            Map<String, String> updatedAttributes = new HashMap<>(existingProfile.attributes());
            updatedAttributes.remove(validKey);
            return new UserProfile(
                    existingProfile.userId(),
                    updatedAttributes,
                    existingProfile.createdAt(),
                    clock.instant()
            );
        });
    }

    private UserProfile findProfile(String userId) {
        String validUserId = validateText(userId, "userId");
        return userProfileRepository.findByUserId(validUserId)
                .orElseThrow(() -> new UserProfileNotFoundException(validUserId));
    }

    private void validateCreateRequest(CreateUserProfileRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }
        validateText(request.userId(), "userId");
        validateAttributes(request.attributes());
    }

    private void validateUpdateRequest(UpdateProfileAttributeRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("request must not be null");
        }
        if (request.value() == null) {
            throw new IllegalArgumentException("attribute value must not be null");
        }
    }

    private void validateAttributes(Map<String, String> attributes) {
        if (attributes == null) {
            throw new IllegalArgumentException("attributes must not be null");
        }
        attributes.forEach((key, value) -> {
            validateText(key, "attribute key");
            if (value == null) {
                throw new IllegalArgumentException("attribute value must not be null");
            }
        });
    }

    private String validateText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }

    private UserProfileResponse toResponse(UserProfile profile) {
        return new UserProfileResponse(
                profile.userId(),
                profile.attributes(),
                profile.createdAt(),
                profile.updatedAt()
        );
    }
}
