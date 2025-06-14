package com.example.proyekakhirpam;

public class Teman {
    private String id;
    private String username;
    private String email;
    private String tag;
    private boolean isFollowed;

    public Teman() {
        // Required for Firestore
    }

    public Teman(String id, String username, String email, String tag, boolean isFollowed) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.tag = tag;
        this.isFollowed = isFollowed;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getTag() { return tag; }
    public boolean isFollowed() { return isFollowed; }

    public void setFollowed(boolean followed) { isFollowed = followed; }
    public void setTag(String tag) { this.tag = tag; }
}
