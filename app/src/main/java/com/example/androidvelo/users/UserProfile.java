package com.example.androidvelo.users;

public class UserProfile {
    private String username;
    private String profileImage;

    public UserProfile() {
        // Пустой конструктор требуется для работы с Firebase
    }

    public UserProfile(String username, String profileImage) {
        this.username = username;
        this.profileImage = profileImage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
