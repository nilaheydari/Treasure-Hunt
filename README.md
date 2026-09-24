# Treasure Hunt

Treasure Hunt is a two-player, console-based game developed in Java as a university project.

Players move around a 10×10 board, collect treasures, avoid traps and walls, and use special abilities to compete against each other. The goal is to reach 100 points or eliminate the opponent by reducing their lives to zero.

## Features

- Two-player gameplay
- 10×10 game board
- Randomly generated treasures, traps, and walls
- Three types of traps:
    - Mouse Trap
    - Bomb
    - TNT
- Breakable and unbreakable walls
- Player health and scoring system
- Special abilities:
    - Long Jump
    - Spawn Trap
    - Destruction
- Random SPIN events
- Colored console output

## How to Play

Players take turns moving around the board using:

- `R` – Right
- `L` – Left
- `U` – Up
- `D` – Down

During a turn, a player can also choose one of the available special abilities.

### Special Abilities

**Long Jump**  
Allows the player to move two cells instead of one.

**Spawn Trap**  
Places a random trap on the board.

**Destruction**  
Allows the player to destroy certain obstacles such as breakable walls and traps.

## Board Elements

| Symbol | Meaning |
|--------|---------|
| `PL1` | Player 1 |
| `PL2` | Player 2 |
| `TRS` | Treasure |
| `MST` | Mouse Trap |
| `BMB` | Bomb |
| `TNT` | TNT |
| `BWL` | Breakable Wall |
| `UWL` | Unbreakable Wall |
| `SPN` | Spin Event |

## Winning the Game

A player wins by:

- Reaching 100 points, or
- Reducing the opponent's lives to zero.

## Technologies

- Java
- Object-Oriented Programming (OOP)
- IntelliJ IDEA

## Project Structure

- `Main.java` – Contains the main game logic and starts the game.
- `GameBoard.java` – Defines the main behaviors required for the game board.

## Running the Project

1. Clone the repository.
2. Open the project in IntelliJ IDEA or another Java IDE.
3. Compile the Java source files.
4. Run `Main.java`.

## About

This project was developed as a university project to practice Java programming and object-oriented programming concepts.