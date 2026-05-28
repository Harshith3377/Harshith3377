package com.harshith.userprofile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.harshith.userprofile.model.UserProfile;
import com.harshith.userprofile.repository.inmemory.InMemoryUserProfileRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

class InMemoryUserProfileRepositoryTests {

    private final InMemoryUserProfileRepository repository = new InMemoryUserProfileRepository();

    @Test
    void savesAndFindsProfileByUserId() {
        UserProfile profile = new UserProfile("user-123", Map.of("theme", "dark"));

        UserProfile savedProfile = repository.save(profile);

        assertThat(savedProfile).isEqualTo(profile);
        assertThat(repository.findByUserId("user-123")).contains(profile);
    }

    @Test
    void replacesExistingProfileOnSave() {
        repository.save(new UserProfile("user-123", Map.of("theme", "dark")));

        UserProfile replacement = repository.save(new UserProfile("user-123", Map.of("theme", "light")));

        assertThat(replacement.attributes()).containsEntry("theme", "light");
        assertThat(repository.findAll()).containsExactly(replacement);
    }

    @Test
    void returnsImmutableSnapshots() {
        UserProfile profile = repository.save(new UserProfile("user-123", Map.of("theme", "dark")));

        assertThatThrownBy(() -> profile.attributes().put("language", "en"))
                .isInstanceOf(UnsupportedOperationException.class);
        assertThatThrownBy(() -> repository.findAll().add(profile))
                .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void updatesAndDeletesAttributes() {
        repository.save(new UserProfile("user-123", Map.of("theme", "dark")));

        UserProfile updatedProfile = repository.putAttribute("user-123", "language", "en");
        repository.deleteAttribute("user-123", "theme");

        assertThat(updatedProfile.attributes()).containsEntry("language", "en");
        assertThat(repository.findByUserId("user-123"))
                .hasValueSatisfying(profile -> assertThat(profile.attributes())
                        .containsEntry("language", "en")
                        .doesNotContainKey("theme"));
    }

    @Test
    void deletesProfileByUserId() {
        repository.save(new UserProfile("user-123", Map.of("theme", "dark")));

        assertThat(repository.deleteByUserId("user-123")).isTrue();
        assertThat(repository.deleteByUserId("user-123")).isFalse();
        assertThat(repository.findByUserId("user-123")).isEmpty();
    }

    @Test
    void validatesInputs() {
        Map<String, String> attributesWithBlankKey = new HashMap<>();
        attributesWithBlankKey.put(" ", "dark");

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> repository.save(new UserProfile(" ", Map.of())));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> repository.save(new UserProfile("user-123", attributesWithBlankKey)));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> repository.findByUserId(""));
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> repository.putAttribute("user-123", "key", null));
    }

    @Test
    void rejectsAttributeUpdatesForMissingProfiles() {
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> repository.putAttribute("missing-user", "theme", "dark"));
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> repository.deleteAttribute("missing-user", "theme"));
    }

    @Test
    void appliesConcurrentAttributeUpdatesSafely() throws Exception {
        repository.save(new UserProfile("user-123", Map.of()));

        var executor = Executors.newFixedThreadPool(8);
        try {
            var tasks = IntStream.range(0, 100)
                    .<Callable<Void>>mapToObj(index -> () -> {
                        repository.putAttribute("user-123", "key-" + index, "value-" + index);
                        return null;
                    })
                    .toList();

            for (Future<Void> future : executor.invokeAll(tasks)) {
                future.get();
            }
        } finally {
            executor.shutdown();
            assertThat(executor.awaitTermination(5, TimeUnit.SECONDS)).isTrue();
        }

        assertThat(repository.findByUserId("user-123"))
                .hasValueSatisfying(profile -> assertThat(profile.attributes()).hasSize(100));
    }
}
