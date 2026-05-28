package com.harshith.userprofile.exception;

public class DuplicateUserProfileException extends RuntimeException {

    public DuplicateUserProfileException(String userId) {
        super("User profile already exists for userId: " + userId);
    }
}
