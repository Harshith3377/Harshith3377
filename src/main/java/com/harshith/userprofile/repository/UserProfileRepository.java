package com.harshith.userprofile.repository;

import com.harshith.userprofile.model.UserProfile;
import java.util.List;
import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * Persistence abstraction for user profile key-value data.
 */
public interface UserProfileRepository {

    /**
     * Creates a user profile when the user id is not already present.
     *
     * @param userProfile profile to persist
     * @return persisted immutable profile snapshot
     * @throws IllegalArgumentException when the profile is invalid
     * @throws com.harshith.userprofile.exception.DuplicateUserProfileException when the user id already exists
     */
    UserProfile create(UserProfile userProfile);

    /**
     * Creates or replaces a user profile.
     *
     * @param userProfile profile to persist
     * @return persisted immutable profile snapshot
     * @throws IllegalArgumentException when the profile is invalid
     */
    UserProfile save(UserProfile userProfile);

    /**
     * Finds a profile by user id.
     *
     * @param userId unique user identifier
     * @return matching profile, or an empty optional when absent
     * @throws IllegalArgumentException when the user id is invalid
     */
    Optional<UserProfile> findByUserId(String userId);

    /**
     * Returns a weakly consistent snapshot of all stored profiles.
     *
     * @return immutable list of profile snapshots
     */
    List<UserProfile> findAll();

    /**
     * Atomically updates an existing profile.
     *
     * @param userId unique user identifier
     * @param updater update function that receives the current profile snapshot
     * @return updated immutable profile snapshot
     * @throws IllegalArgumentException when any input or output is invalid
     * @throws com.harshith.userprofile.exception.UserProfileNotFoundException when the profile does not exist
     */
    UserProfile update(String userId, UnaryOperator<UserProfile> updater);

    /**
     * Adds or replaces a single profile attribute.
     *
     * @param userId unique user identifier
     * @param key attribute key
     * @param value attribute value
     * @return updated immutable profile snapshot
     * @throws IllegalArgumentException when any input is invalid
     * @throws java.util.NoSuchElementException when the profile does not exist
     */
    UserProfile putAttribute(String userId, String key, String value);

    /**
     * Deletes a single profile attribute.
     *
     * @param userId unique user identifier
     * @param key attribute key
     * @throws IllegalArgumentException when any input is invalid
     * @throws java.util.NoSuchElementException when the profile does not exist
     */
    void deleteAttribute(String userId, String key);

    /**
     * Deletes a complete user profile.
     *
     * @param userId unique user identifier
     * @return {@code true} when a profile was removed, otherwise {@code false}
     * @throws IllegalArgumentException when the user id is invalid
     */
    boolean deleteByUserId(String userId);
}
