

# Battleship Game

A classic game of Battleship where the player goes head-to-head against the computer. Each player gets a 10x10 board and can place 4 ships, each of size 4x1 (either vertically or horizontally). Players then take turns trying to sink each other's ships by guessing the coordinates.

## Getting Started

### Prerequisites

- Java Development Kit (JDK)

### Compilation and Execution

1. Save the game code in a file named `BattleshipGame.java`.
2. Compile the code:
```
javac BattleshipGame.java
```
3. Run the game:
```
java BattleshipGame
```

## How to Play

1. **Setting up your ships**:
    - At the beginning of the game, you will be prompted to set the coordinates for each of your 4 ships.
    - Ships can be placed vertically or horizontally.
    - Example coordinate format: `A0` or `B3`.
    
2. **Guessing Phase**:
    - You'll attempt to guess the coordinates of the computer's ships.
    - The computer will also guess coordinates for your ships.
    - Hits are represented with an `X`, and misses with an `O`.
    
3. **Game Over**:
    - The game ends when all ships of a player are sunk.
    - A message is displayed announcing the winner.

## Features

- Interactive coordinate input for ship placement.
- Ships are displayed on your board but are hidden on the computer's board. Only hits and misses are shown on the computer's board.
- Randomized ship placement and guessing by the computer.

