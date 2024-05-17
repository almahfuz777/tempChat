package com.example.tempchat;

public class Message {
    private String username;
    private String message;
    private long timestamp;

    public Message() {
        // No-argument constructor needed for Firestore serialization
    }

    public Message(String username, String message, long timestamp) {
        this.username = username;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
