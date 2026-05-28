package com.harshith.userprofile.exception;

public class UserProfileNotFoundException extends RuntimeException {

    public UserProfileNotFoundException(String userId) {
        super("User profile not found for userId: " + userId);
    }
}
