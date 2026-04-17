package com.java.WhisperOfTheEmeraldForest.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LeaderboardService {
    private static final int NETWORK_TIMEOUT_MS = 8000;
    private static final String USER_AGENT = "WhisperOfTheEmeraldForest/1.0";
    private static final Pattern ENTRY_PATTERN = Pattern.compile(
        "\\{[^\\{\\}]*?\"player_name\"\\s*:\\s*\"((?:\\\\.|[^\\\\\"])*)\"[^\\{\\}]*?"
            + "\"elapsed_ms\"\\s*:\\s*(\\d+)[^\\{\\}]*?"
            + "\"created_at\"\\s*:\\s*\"((?:\\\\.|[^\\\\\"])*)\"[^\\{\\}]*?\\}"
    );

    private final String restBaseUrl;
    private final String apiKey;
    private volatile List<LeaderboardEntry> cachedEntries = Collections.emptyList();
    private volatile String statusMessage = "Leaderboard loading...";

    public LeaderboardService(String projectUrl, String apiKey) {
        this.restBaseUrl = trimTrailingSlash(projectUrl) + "/rest/v1/leaderboard_scores";
        this.apiKey = apiKey == null ? "" : apiKey.trim();
    }

    public void fetchTopScoresAsync(int limit) {
        Thread worker = new Thread(() -> fetchTopScores(limit), "leaderboard-fetch");
        worker.setDaemon(true);
        worker.start();
    }

    public void submitScoreAsync(String playerName, float elapsedSeconds) {
        Thread worker = new Thread(() -> submitScore(playerName, elapsedSeconds), "leaderboard-submit");
        worker.setDaemon(true);
        worker.start();
    }

    public List<LeaderboardEntry> getCachedEntries() {
        return cachedEntries;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    private void fetchTopScores(int limit) {
        if (apiKey.isBlank() || restBaseUrl.contains("your-project-ref")) {
            statusMessage = "Rebuild Supabase URL and API key.";
            cachedEntries = Collections.emptyList();
            return;
        }

        statusMessage = "Leaderboard loading...";
        HttpURLConnection connection = null;
        try {
            int safeLimit = Math.max(1, limit);
            String query = "?select=player_name,elapsed_ms,created_at&order=elapsed_ms.asc,created_at.asc&limit=" + safeLimit;
            URL url = URI.create(restBaseUrl + query).toURL();
            connection = openConnection(url, "GET");

            int code = connection.getResponseCode();
            String responseBody = readResponseBody(connection, code);
            if (code != HttpURLConnection.HTTP_OK) {
                statusMessage = "Leaderboard load failed: HTTP " + code + formatResponseSuffix(responseBody);
                cachedEntries = Collections.emptyList();
                return;
            }

            List<LeaderboardEntry> entries = parseEntries(responseBody);
            cachedEntries = Collections.unmodifiableList(entries);
            statusMessage = entries.isEmpty() ? "Learderboard have no information." : "Top " + 5 + " Best Score";
        } catch (IOException ex) {
            cachedEntries = Collections.emptyList();
            statusMessage = "Cannot load Leaderboard: " + simplifyError(ex);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void submitScore(String playerName, float elapsedSeconds) {
        if (apiKey.isBlank() || restBaseUrl.contains("your-project-ref")) {
            statusMessage = "Hay cau hinh Supabase URL va API key.";
            return;
        }

        HttpURLConnection connection = null;
        try {
            URL url = URI.create(restBaseUrl).toURL();
            connection = openConnection(url, "POST");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Prefer", "return=minimal");
            connection.setDoOutput(true);

            String payload = "{\"player_name\":\"" + escapeJson(playerName)
                + "\",\"elapsed_ms\":" + Math.max(0L, Math.round(elapsedSeconds * 1000f)) + "}";

            try (OutputStream output = connection.getOutputStream()) {
                output.write(payload.getBytes(StandardCharsets.UTF_8));
                output.flush();
            }

            int code = connection.getResponseCode();
            String responseBody = readResponseBody(connection, code);
            if (code == HttpURLConnection.HTTP_CREATED || code == HttpURLConnection.HTTP_OK) {
                statusMessage = "Da luu thanh tich.";
                fetchTopScores(5);
            } else {
                statusMessage = "Luu diem that bai: HTTP " + code + formatResponseSuffix(responseBody);
            }
        } catch (IOException ex) {
            statusMessage = "Luu diem that bai: " + simplifyError(ex) + " Try connect to the internet";
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private HttpURLConnection openConnection(URL url, String method) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(NETWORK_TIMEOUT_MS);
        connection.setReadTimeout(NETWORK_TIMEOUT_MS);
        connection.setRequestMethod(method);
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("apikey", apiKey);
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);
        return connection;
    }

    private List<LeaderboardEntry> parseEntries(String json) {
        if (json == null || json.isBlank() || "[]".equals(json.trim())) {
            return new ArrayList<>();
        }

        List<LeaderboardEntry> entries = new ArrayList<>();
        Matcher matcher = ENTRY_PATTERN.matcher(json);
        while (matcher.find()) {
            long elapsedMillis;
            try {
                elapsedMillis = Long.parseLong(matcher.group(2));
            } catch (NumberFormatException ex) {
                continue;
            }
            entries.add(new LeaderboardEntry(
                unescapeJson(matcher.group(1)),
                elapsedMillis,
                unescapeJson(matcher.group(3))
            ));
        }
        return entries;
    }

    private String readResponseBody(HttpURLConnection connection, int code) {
        InputStream stream = null;
        try {
            stream = code >= 400 ? connection.getErrorStream() : connection.getInputStream();
            if (stream == null) {
                return "";
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                return builder.toString().trim();
            }
        } catch (IOException ex) {
            return "";
        }
    }

    private String formatResponseSuffix(String responseBody) {
        return responseBody.isEmpty() ? "" : " - " + responseBody;
    }

    private String simplifyError(IOException ex) {
        String message = ex.getMessage();
        if (message == null || message.isBlank()) {
            return ex.getClass().getSimpleName();
        }
        return message;
    }

    private String trimTrailingSlash(String value) {
        if (value == null) {
            return "";
        }
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }

    private String escapeJson(String value) {
        return value
            .replace("\\", "\\\\")
            .replace("\"", "\\\"");
    }

    private String unescapeJson(String value) {
        return value
            .replace("\\\"", "\"")
            .replace("\\\\", "\\");
    }
}
