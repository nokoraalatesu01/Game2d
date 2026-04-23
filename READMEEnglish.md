# Whisper Of The Emerald Forest

`Whisper Of The Emerald Forest` is a 2D platformer game built with pure Java SE using `Swing/AWT/Java2D`, without any external game engine. The player takes the role of a knight on a journey to recover the emerald crystal and restore peace to the Emerald Forest.

## Link presentation

https://docs.google.com/presentation/d/1pDiTIHz6L_13rbvOt1VJYRaLFs943rHs/edit?usp=sharing&ouid=117569433518339942759&rtpof=true&sd=true

## This Project Includes

- A start menu with `Play`, `Resolution`, `Intro`, `Guide`, `Leaderboard`, `Credits`, and `Exit`
- Player name input before starting the game
- 2 main gameplay levels and 1 ending level
- Tile-based map movement with collision against walls, ground, and water areas
- Orc enemies that can attack the player
- Health potion drops after defeating enemies
- `Pause`, `Death`, and `The End` screens
- A run timer to track completion time
- Online leaderboard integration with Supabase
- Multiple resolution options and fullscreen mode

## Gameplay Objective

The player controls the main character through multiple stages, defeats all enemies to unlock the path to the next area, avoids drowning in water, survives until the final stage, and touches the `Emerald` to complete the game.

## Controls

- `A` / `D`: move left / right
- `Space`: jump
- `Enter`: attack or confirm menu selection
- `W` / `S`: move up / down in menus
- `Esc`: leave a subpage, return to menu, or trigger pause-related behavior depending on the current screen
- `Backspace`: delete characters when entering the player name

## Technologies Used

- Language: `Java`
- Graphics: `Swing`, `AWT`, `Java2D`
- Level format: `.tmx` maps created with Tiled
- Audio: Java sound playback for music and sound effects
- Score service: `Supabase REST API`

## Main Folder Structure

- `src/`: Java source code
- `src/Main.java`: application entry point
- `src/com/java/WhisperOfTheEmeraldForest/`: core game source code
- `assets/`: images, maps, sprites, and audio assets
- `run.bat`: quick compile-and-run script for Windows
- `out/`: compiled `.class` files

## Project Structure

```text
Game2d/
|- src/
|  |- Main.java
|  `- com/java/WhisperOfTheEmeraldForest/
|     |- Core.java
|     |- GamePanel.java
|     |- entities/
|     |- input/
|     |- screens/
|     |- services/
|     |- tiled/
|     `- util/
|- assets/
|- out/
|- run.bat
`- READMEEnglish.md
```

- `Main.java`: entry point that calls `Core.main(...)`
- `Core.java`: manages the game window, screen switching, timer, resolution, and leaderboard flow
- `GamePanel.java`: handles the game loop, back-buffer rendering, and input capture
- `entities/`: in-game objects such as `Player`, `OrcEnemy`, and `HealthPotion`
- `input/`: keyboard input state management
- `screens/`: start menu, gameplay screens, death screen, pause overlay, and ending screen
- `services/`: leaderboard logic and Supabase communication
- `tiled/`: `.tmx` parsing, tileset handling, and tile map rendering
- `util/`: helper classes for animation, camera, audio, text, and asset loading
- `assets/`: image files, maps, sprite sheets, background music, and sound effects
- `out/`: compiled output directory

## How To Run

### Option 1: Run with the batch file

On Windows:

```bat
run.bat
```

This script will:

1. Compile the source code from `src/` into `out/`
2. Launch the game through the `Main` class

### Option 2: Run with Java commands

```bat
javac -d out -sourcepath src src\Main.java
java -cp out Main
```

## Environment Requirements

- A working `JDK` installation
- `javac` and `java` available in `PATH`
- Windows is the most suitable environment because the project references the system font at `C:/Windows/Fonts/segoeui.ttf`

## Main Gameplay Flow

1. Launch the game and enter the main menu
2. Choose `Play`
3. Enter the player name
4. Clear level 1
5. Defeat all enemies to unlock the next area
6. Clear level 2
7. Reach the final stage and touch the `Emerald`
8. The game saves the completion time and shows the result

## Notable Technical Points

- The game uses a fixed virtual resolution of `800 x 480` to keep the layout consistent
- Supported display modes include `800x480`, `1200x720`, `1600x960`, and `Full Screen`
- The camera follows the player during gameplay
- Tile-based collision is handled manually
- Leaderboard loading and score submission run asynchronously to avoid blocking the UI

## Leaderboard

The project already includes Supabase connection details in the source code. When Internet access is available, the game can:

- fetch high scores
- submit the player's completion time after winning

If the network is unavailable or the service does not respond, the leaderboard may not update, but the offline gameplay still works.

## Team Members

- Nguyen Duc Truong - 241230872
- Nguyen Khac Minh - 241224482
- Tran Huu Long - 241230780

## Summary

This project is a pure Java 2D platformer focused on the core parts of a desktop game: menus, player control, enemies, tile-based maps, collision handling, audio, multiple stages, an ending screen, and online score tracking.
