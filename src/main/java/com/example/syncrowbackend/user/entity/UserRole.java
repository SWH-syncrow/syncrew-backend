package com.example.syncrowbackend.user.entity;

public enum UserRole {
    USER, ADMIN;

    public static UserRole from(String role) {
        return UserRole.valueOf(role);
    }
}
