package com.harshith.userprofile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.harshith.userprofile.dto.CreateUserProfileRequest;
import com.harshith.userprofile.dto.UpdateProfileAttributeRequest;
import com.harshith.userprofile.exception.DuplicateUserProfileException;
import com.harshith.userprofile.exception.UserProfileNotFoundException;
import com.harshith.userprofile.repository.inmemory.InMemoryUserProfileRepository;
import com.harshith.userprofile.service.impl.UserProfileServiceImpl;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Map;
import org.junit.jupiter.api.Test;

class UserProfileServiceImplTests {

    private static final Instant CREATED_AT = Instant.parse("2026-05-28T20:00:00Z");
    private static final Instant UPDATED_AT = Instant.parse("2026-05-28T20:05:00Z");

    private final InMemoryUserProfileRepository repository = new InMemoryUserProfileRepository();

    @Test
    void createsUserWithTimestamps() {
        UserProfileServiceImpl service = serviceAt(CREATED_AT);

        var response = service.createUser(new CreateUserProfileRequest("user-123", Map.of("theme", "dark")));

        assertThat(response.userId()).isEqualTo("user-123");
        assertThat(response.attributes()).containsEntry("theme", "dark");
        assertThat(response.createdAt()).isEqualTo(CREATED_AT);
        assertThat(response.updatedAt()).isEqualTo(CREATED_AT);
    }

    @Test
    void preventsDuplicateUserIds() {
        UserProfileServiceImpl service = serviceAt(CREATED_AT);
        service.createUser(new CreateUserProfileRequest("user-123", Map.of()));

        assertThatExceptionOfType(DuplicateUserProfileException.class)
                .isThrownBy(() -> service.createUser(new CreateUserProfileRequest("user-123", Map.of())));
    }

    @Test
    void getsExistingUserAndRejectsMissingUser() {
        UserProfileServiceImpl service = serviceAt(CREATED_AT);
        service.createUser(new CreateUserProfileRequest("user-123", Map.of("theme", "dark")));

        assertThat(service.getUser("user-123").attributes()).containsEntry("theme", "dark");
        assertThatExceptionOfType(UserProfileNotFoundException.class)
                .isThrownBy(() -> service.getUser("missing-user"));
    }

    @Test
    void updatesExistingKeyOrCreatesNewKeyAndRefreshesUpdatedTimestamp() {
        serviceAt(CREATED_AT).createUser(new CreateUserProfileRequest("user-123", Map.of("theme", "dark")));
        UserProfileServiceImpl updateService = serviceAt(UPDATED_AT);

        var firstUpdate = updateService.updateUser(
                "user-123",
                "theme",
                new UpdateProfileAttributeRequest("light")
        );
        var secondUpdate = updateService.updateUser(
                "user-123",
                "language",
                new UpdateProfileAttributeRequest("en")
        );

        assertThat(firstUpdate.attributes()).containsEntry("theme", "light");
        assertThat(secondUpdate.attributes())
                .containsEntry("theme", "light")
                .containsEntry("language", "en");
        assertThat(secondUpdate.createdAt()).isEqualTo(CREATED_AT);
        assertThat(secondUpdate.updatedAt()).isEqualTo(UPDATED_AT);
    }

    @Test
    void deletesUser() {
        UserProfileServiceImpl service = serviceAt(CREATED_AT);
        service.createUser(new CreateUserProfileRequest("user-123", Map.of()));

        service.deleteUser("user-123");

        assertThatExceptionOfType(UserProfileNotFoundException.class)
                .isThrownBy(() -> service.getUser("user-123"));
    }

    @Test
    void rejectsInvalidRequests() {
        UserProfileServiceImpl service = serviceAt(CREATED_AT);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> service.createUser(null));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> service.updateUser("user-123", "key", null));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> service.deleteUser(" "));
    }

    private UserProfileServiceImpl serviceAt(Instant instant) {
        Clock clock = Clock.fixed(instant, ZoneOffset.UTC);
        return new UserProfileServiceImpl(repository, clock);
    }
}
