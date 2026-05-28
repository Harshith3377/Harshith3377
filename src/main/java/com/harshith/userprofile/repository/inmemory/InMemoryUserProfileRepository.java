package com.harshith.userprofile.repository.inmemory;

import com.harshith.userprofile.exception.DuplicateUserProfileException;
import com.harshith.userprofile.exception.UserProfileNotFoundException;
import com.harshith.userprofile.model.UserProfile;
import com.harshith.userprofile.repository.UserProfileRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

/**
 * Thread-safe in-memory implementation of {@link UserProfileRepository}.
 *
 * <p>This repository is intended for local development and tests. It stores
 * immutable {@link UserProfile} snapshots in a {@link ConcurrentHashMap};
 * per-profile attribute changes are applied atomically through map compute
 * operations.</p>
 */
@Repository
public class InMemoryUserProfileRepository implements UserProfileRepository {

    private final ConcurrentMap<String, UserProfile> profiles = new ConcurrentHashMap<>();

    /**
     * Creates a profile snapshot unless the user id is already present.
     *
     * @param userProfile profile to persist
     * @return persisted immutable profile snapshot
     */
    @Override
    public UserProfile create(UserProfile userProfile) {
        UserProfile validProfile = validateProfile(userProfile);
        UserProfile previousProfile = profiles.putIfAbsent(validProfile.userId(), validProfile);
        if (previousProfile != null) {
            throw new DuplicateUserProfileException(validProfile.userId());
        }
        return validProfile;
    }

    /**
     * Creates or replaces a profile snapshot for the given user id.
     *
     * @param userProfile profile to persist
     * @return persisted immutable profile snapshot
     */
    @Override
    public UserProfile save(UserProfile userProfile) {
        UserProfile validProfile = validateProfile(userProfile);
        profiles.put(validProfile.userId(), validProfile);
        return validProfile;
    }

    /**
     * Finds a profile by user id.
     *
     * @param userId unique user identifier
     * @return matching profile, or an empty optional when absent
     */
    @Override
    public Optional<UserProfile> findByUserId(String userId) {
        return Optional.ofNullable(profiles.get(validateText(userId, "userId")));
    }

    /**
     * Returns a weakly consistent immutable snapshot of all profiles currently stored.
     *
     * @return profile snapshots
     */
    @Override
    public List<UserProfile> findAll() {
        return List.copyOf(profiles.values());
    }

    /**
     * Atomically updates an existing profile.
     *
     * @param userId unique user identifier
     * @param updater update function that receives the current profile snapshot
     * @return updated immutable profile snapshot
     */
    @Override
    public UserProfile update(String userId, UnaryOperator<UserProfile> updater) {
        String validUserId = validateText(userId, "userId");
        UnaryOperator<UserProfile> validUpdater = Objects.requireNonNull(
                updater,
                "updater must not be null"
        );

        return profiles.compute(validUserId, (ignored, existingProfile) -> {
            if (existingProfile == null) {
                throw profileNotFound(validUserId);
            }

            UserProfile updatedProfile = validateProfile(validUpdater.apply(existingProfile));
            if (!validUserId.equals(updatedProfile.userId())) {
                throw new IllegalArgumentException("updated userId must match existing userId");
            }
            return updatedProfile;
        });
    }

    /**
     * Adds or replaces a single attribute on an existing profile.
     *
     * @param userId unique user identifier
     * @param key attribute key
     * @param value attribute value
     * @return updated immutable profile snapshot
     */
    @Override
    public UserProfile putAttribute(String userId, String key, String value) {
        String validKey = validateText(key, "key");
        String validValue = validateAttributeValue(value);

        return update(userId, existingProfile -> {
            Map<String, String> updatedAttributes = new HashMap<>(existingProfile.attributes());
            updatedAttributes.put(validKey, validValue);
            return new UserProfile(
                    existingProfile.userId(),
                    updatedAttributes,
                    existingProfile.createdAt(),
                    existingProfile.updatedAt()
            );
        });
    }

    /**
     * Deletes a single attribute from an existing profile.
     *
     * @param userId unique user identifier
     * @param key attribute key
     */
    @Override
    public void deleteAttribute(String userId, String key) {
        String validKey = validateText(key, "key");

        update(userId, existingProfile -> {
            Map<String, String> updatedAttributes = new HashMap<>(existingProfile.attributes());
            updatedAttributes.remove(validKey);
            return new UserProfile(
                    existingProfile.userId(),
                    updatedAttributes,
                    existingProfile.createdAt(),
                    existingProfile.updatedAt()
            );
        });
    }

    /**
     * Deletes a complete profile.
     *
     * @param userId unique user identifier
     * @return {@code true} when a profile existed and was removed
     */
    @Override
    public boolean deleteByUserId(String userId) {
        return profiles.remove(validateText(userId, "userId")) != null;
    }

    private UserProfile validateProfile(UserProfile userProfile) {
        if (userProfile == null) {
            throw new IllegalArgumentException("userProfile must not be null");
        }

        String validUserId = validateText(userProfile.userId(), "userId");
        Map<String, String> attributes = userProfile.attributes();
        if (attributes == null) {
            throw new IllegalArgumentException("attributes must not be null");
        }

        attributes.forEach((key, value) -> {
            validateText(key, "attribute key");
            validateAttributeValue(value);
        });

        return new UserProfile(
                validUserId,
                attributes,
                userProfile.createdAt(),
                userProfile.updatedAt()
        );
    }

    private String validateText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " must not be blank");
        }
        return value;
    }

    private String validateAttributeValue(String value) {
        if (value == null) {
            throw new IllegalArgumentException("attribute value must not be null");
        }
        return value;
    }

    private UserProfileNotFoundException profileNotFound(String userId) {
        return new UserProfileNotFoundException(userId);
    }
}
