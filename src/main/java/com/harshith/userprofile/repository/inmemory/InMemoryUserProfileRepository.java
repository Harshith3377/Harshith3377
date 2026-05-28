package com.harshith.userprofile.repository.inmemory;

import com.harshith.userprofile.exception.BusinessLogicNotImplementedException;
import com.harshith.userprofile.model.UserProfile;
import com.harshith.userprofile.repository.UserProfileRepository;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryUserProfileRepository implements UserProfileRepository {

    private final ConcurrentMap<String, UserProfile> profiles = new ConcurrentHashMap<>();

    @Override
    public UserProfile save(UserProfile userProfile) {
        throw notImplemented();
    }

    @Override
    public Optional<UserProfile> findByUserId(String userId) {
        throw notImplemented();
    }

    @Override
    public UserProfile putAttribute(String userId, String key, String value) {
        throw notImplemented();
    }

    @Override
    public void deleteAttribute(String userId, String key) {
        throw notImplemented();
    }

    private BusinessLogicNotImplementedException notImplemented() {
        return new BusinessLogicNotImplementedException(
                "User profile repository behavior is intentionally not implemented yet."
        );
    }

    int reservedCapacityForFutureImplementation() {
        return profiles.size();
    }
}
