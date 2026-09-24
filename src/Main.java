import java.util.Random;
import java.util.Scanner;

class Player implements GameBoard {
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";
    private static final String ANSI_BLACK = "\u001B[30m";

    private final String SPIN = "SPN";
    private int spincount =1;

    private int player1TrapSpawnCount = 3;
    private int player2TrapSpawnCount = 3;


    private int player1LongJumpCount = 6;
    private int player2LongJumpCount = 6;
    private int player1DestroyCount = 3;
    private int player2DestroyCount = 3;

    private String[][] board;

    private int mouseTrapCount = 15;
    private int bombCount = 10;
    private int tntCount = 5;


    private final String MOUSE_TRAP = "MST";
    private final String BOMB = "BMB";

    private final String UNBREAKABLE_WALL="UWL";

    private final String BREAKABLE_WALL="BWL";
    private final String TNT = "TNT";

    private int player1Lives = 5;
    private int player2Lives = 5;
    private final int DAMAGE = 1;
    private final int POINTS_LOST = 5;

    private final int SIZE = 10;
    private final String PLAYER1 = "PL1";
    private final String PLAYER2 = "PL2";
    private final String TREASURE = "TRS";
    public int TREASURE_POINTS = 10;
    private int[] player1Position = new int[2];
    private int[] player2Position = new int[2];
    private int player1Score = 0;
    private int player2Score = 0;

    public Player() {
        board = new String[SIZE][SIZE];
        initializeBoard();
        placePlayers();
        placeTreasure();
    }

    @Override
    public void initializeBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = " ";
            }
        }
        placeTraps();
        placeWalls();
        placeSpin();
    }

    private void placeSpin() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(SIZE);
            y = rand.nextInt(SIZE);
        } while (!board[x][y].equals(" ")||(x==0 && y==0)||(x==SIZE-1 && y==SIZE-1));
        board[x][y] = SPIN;
    }



    @Override
    public void placePlayers() {
        board[0][0] = PLAYER1;
        player1Position[0] = 0;
        player1Position[1] = 0;
        board[SIZE - 1][SIZE - 1] = PLAYER2;
        player2Position[0] = SIZE - 1;
        player2Position[1] = SIZE - 1;
    }

    @Override
    public void displayBoard() {
        int cellWidth = 3;

        for (int i = 0; i < SIZE; i++) {
            System.out.print("| ");
            for (int j = 0; j < SIZE; j++) {
                String cell = board[i][j];
                switch (cell) {
                    case TNT:
                        System.out.print(ANSI_RED + cell + ANSI_RESET + " ");
                        break;
                    case TREASURE:
                        System.out.print(ANSI_GREEN + cell + ANSI_RESET + " ");
                        break;
                    case "BMB":
                    case "MST":
                        System.out.print(ANSI_RED + cell + ANSI_RESET + " ");
                        break;
                    case "UWL":
                    case "BWL":
                        System.out.print(ANSI_YELLOW + cell + ANSI_RESET + " ");
                        break;
                    case PLAYER1:
                    case PLAYER2:
                        System.out.print(ANSI_WHITE_BACKGROUND + ANSI_BLACK + cell + ANSI_RESET + " ");
                        break;
                    case SPIN:
                        System.out.print(ANSI_BLUE + cell + ANSI_RESET + " ");
                        break;
                    default:
                        System.out.print(cell + " ");
                }
                if (cell.length() < cellWidth) {
                    System.out.print(" ".repeat(cellWidth - cell.length()));
                }
                System.out.print("| ");
            }
            System.out.println();
            if (i < SIZE - 1) {
                System.out.println("-".repeat(SIZE * (cellWidth + 3)));
            }
        }
        System.out.println("PL1 Score: " + player1Score + " | PL2 Score: " + player2Score);
        System.out.println("PL1 HP: " + player1Lives + " | PL2 HP: " + player2Lives);
        System.out.println("PL1 Abilities -> Destruction: " + player1DestroyCount + " | Long Jump: " + player1LongJumpCount + "| Spawn Trap: " + player1TrapSpawnCount);
        System.out.println("PL2 Abilities -> Destruction: " + player2DestroyCount + " | Long Jump: " + player2LongJumpCount + "| Spawn Trap: " + player2TrapSpawnCount);
    }

    @Override
    public void startGame() {
        Scanner scanner = new Scanner(System.in);
        Player currentPlayer = this;

        displayBoard();

        while (true) {
            System.out.println("--------------------------------" + (currentPlayer == this ? PLAYER1 : PLAYER2)+"'s Turn -------------------------------");
            System.out.print("Choose which way you want to move:(R = Right، L = Left، U = Up، D = Down): ");
            String direction = scanner.nextLine();

            movePlayer(currentPlayer, direction);
            displayBoard();

            if (currentPlayer == this && player1Score >= 100) {
                System.out.println("Pl1 has win!");
                break;
            } else if (currentPlayer != this && player2Score >= 100) {
                System.out.println("Pl2 has won!");
                break;
            }

            if(player1Lives<=0){
                System.out.println("PL2 has won! PL1 has no lives left.");
                break;
            }else if(player2Lives<=0){
                System.out.println("PL1 has won! PL2 has no lives left.");
                break;
            }

            currentPlayer = (currentPlayer == this) ? new Opponent() : this;
        }
    }

    private void movePlayer(Player player, String direction) {

        boolean isValidInput = false;
        Scanner scanner = new Scanner(System.in);
        while(!isValidInput){
        int x = 0, y = 0;
        int[] currentPosition;
        String playerSymbol;
        int playerDestroyCount;
        int playerTrapSpawnCount;
        int playerLongJumpCount;
        int playerLives;
        int playerScore;

        if (player == this) {
            currentPosition = player1Position;
            playerSymbol = PLAYER1;
            playerTrapSpawnCount = player1TrapSpawnCount;
            playerDestroyCount = player1DestroyCount;
            playerLongJumpCount=player1LongJumpCount;
            playerLives = player1Lives;
            playerScore = player1Score;
        } else {
            currentPosition = player2Position;
            playerSymbol = PLAYER2;
            playerTrapSpawnCount = player2TrapSpawnCount;
            playerDestroyCount = player2DestroyCount;
            playerLongJumpCount=player2LongJumpCount;
            playerLives = player2Lives;
            playerScore = player2Score;
        }
        switch (direction.toUpperCase()) {
            case "R":
                y = 1;
                break;
            case "L":
                y = -1;
                break;
            case "U":
                x = -1;
                break;
            case "D":
                x = 1;
                break;
            default:
                System.out.println("Invalid choice to move! Please try again.");
                return;
        }

        System.out.println("choose your ability:(s = Spawn Trap , l = Long Jump , d = Destruction , n = none)");
        String abilityChoice = scanner.nextLine();

        int newX = currentPosition[0] ;
        int newY = currentPosition[1];

        switch (abilityChoice){
            case "s":
                if (playerTrapSpawnCount > 0) {
                    SpawnTrap(player, direction);
                    playerTrapSpawnCount--;
                    if (player == this) {
                        player1TrapSpawnCount = playerTrapSpawnCount;
                    } else {
                        player2TrapSpawnCount = playerTrapSpawnCount;
                    }
                    return;
                } else {
                    System.out.println("You have no spawn trap abilities left.");
                    System.out.println("Choose which way you want to move:(R = Right، L = Left، U = Up، D = Down): ");
                    direction=scanner.nextLine();
                }
                break;

            case "l":

                int multiplier = 1;
                if(playerLongJumpCount>0){
                    multiplier=2;
                    playerLongJumpCount--;
                }else{
                    System.out.println("you have no Long Jump ability left!");
                    System.out.println("Choose which way you want to move:(R = Right، L = Left، U = Up، D = Down): ");
                    direction=scanner.nextLine();
                    continue;
                }

                if ((x == -1 && currentPosition[0] - multiplier < 0) ||
                        (x == 1 && currentPosition[0] + multiplier >= SIZE) ||
                        (y == -1 && currentPosition[1] - multiplier < 0) ||
                        (y == 1 && currentPosition[1] + multiplier >= SIZE)) {
                    System.out.println("You can't go out of the board.");
                    System.out.println("Choose which way you want to move:(R = Right، L = Left، U = Up، D = Down): ");
                    direction=scanner.nextLine();
                    continue;
                }

                newX = currentPosition[0] + x * multiplier;
                newY = currentPosition[1] + y * multiplier;

                String cell = board[newX][newY];
                if (cell.equals(BREAKABLE_WALL)||cell.equals(UNBREAKABLE_WALL)) {
                     System.out.println("You encountered a wall! you can't do the long jump.");
                     System.out.println("Choose which way you want to move:(R = Right، L = Left، U = Up، D = Down): ");
                        direction=scanner.nextLine();
                        continue;

                } else if (cell.equals(BOMB) || cell.equals(MOUSE_TRAP)||cell.equals(TNT)||cell.equals(TREASURE)) {
                    System.out.println("You encountered an obstacle: " + cell);
                        switch (cell) {
                            case BOMB:
                                playerLives -= 2;
                                playerScore -= 10;
                                break;

                            case MOUSE_TRAP:
                                playerLives -= DAMAGE;
                                playerScore -= POINTS_LOST;
                                break;
                            case TREASURE:
                                playerScore+=10;
                                break;
                            case TNT:
                                playerLives-=3;
                                playerScore-=15;
                        }
                    }
                System.out.println("PL"+(player==this ? "1":"2")+" choose to do Long Jump.");
                break;
            case "d":
                newX = currentPosition[0] + x;
                newY = currentPosition[1] + y;
                if ((x == -1 && newX < 0) ||(x == 1 && newX >= SIZE) ||(y == -1 && newY < 0) ||(y == 1 && newY >= SIZE)) {
                System.out.println("You can't destroy outside the board.");
                System.out.println("Choose which way you want to move:(R = Right، L = Left، U = Up، D = Down): ");
                direction=scanner.nextLine();
                continue;
            }
            String targetCell = board[newX][newY];
            if (targetCell.equals(BREAKABLE_WALL) ||targetCell.equals(BOMB) ||targetCell.equals(MOUSE_TRAP)) {
                if (playerDestroyCount > 0) {
                    destroy(newX, newY, playerSymbol);
                    if (player == this) {
                        if (player1DestroyCount > 0) {
                            player1DestroyCount--;
                        }
                    } else {
                        if (player2DestroyCount > 0) {
                            player2DestroyCount--;
                        }
                    }
                } else {
                    System.out.println("You have no destroy abilities left.");
                    System.out.println("Choose which way you want to move:(R = Right، L = Left، U = Up، D = Down): ");
                    direction=scanner.nextLine();
                    continue;
                }
            } else {
                System.out.println("There is nothing to destroy at the specified location.");
                System.out.println("Choose which way you want to move:(R = Right، L = Left، U = Up، D = Down): ");
                direction=scanner.nextLine();
                continue;
            }
            break;

            case "n":
                newX = currentPosition[0] + x;
                newY = currentPosition[1] + y;

                if ((x == -1 && newX < 0) ||(x == 1 && newX >= SIZE) ||(y == -1 && newY < 0) || (y == 1 && newY >= SIZE)) {
                    System.out.println("You can't move outside the board.");
                    System.out.println("Choose which way you want to move:(R = Right، L = Left، U = Up، D = Down): ");
                    direction=scanner.nextLine();
                    continue;
                }

                if (board[newX][newY].equals(UNBREAKABLE_WALL)) {
                    System.out.println("You encountered an unbreakable wall in the path! You cannot pass.");
                    System.out.println("Choose which way you want to move:(R = Right، L = Left، U = Up، D = Down): ");
                    direction=scanner.nextLine();
                    continue;
                }
                if (board[newX][newY].equals(BREAKABLE_WALL)) {
                    System.out.println("You encountered a breakable wall! You cannot pass.");
                    System.out.println("Choose which way you want to move:(R = Right، L = Left، U = Up، D = Down): ");
                    direction=scanner.nextLine();
                    continue;
                }

                cell = board[newX][newY];
                if (cell.equals(BOMB)|| cell.equals(MOUSE_TRAP)|| cell.equals(TNT)||cell.equals(TREASURE)) {
                    System.out.println("You encountered an obstacle: " + cell);
                    switch (cell) {
                        case BOMB:
                            playerLives -= 2;
                            playerScore -= 10;
                            break;
                        case MOUSE_TRAP:
                            playerLives -= DAMAGE;
                            playerScore -= POINTS_LOST;
                            break;
                        case TNT:
                            playerLives -= 3;
                            playerScore -= 15;
                            break;
                        case TREASURE:
                            playerScore+=10;
                            break;

                    }
                }
                if (player == this) {
                    player1Lives = playerLives;
                    player1Score = playerScore;
                } else {
                    player2Lives = playerLives;
                    player2Score = playerScore;
                }

                System.out.println("PL"+(player==this ? "1":"2")+" chose to move "+direction.toUpperCase()+".");
                handleMovement(player, currentPosition, newX, newY, playerSymbol, playerDestroyCount);
                break;
        }

        if (player == this) {
            player1Lives = playerLives;
            player1Score = playerScore;
            player1LongJumpCount=playerLongJumpCount;
        } else {
            player2Lives = playerLives;
            player2Score = playerScore;
            player2LongJumpCount=playerLongJumpCount;
        }

        handleMovement(player, currentPosition, newX, newY, playerSymbol, playerDestroyCount);
        isValidInput=true;
    }}
    private void destroy(int x, int y, String playerSymbol) {
        String cell = board[x][y];
        if (cell.equals(BOMB) ||cell.equals(MOUSE_TRAP)|| cell.equals(BREAKABLE_WALL)) {
            board[x][y] = " ";
            System.out.println(playerSymbol + " destroyed " + cell + " at (" + x + ", " + y + ").");
        } else {
            System.out.println("Nothing to destroy at (" + x + ", " + y + ").");
        }
    }


    private void SpawnTrap(Player player, String direction) {
        boolean ValidInput=false;
        Scanner scanner=new Scanner(System.in);
        while(!ValidInput){
        int x = 0, y = 0;
        int[] currentPosition;

        String playerSymbol;
        if (player == this) {
            currentPosition = player1Position;
            playerSymbol = PLAYER1;
        } else {
            currentPosition = player2Position;
            playerSymbol = PLAYER2;
        }

        switch (direction.toUpperCase()) {
            case "R":
                y = 1;
                break;
            case "L":
                y = -1;
                break;
            case "U":
                x = -1;
                break;
            case "D":
                x = 1;
                break;
            default:
                System.out.println("Invalid direction for trap! Please try again.");
                return;
        }

        int newX = currentPosition[0] + x;
        int newY = currentPosition[1] + y;

        int distance = Math.abs(player1Position[0]-player2Position[0])+Math.abs(player1Position[1]-player2Position[1]);

        if(distance==1){
            placeTrapforanotherplayer(player,newX,newY);
            return;

        }

        if (newX < 0 ||newX >= SIZE|| newY < 0 || newY >= SIZE) {
            System.out.println("Invalid position for trap! Out of bounds.");
            System.out.println("Choose which way you want to spawn your trap:(R = Right، L = Left، U = Up، D = Down): ");
            direction=scanner.nextLine();
            continue;
        }

        if((player==this && newX==0 && newY==0)||(player !=this && newX==SIZE-1 && newY==SIZE-1)){
            direction=scanner.nextLine();
            continue;
        }

        if (!board[newX][newY].equals(" ")) {
            System.out.println("Position is occupied. Cannot place trap.");
            System.out.println("Choose which way you want to spawn your trap:(R = Right، L = Left، U = Up، D = Down): ");
            direction=scanner.nextLine();
            continue;
        }

        Random rand = new Random();
        String[] traps = {MOUSE_TRAP, BOMB, TNT};
        String trap = traps[rand.nextInt(traps.length)];

        board[newX][newY] = trap;
        System.out.println("Trap (" + trap + ") placed at (" + newX + ", " + newY + ").");

        ValidInput=true;

    }}

    private void handleMovement(Player player, int[] currentPosition, int newX, int newY, String playerSymbol, int playerDestroyCount) {
         if (board[newX][newY].equals(TREASURE)) {
            System.out.println("You've found the treasure!");
            placeTreasure();
        } else if (board[newX][newY].equals(SPIN)) {
            System.out.println("You've landed on SPIN!");
            executeSpinEvent(player);
            placeSpin();
        }

        if((player == this && newX ==0 && newY==0)||(player !=this && newX==SIZE-1 && newY==SIZE-1)){
            return;
        }

        if(board[newX][newY].equals(PLAYER1)|| board[newX][newY].equals(PLAYER2)){

            return;
        }

        board[currentPosition[0]][currentPosition[1]] = " ";
        currentPosition[0] = newX;
        currentPosition[1] = newY;
        board[newX][newY] = playerSymbol;
    }

    private void applyTrapEffect(String trap, int[] position, boolean isOpponent) {
        int damage = 0;
        int scorePenalty = 0;

        switch (trap) {
            case BOMB:
                damage = 2;
                scorePenalty = 10;
                break;
            case MOUSE_TRAP:
                damage = 1;
                scorePenalty = 5;
                break;
            case TNT:
                damage = 3;
                scorePenalty = 15;
                break;
        }

        if (isOpponent) {
            player2Lives -= damage;
            player2Score -= scorePenalty;
            board[player2Position[0]][player2Position[1]] = " ";
            player2Position[0] = SIZE - 1;
            player2Position[1] = SIZE - 1;
            board[SIZE - 1][SIZE - 1] = PLAYER2;
        } else {
            player1Lives -= damage;
            player1Score -= scorePenalty;
            board[player1Position[0]][player1Position[1]] = " ";
            player1Position[0] = 0;
            player1Position[1] = 0;
            board[0][0] = PLAYER1;
        }
    }
    private void placeTrapforanotherplayer(Player player, int newX, int newY) {
        String playerSymbol = (player == this) ? PLAYER1 : PLAYER2;
        boolean isOpponent = (player == this);

        Random rand = new Random();
        String[] traps = {MOUSE_TRAP, BOMB, TNT};
        String trap = traps[rand.nextInt(traps.length)];

        board[newX][newY] = trap;
        System.out.println(playerSymbol + " planted a trap (" + trap + ") at (" + newX + ", " + newY + ").");

        applyTrapEffect(trap, isOpponent ? player1Position : player2Position, isOpponent);
    }

    private void executeSpinEvent(Player player) {
        Random rand = new Random();
        int event = rand.nextInt(4);

        switch (event) {
            case 0:
                System.out.println("You gained an ability!");
                int ability = rand.nextInt(3);
                if (player == this) {
                    if (ability == 0) player1TrapSpawnCount++;
                    else if (ability == 1) player1DestroyCount++;
                    else player1LongJumpCount++;
                } else {
                    if (ability == 0) player2TrapSpawnCount++;
                    else if (ability == 1) player2DestroyCount++;
                    else player2LongJumpCount++;
                }
                break;
            case 1:
                System.out.println("Your opponent returned to their start point!");
                if (player == this) {
                    board[player2Position[0]][player2Position[1]] = " ";
                    player2Position[0] = SIZE - 1;
                    player2Position[1] = SIZE - 1;
                    board[SIZE - 1][SIZE - 1] = PLAYER2;
                } else {
                    board[player1Position[0]][player1Position[1]] = " ";
                    player1Position[0] = 0;
                    player1Position[1] = 0;
                    board[0][0] = PLAYER1;
                }
                break;
            case 2:
                System.out.println("3 random TNT traps placed!");
                for (int i = 0; i < 3; i++) {
                    placeTrap(TNT);
                }
                break;
            case 3:
                System.out.println("3 random traps destroyed!");
                for (int i = 0; i < 3; i++) {
                    destroyRandomTrap();
                }
                break;
        }
    }

    private void placeTrap(String trapType) {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(SIZE);
            y = rand.nextInt(SIZE);
        } while (!board[x][y].equals(" "));
        board[x][y] = trapType;
    }

    private void destroyRandomTrap() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(SIZE);
            y = rand.nextInt(SIZE);
        } while (!(board[x][y].equals(MOUSE_TRAP)|| board[x][y].equals(BOMB) ||board[x][y].equals(TNT)));
        board[x][y] = " ";
    }


    private void placeTreasure() {
        Random rand = new Random();
        int x, y;
        do {
            x = rand.nextInt(SIZE);
            y = rand.nextInt(SIZE);
        } while (!board[x][y].equals(" ")||(x==0 && y==0)||(x==SIZE-1 && y==SIZE-1));

        board[x][y] = TREASURE;
    }

    private void placeTraps() {
        Random rand = new Random();

        while (mouseTrapCount > 0|| bombCount > 0 ||tntCount > 0) {
            int x = rand.nextInt(SIZE);
            int y = rand.nextInt(SIZE);

            if (board[x][y].equals(" ")) {
                String trap;
                if (mouseTrapCount > 0) {
                    trap = MOUSE_TRAP;
                    mouseTrapCount--;
                } else if (bombCount > 0) {
                    trap = BOMB;
                    bombCount--;
                } else {
                    trap = TNT;
                    tntCount--;
                }
                board[x][y] = trap;
            }
        }
    }

    private void placeWalls() {
        Random rand = new Random();
        int breakableWallCount = 15;
        int unbreakableWallCount = 5;

        while (breakableWallCount > 0 || unbreakableWallCount > 0) {
            int x = rand.nextInt(SIZE);
            int y = rand.nextInt(SIZE);

            if (board[x][y].equals(" ")) {
                String wall;
                if (breakableWallCount > 0) {
                    wall = "BWL";
                    breakableWallCount--;
                } else {
                    wall = "UWL";
                    unbreakableWallCount--;
                }
                board[x][y] = wall;
            }
        }
    }
}

class Opponent extends Player {
}

public class Main {
    public static void main(String[] args) {
        Player player = new Player();
        player.startGame();
    }
}
