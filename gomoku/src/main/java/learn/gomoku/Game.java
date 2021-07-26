package learn.gomoku;

import learn.gomoku.game.*;
import learn.gomoku.players.*;
import java.util.Scanner;

public class Game {
    private Gomoku gomoku;
    private Board board;

    public void run() {
        do {
            setUp();
            takeTurns();
        } while (askPlayAgain());
    }

    //set up methods
    /**
     * creates gomoku and board objects
     */
    private void setUp() {
        System.out.println("\nWelcome to Gomoku");
        System.out.println("=".repeat(17));
        gomoku = new Gomoku(createPlayer(1), createPlayer(2));
        System.out.println("\n(Randomizing)");
        System.out.printf("\n%s goes first.\n", gomoku.getCurrent().getName());
        board = new Board();
    }

    /**
     * creats HumanPlayer or RandomPlayer depending on the input integer
     * @param num
     * @return HumanPlayer or RandomPlayer
     */
    private Player createPlayer(int num) {
        return choosePlayer(num) == 1 ? new HumanPlayer(inputName(num)) : new RandomPlayer();
    }

    /**
     * prints menu for choosing players and collects input for the choices
     * @param num
     * @return integer value 1 or 2
     */
    private int choosePlayer(int num) {
        int option;
        String choosePrompt = "\nPlayer %d is: \n1. Human \n2. Random Player \nSelect [1-2]:";
        boolean askAgain = true;
        do {
            option = readNonNegativeInt(String.format(choosePrompt, num));
            if (option == 0 || option > 2) {
                System.out.println("Invalid Option. Try Again.");
            } else {
                askAgain = false;
            }
        } while (askAgain);
        return option;
    }

    // gameplay methods
    /**
     * runs the game by letting players take turns until the game is over
     */
    private void takeTurns() {
        Stone stone;
        Result result = new Result("No stone placed"); // initialization needed here to compile print statement after loop
        while (!gomoku.isOver()) {
            System.out.printf("\n%s's Turn\n", gomoku.getCurrent().getName());
            stone = gomoku.getCurrent().generateMove(gomoku.getStones());
            result = (stone == null) ? humanPlayerTurn() : randomPlayerTurn(stone);
            board.show(gomoku.getStones());
        }
        // print outcome of game
        System.out.printf("\n%s\n", result.getMessage());
    }

    /**
     * collects input for Human Player's turn until a valid move is generated
     * places stone on board according to the generated move
     * @return result of stone placement
     */
    private Result humanPlayerTurn() {
        Stone stone;
        Result result;
        int[] coordinates;
        do {
            coordinates = inputCoordinates();
            stone = new Stone(coordinates[0], coordinates[1], gomoku.isBlacksTurn());
            result = gomoku.place(stone);
            // print msg for invalid moves
            if (result.getMessage() != null && !gomoku.isOver()) {
                System.out.printf("\n[Err]: %s\n" , result.getMessage());
            }
        } while (!result.isSuccess());
        return result;
    }

    /**
     * generates a valid random move and places stone on board according to the generated move
     * @param stone
     * @return result of stone placement
     */
    private Result randomPlayerTurn(Stone stone) {
        Result result;
        do {
            result = gomoku.place(stone);
            // if the above stone placement fails, print msg for invalid move
            // & create new stone
            if (!result.isSuccess()) {
                System.out.printf("\n[Err]: %s\n" , result.getMessage());
                stone = gomoku.getCurrent().generateMove(gomoku.getStones());
            }
        } while (!result.isSuccess());
        return result;
    }

    // helper methods for collecting input
    private String inputName(int num) {
        String namePrompt = "\nPlayer %d, enter your name: ";
        return readRequiredString(String.format(namePrompt, num));
    }
    private int[] inputCoordinates() {
        int[] coordinates = new int[2];
        coordinates[0] = readNonNegativeInt("Enter a row:") - 1;
        coordinates[1] = readNonNegativeInt("Enter a column:") - 1;
        return coordinates;
    }
    private boolean askPlayAgain() {
        char play;
        boolean askAgain = true;
        do {
            play = readRequiredString("\nPlay Again? [y/n]").trim().toLowerCase().charAt(0);
            if (play == 'y' || play == 'n') {
                askAgain = false;
            } else {
                System.out.println("Choose either Y or N.");
            }
        } while (askAgain);
        return play == 'y';
    }
    private int readNonNegativeInt(String prompt) {
        Scanner scanner = new Scanner(System.in);
        int input;
        boolean askAgain = true;
        do {
            System.out.print(prompt);
            try {
                input = Integer.parseInt(scanner.nextLine().trim());
                if (input < 0) {
                    System.out.println("Choose a positive number.");
                } else {
                    askAgain = false;
                }
            } catch (NumberFormatException e) {
                System.out.println("Input an integer.");
                input = 0; // to compile, input variable needs to be initialized
            }
        } while (askAgain);
        return input;
    }
    private String readRequiredString (String prompt) {
        Scanner scanner = new Scanner(System.in);
        String input;
        do {
            System.out.print(prompt);
            input = scanner.nextLine();
        } while (input.isBlank());
        return input;
    }
}
