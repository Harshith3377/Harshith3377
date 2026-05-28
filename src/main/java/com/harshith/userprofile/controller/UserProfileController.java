package com.harshith.userprofile.controller;

import com.harshith.userprofile.dto.CreateUserProfileRequest;
import com.harshith.userprofile.dto.UpdateProfileAttributeRequest;
import com.harshith.userprofile.dto.UserProfileResponse;
import com.harshith.userprofile.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profiles")
@Tag(name = "User Profiles", description = "User profile key-value store API")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PostMapping
    @Operation(summary = "Create a user profile")
    public ResponseEntity<UserProfileResponse> createProfile(
            @Valid @RequestBody CreateUserProfileRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userProfileService.createUser(request));
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get a user profile by user id")
    public ResponseEntity<UserProfileResponse> getProfile(@PathVariable String userId) {
        return ResponseEntity.ok(userProfileService.getUser(userId));
    }

    @PutMapping("/{userId}/attributes/{key}")
    @Operation(summary = "Create or update a profile attribute")
    public ResponseEntity<UserProfileResponse> updateAttribute(
            @PathVariable String userId,
            @PathVariable String key,
            @Valid @RequestBody UpdateProfileAttributeRequest request
    ) {
        return ResponseEntity.ok(userProfileService.updateUser(userId, key, request));
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete a user profile")
    public ResponseEntity<Void> deleteProfile(@PathVariable String userId) {
        userProfileService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}/attributes/{key}")
    @Operation(summary = "Delete a profile attribute")
    public ResponseEntity<Void> deleteAttribute(
            @PathVariable String userId,
            @PathVariable String key
    ) {
        userProfileService.deleteAttribute(userId, key);
        return ResponseEntity.noContent().build();
    }
}
