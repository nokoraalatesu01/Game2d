package com.java.WhisperOfTheEmeraldForest;

import com.java.WhisperOfTheEmeraldForest.entities.Player;
import com.java.WhisperOfTheEmeraldForest.input.InputState;
import com.java.WhisperOfTheEmeraldForest.services.LeaderboardEntry;
import com.java.WhisperOfTheEmeraldForest.services.LeaderboardService;
import com.java.WhisperOfTheEmeraldForest.screens.GameScreen;
import com.java.WhisperOfTheEmeraldForest.screens.GameScreen2;
import com.java.WhisperOfTheEmeraldForest.screens.Screen;
import com.java.WhisperOfTheEmeraldForest.screens.StartScreen;
import com.java.WhisperOfTheEmeraldForest.screens.TheEndScreen;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Core {
    public static final int VIRTUAL_WIDTH = 800;
    public static final int VIRTUAL_HEIGHT = 480;
    private static final String SUPABASE_PROJECT_URL = "https://tvmfauertbelxqngnwof.supabase.co";
    private static final String SUPABASE_API_KEY = "sb_publishable_fW9iKPC_49x00nh5nEWiqw_dItziah-";

    private GamePanel panel;
    private JFrame frame;
    private GraphicsDevice device;
    private Screen currentScreen;
    private Rectangle windowedBounds;
    private boolean fullscreen;
    private String currentPlayerName = "";
    private float currentRunSeconds;
    private boolean runTimerActive;
    private boolean scoreSubmitted;

    public final Player player;
    private final LeaderboardService leaderboardService;

    public Core() {
        this.player = new Player(80f, 120f);
        this.leaderboardService = new LeaderboardService(SUPABASE_PROJECT_URL, SUPABASE_API_KEY);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Core().start());
    }

    private void start() {
        frame = new JFrame("Whisper Of The Emerald Forest");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        panel = new GamePanel(this);
        frame.setContentPane(panel);
        frame.setSize(VIRTUAL_WIDTH, VIRTUAL_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        windowedBounds = frame.getBounds();
        device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

        setScreen(new StartScreen(this));
        panel.start();
    }

    public InputState getInput() {
        return panel.getInput();
    }

    public void setScreen(Screen screen) {
        if (currentScreen != null) {
            currentScreen.dispose();
        }
        currentScreen = screen;
        if (currentScreen != null) {
            currentScreen.onShow();
        }
    }

    public Screen getCurrentScreen() {
        return currentScreen;
    }

    public void update(float delta) {
        if (currentScreen != null) {
            currentScreen.update(delta);
        }
    }

    public void render(java.awt.Graphics2D g) {
        if (currentScreen != null) {
            currentScreen.render(g);
        }
    }

    public void resize(int width, int height) {
        if (currentScreen != null) {
            currentScreen.resize(width, height);
        }
    }

    public void startGame() {
        startGameForPlayer(currentPlayerName);
    }

    public void startGameForPlayer(String playerName) {
        currentPlayerName = sanitizePlayerName(playerName);
        currentRunSeconds = 0f;
        runTimerActive = true;
        scoreSubmitted = false;
        startLevel(1);
    }

    public void startLevel(int level) {
        if (level == 3) {
            player.resetForRespawn(6f, 250f);
            setScreen(new TheEndScreen(this));
        } else if (level == 2) {
            player.resetForRespawn(40f, 140f);
            setScreen(new GameScreen2(this));
        } else {
            player.resetForRespawn(50f, 120f);
            setScreen(new GameScreen(this));
        }
    }

    public void addPlayTime(float delta) {
        if (runTimerActive) {
            currentRunSeconds += delta;
        }
    }

    public void stopRunTimer() {
        runTimerActive = false;
    }

    public void abandonCurrentRun() {
        runTimerActive = false;
    }

    public void submitCurrentScore() {
        if (scoreSubmitted || currentPlayerName.isBlank()) {
            runTimerActive = false;
            return;
        }
        runTimerActive = false;
        scoreSubmitted = true;
        leaderboardService.submitScoreAsync(currentPlayerName, currentRunSeconds);
    }

    public void refreshLeaderboard() {
        leaderboardService.fetchTopScoresAsync(10);
    }

    public List<LeaderboardEntry> getLeaderboardEntries() {
        return leaderboardService.getCachedEntries();
    }

    public String getLeaderboardStatus() {
        return leaderboardService.getStatusMessage();
    }

    public String getCurrentPlayerName() {
        return currentPlayerName;
    }

    public float getCurrentRunSeconds() {
        return currentRunSeconds;
    }

    public String getFormattedRunTime() {
        return formatTime(currentRunSeconds);
    }

    public String formatTime(float seconds) {
        long totalMillis = Math.max(0L, Math.round(seconds * 1000f));
        long minutes = totalMillis / 60000L;
        long remainingMillis = totalMillis % 60000L;
        long secs = remainingMillis / 1000L;
        long millis = remainingMillis % 1000L;
        return String.format("%02d:%02d.%03d", minutes, secs, millis);
    }

    private String sanitizePlayerName(String playerName) {
        if (playerName == null) {
            return "";
        }
        String trimmed = playerName.trim().replaceAll("\\s+", " ");
        return trimmed.length() > 18 ? trimmed.substring(0, 18) : trimmed;
    }

    public void setResolutionSmall() {
        setWindowedSize(800, 480);
    }

    public void setResolutionMedium() {
        setWindowedSize(1200, 720);
    }

    public void setResolutionLarge() {
        setWindowedSize(1600, 960);
    }

    public void setResolutionFullscreen() {
        SwingUtilities.invokeLater(this::enterFullscreenInternal);
    }

    private void setWindowedSize(int width, int height) {
        SwingUtilities.invokeLater(() -> {
            exitFullscreenInternal();
            frame.setSize(width, height);
            frame.setLocationRelativeTo(null);
            windowedBounds = frame.getBounds();
        });
    }

    private void enterFullscreenInternal() {
        if (fullscreen || frame == null || device == null) {
            return;
        }
        windowedBounds = frame.getBounds();
        frame.dispose();
        frame.setUndecorated(true);
        frame.setResizable(false);
        device.setFullScreenWindow(frame);
        frame.setVisible(true);
        fullscreen = true;
    }

    private void exitFullscreenInternal() {
        if (!fullscreen || frame == null || device == null) {
            return;
        }
        device.setFullScreenWindow(null);
        frame.dispose();
        frame.setUndecorated(false);
        frame.setResizable(false);
        if (windowedBounds != null) {
            frame.setBounds(windowedBounds);
        }
        frame.setVisible(true);
        fullscreen = false;
    }
}
