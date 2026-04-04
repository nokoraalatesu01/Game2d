# 2DRPG (Java SE, Swing/AWT)

This project is a pure Java SE (Swing/AWT/Java2D) game with no external dependencies.

## Structure

- `src/com/...` Java source code
- `assets/` Game assets
- `src/Main.java` Entry point

## Compile + Run

From the project root:

```bash
javac -d . -sourcepath src src/Main.java
java Main
```

Notes:
- The compile command outputs class files into the project root (`./com/...`) so `java Main` works.
- If you prefer a separate output folder:

```bash
javac -d out -sourcepath src src/Main.java
java -cp out Main
```

## Leaderboard with Supabase

The leaderboard now uses Supabase directly from the Java client.

Setup:

1. Create a Supabase project.
2. Create a table named `leaderboard_scores` with columns `id`, `player_name`, `elapsed_ms`, `created_at`.
3. In [Core.java](/C:/Users/Lenovo/Desktop/Game2d/src/com/java/WhisperOfTheEmeraldForest/Core.java), set `SUPABASE_PROJECT_URL` and `SUPABASE_API_KEY`.
4. Keep `RLS` disabled while testing, or add policies that allow `select` and `insert` for your chosen key.
5. Build and run the game. Winning a run will submit the score and refresh the leaderboard.
