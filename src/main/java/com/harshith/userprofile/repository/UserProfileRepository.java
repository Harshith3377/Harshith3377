package com.harshith.userprofile.repository;

import com.harshith.userprofile.model.UserProfile;
import java.util.Optional;

public interface UserProfileRepository {

    UserProfile save(UserProfile userProfile);

    Optional<UserProfile> findByUserId(String userId);

    UserProfile putAttribute(String userId, String key, String value);

    void deleteAttribute(String userId, String key);
}
