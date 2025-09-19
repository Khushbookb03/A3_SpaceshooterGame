# Space Shooter (Java Swing) — Assignment

## Overview
A 2D Space Shooter game built with Java Swing. Multiple panels (Welcome, Level selection, Game, Game Over) navigated using CardLayout. Uses Graphics2D for rendering, Swing Timers for animation, KeyListener for controls, and Clip for audio effects.

## Controls
- Left Arrow / Right Arrow: Move spaceship left/right
- Spacebar: Shoot bullet
- P: Pause / Resume
- Buttons: Use GUI buttons to navigate panels

## Difficulty Effects
- EASY: fewer enemies, slower spawn rate, more allowed passes
- MEDIUM: balanced
- HARD: more enemies, quicker spawn, fewer allowed passes

## Files
- `Main.java` — Program entry point; sets up CardLayout and panels.
- `WelcomePanel.java` — Player name entry + background.
- `LevelPanel.java` — Difficulty selection.
- `GamePanel.java` — Main game loop, drawing, collisions, audio.
- `GameOverPanel.java` — Shows final score and options.
- `Difficulty.java` — Enum for difficulty levels.
- `README.md` — this file.

## Assets (optional)
Put these in an `assets/` folder:
- `space_bg.jpg` — welcome screen background
- `player.png` — player ship image (recommended ~60x30)
- `enemy.png` — enemy image
- `shoot.wav` — shooting sound (short)
- `explosion.wav` — explosion sound
- `bg_music.wav` — background music (loopable)

The code will fall back to shape-drawn graphics if assets are not present.

## How to compile & run
1. Ensure JDK 8+ is installed.
2. Place `.java` files in a single folder (and optional `assets/`).
3. Compile: `javac *.java`
4. Run: `java Main`

## Design notes
- `CardLayout` used to manage navigation between panels.
- `GamePanel` uses two `Timer`s: one for per-frame updates (~60 FPS), one for periodic enemy spawning.
- Bullets and enemies are simple data classes for clarity.
- Collisions detected via simple rectangle intersection (fast & easy).
- Audio handled via `javax.sound.sampled.Clip`. If audio files are missing or unsupported, game continues silently.
- Code is written for clarity and teaching: modular, commented, and easy to extend.

## Possible extensions / improvements (extra credit)
- Add multiple enemy types and boss waves.
- Add power-ups (rapid fire, shields).
- Improve animations and particle effects on explosion.
- Add high-score saving to disk.
- Add better sprite-sheet animation for enemies.

Good luck! If you want, I can:
- produce a single `.zip` with all .java files and placeholder assets,
- or convert this into a single-file assignment (all classes inside one .java) if your submission needs one file.
"# A3_SpaceshooterGame" 
