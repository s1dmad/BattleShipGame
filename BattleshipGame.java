import java.util.Random;
import java.util.Scanner;

public class BattleshipGame {

    public static final int GRID_SIZE = 10;
    public static final int SHIP_SIZE = 4;
    public static final int TOTAL_SHIPS = 4;

    class Ship {
        int startX;
        int startY;
        boolean isVertical;
        int hits;

        Ship(int startX, int startY, boolean isVertical) {
            this.startX = startX;
            this.startY = startY;
            this.isVertical = isVertical;
            this.hits = 0;
        }

        boolean occupies(int x, int y) {
            if (isVertical) {
                return x >= startX && x < startX + SHIP_SIZE && y == startY;
            } else {
                return y >= startY && y < startY + SHIP_SIZE && x == startX;
            }
        }
        
        boolean isSunk() {
            return hits == SHIP_SIZE;
        }
    }

    char[][] humanBoard = new char[GRID_SIZE][GRID_SIZE];
    char[][] computerDisplayBoard = new char[GRID_SIZE][GRID_SIZE];
    char[][] computerActualBoard = new char[GRID_SIZE][GRID_SIZE];
    Ship[] humanShips = new Ship[TOTAL_SHIPS];
    Ship[] computerShips = new Ship[TOTAL_SHIPS];
    Random rand = new Random();
    Scanner scanner = new Scanner(System.in);

    public BattleshipGame() {
        initializeBoard(humanBoard);
        initializeBoard(computerDisplayBoard);
        initializeBoard(computerActualBoard);
    }

    void initializeBoard(char[][] board) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                board[i][j] = '-';
            }
        }
    }

    void deployUserShips() {
        for (int i = 0; i < TOTAL_SHIPS; i++) {
            System.out.println("Deploy ship #" + (i + 1) + " (size: " + SHIP_SIZE + ").");
            System.out.print("Enter start coordinate (e.g. A0): ");
            String start = scanner.nextLine().toUpperCase();

            int startX = start.charAt(0) - 'A';
            int startY;

            try {
                startY = Integer.parseInt(start.substring(1));
            } catch (NumberFormatException e) {
                System.out.println("Invalid placement. Try again.");
                i--; // Retry the same ship placement
                continue;
            }

            System.out.print("Is the ship vertical? (yes/no): ");
            boolean isVertical = scanner.nextLine().trim().equalsIgnoreCase("yes");

            if (isValidPlacement(startX, startY, isVertical, humanBoard)) {
                humanShips[i] = new Ship(startX, startY, isVertical);
                markBoard(humanShips[i], humanBoard);
            } else {
                System.out.println("Invalid placement. Try again.");
                i--; // Retry the same ship placement
            }
        }
    }

    void deployComputerShips() {
        for (int i = 0; i < TOTAL_SHIPS; i++) {
            boolean placed = false;
            while (!placed) {
                int x = rand.nextInt(GRID_SIZE);
                int y = rand.nextInt(GRID_SIZE);
                boolean isVertical = rand.nextBoolean();
                if (isValidPlacement(x, y, isVertical, computerActualBoard)) {
                    computerShips[i] = new Ship(x, y, isVertical);
                    markBoard(computerShips[i], computerActualBoard);
                    placed = true;
                }
            }
        }
    }

    boolean isValidPlacement(int startX, int startY, boolean isVertical, char[][] board) {
        if (isVertical) {
            if (startX + SHIP_SIZE > GRID_SIZE) return false;
            for (int i = startX; i < startX + SHIP_SIZE; i++) {
                if (board[i][startY] == 'S') return false;
            }
        } else {
            if (startY + SHIP_SIZE > GRID_SIZE) return false;
            for (int i = startY; i < startY + SHIP_SIZE; i++) {
                if (board[startX][i] == 'S') return false;
            }
        }
        return true;
    }

    void markBoard(Ship ship, char[][] board) {
        for (int i = 0; i < SHIP_SIZE; i++) {
            if (ship.isVertical) {
                board[ship.startX + i][ship.startY] = 'S';
            } else {
                board[ship.startX][ship.startY + i] = 'S';
            }
        }
    }

    Ship getHitShip(int x, int y, Ship[] ships) {
        for (Ship ship : ships) {
            if (ship.occupies(x, y)) {
                ship.hits++;
                return ship;
            }
        }
        return null;
    }

    void displayBoard(char[][] board) {
        System.out.print("  ");
        for (int i = 0; i < GRID_SIZE; i++) {
            System.out.print(i + " ");
        }
        System.out.println();

        for (int i = 0; i < GRID_SIZE; i++) {
            System.out.print((char) ('A' + i) + " ");
            for (int j = 0; j < GRID_SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    void userTurn() {
        System.out.print("Enter your guess (e.g. A0): ");
        String guess = scanner.nextLine().toUpperCase();

        if (guess.length() < 2) {
            System.out.println("Invalid guess. Try again.");
            userTurn();
            return;
        }

        int x = guess.charAt(0) - 'A';
        int y;

        try {
            y = Integer.parseInt(guess.substring(1));
        } catch (NumberFormatException e) {
            System.out.println("Invalid guess. Try again.");
            userTurn();
            return;
        }

        if (x < 0 || x >= GRID_SIZE || y < 0 || y >= GRID_SIZE) {
            System.out.println("Invalid guess. Try again.");
            userTurn();
        } else {
            Ship hitShip = getHitShip(x, y, computerShips);
            if (hitShip != null) {
                System.out.println("Hit!");
                computerDisplayBoard[x][y] = 'X';
            } else {
                System.out.println("Miss.");
                computerDisplayBoard[x][y] = 'O';
            }
        }
    }

    void computerTurn() {
        int x = rand.nextInt(GRID_SIZE);
        int y = rand.nextInt(GRID_SIZE);
        Ship hitShip = getHitShip(x, y, humanShips);
        if (hitShip != null) {
            System.out.println("Computer hits at " + (char) ('A' + x) + y + "!");
            humanBoard[x][y] = 'X';
        } else {
            System.out.println("Computer misses at " + (char) ('A' + x) + y + ".");
            humanBoard[x][y] = 'O';
        }
    }

    boolean isGameOver(Ship[] ships) {
        for (Ship ship : ships) {
            if (!ship.isSunk()) {
                return false;
            }
        }
        return true;
    }

    void playGame() {
        deployUserShips();
        deployComputerShips();

        while (true) {
            System.out.println("\nYour board:");
            displayBoard(humanBoard);
            System.out.println("\nComputer's board:");
            displayBoard(computerDisplayBoard);

            userTurn();
            if (isGameOver(computerShips)) {
                System.out.println("Congratulations! You've sunk all of the computer's ships!");
                break;
            }

            computerTurn();
            if (isGameOver(humanShips)) {
                System.out.println("Sorry, the computer has sunk all of your ships.");
                break;
            }
        }
    }

    public static void main(String[] args) {
        BattleshipGame game = new BattleshipGame();
        game.playGame();
    }
}
