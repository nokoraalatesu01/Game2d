package com.java.WhisperOfTheEmeraldForest.services;

public class LeaderboardEntry {
    private final String playerName;
    private final long elapsedMillis;
    private final String createdAt;

    public LeaderboardEntry(String playerName, long elapsedMillis, String createdAt) {
        this.playerName = playerName;
        this.elapsedMillis = elapsedMillis;
        this.createdAt = createdAt;
    }

    public String getPlayerName() {
        return playerName;
    }

    public long getElapsedMillis() {
        return elapsedMillis;
    }

    public String getCreatedAt() {
        return createdAt;
    }
}
