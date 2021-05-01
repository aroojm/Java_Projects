package test;

import java.util.Random;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        menuLoop();
    }
    private static void menuLoop() {
        Scanner scanner = new Scanner(System.in);
        Random random = new Random();
        String command;
        String[] commands;
        boolean play = true;
        do {
            System.out.println("Input command:");
            command = scanner.nextLine();
            commands = command.split(" ");
            if (commands.length == 1 && commands[0].equals("exit")) {
                play = false;
                break;
            }
            if (commands.length != 3) {
                System.out.println("Bad parameters!");
            }
        } while (commands.length != 3);
        if (play) {
            gameLoop(scanner, random, commands);
        }
    }
    private static void gameLoop(Scanner scanner, Random random, String[] commands) {
        StringBuilder strB = new StringBuilder("_________");
        printGrid(strB);
        String user1 = commands[1];
        String user2 = commands[2];
        char usr1Sym = 'X';
        char usr2Sym = 'O';
        if (user1.equals("user") && user2.equals("user")) {
            do {
                playerPlay(scanner, strB, usr1Sym);
                if (checkStatus(strB)) {break;}
                playerPlay(scanner, strB, usr2Sym);
                if (checkStatus(strB)) {break;}
            } while (!checkStatus(strB));
        }
        if (user1.equals("user") && !user2.equals("user")) {
            do {
                playerPlay(scanner, strB, usr1Sym);
                if (checkStatus(strB)) {break;}
                comp(random, strB, usr2Sym, user2);
                if (checkStatus(strB)) {break;}
            } while (!checkStatus(strB));
        }
        if (!user1.equals("user") && user2.equals("user")) {
            do {
                comp(random, strB, usr1Sym, user1);
                if (checkStatus(strB)) {break;}
                playerPlay(scanner, strB, usr2Sym);
                if (checkStatus(strB)) {break;}
            } while (!checkStatus(strB));
        }
        if (!user1.equals("user") && !user2.equals("user")) {
            do {
                comp(random, strB, usr1Sym, user1);
                if (checkStatus(strB)) {break;}
                comp(random, strB, usr2Sym, user2);
                if (checkStatus(strB)) {break;}
            } while (!checkStatus(strB));
        }
    }
    private static void playerPlay(Scanner scanner, StringBuilder strB, char turnSymbol) {
        String coordinates;
        char a, b;
        int x, y, index;
        boolean turnTaken = false;
        do {
            System.out.print("Enter the coordinates: ");
            coordinates = scanner.nextLine();
            a = coordinates.charAt(0);
            b = coordinates.charAt(2);
            if (Character.isDigit(a) && Character.isDigit(b)) {
                x = Character.getNumericValue(a);
                y = Character.getNumericValue(b);
                index = coordToIndex(x, y);
                if ((x < 1 || x > 3) || (y < 1 || y > 3)) {
                    System.out.println("Coordinates should be from 1 to 3!");
                    continue;
                } else if (strB.charAt(index) == 'X' || strB.charAt(index) == 'O') {
                    System.out.println("This cell is occupied! Choose another one!");
                    continue;
                }
                strB.setCharAt(index, turnSymbol);
                turnTaken = true;
                printGrid(strB);
            } else {
                System.out.println("You should enter numbers!");
            }
        } while (!turnTaken);
    }
    private static void comp(Random random, StringBuilder strB, char turnSymbol, String level) {
        int index = -1;
        switch (level) {
            case "easy":
                System.out.println("Making move level \"easy\"");
                do {
                    index = random.nextInt(9);
                } while ((strB.charAt(index) == 'X' || strB.charAt(index) == 'O'));
                break;
            case "medium":
                System.out.println("Making move level \"medium\"");
                switch (turnSymbol) {
                    case 'X':
                        int temp1 = check(strB, 'X');
                        if (temp1 == -1) {
                            int temp2 = check(strB, 'O');
                            if (temp2 == -1) {
                                do {
                                    index = random.nextInt(9);
                                } while ((strB.charAt(index) == 'X' || strB.charAt(index) == 'O'));
                            } else {
                                index = temp2;
                            }
                        } else {
                            index = temp1;
                        }
                        break;
                    case 'O':
                        int temp3 = check(strB, 'O');
                        if (temp3 == -1) {
                            int temp4 = check(strB, 'X');
                            if (temp4 == -1) {
                                do {
                                    index = random.nextInt(9);
                                } while ((strB.charAt(index) == 'X' || strB.charAt(index) == 'O'));
                            } else {
                                index = temp4;
                            }
                        } else {
                            index = temp3;
                        }
                        break;
                }
                break;
//            case "hard":
//                System.out.println("Making move level \"hard\"");
//                Move bestMove = findBestMove(converter1(strB));
//                int[] arr = {bestMove.row, bestMove.col};
//                index = converter2(arr);
//                break;
        }
        strB.setCharAt(index, turnSymbol);
        printGrid(strB);
    }
    private static void printGrid(StringBuilder s) {
        int index = 0;
        System.out.println("---------");
        for (int i = 0; i < 3; i++) {
            System.out.print("| ");
            for (int j = 0; j < 3; j++) {
                System.out.print(s.charAt(index) + " ");
                index++;
            }
            System.out.println("|");
        }
        System.out.println("---------");
    }
    private static int coordToIndex(int x, int y) {
        int index = 0;
        switch (x) {
            case 1:
                switch (y) {
                    case 1:
                        index = 0;
                        break;
                    case 2:
                        index = 1;
                        break;
                    case 3:
                        index = 2;
                        break;
                }
                break;
            case 2:
                switch (y) {
                    case 1:
                        index = 3;
                        break;
                    case 2:
                        index = 4;
                        break;
                    case 3:
                        index = 5;
                        break;
                }
                break;
            case 3:
                switch (y) {
                    case 1:
                        index = 6;
                        break;
                    case 2:
                        index = 7;
                        break;
                    case 3:
                        index = 8;
                        break;
                }
                break;
        }
        return index;
    }
    private static int[] rowSum(StringBuilder s) {
        int[] arr = new int[3];
        int j = 0;
        for (int i = 0; i < s.length(); i+= 3) {
            arr[j] = ((int) s.charAt(i)) + ((int) s.charAt(i+1)) + ((int) s.charAt(i+2));
            j++;
        }
        return arr;
    }
    private static int[] colSum(StringBuilder s) {
        int[] arr = new int[3];
        int j = 0;
        for (int i = 0; i < 3; i++) {
            arr[j] = ((int) s.charAt(i)) + ((int) s.charAt(i+3)) + ((int) s.charAt(i+6));
            j++;
        }
        return arr;
    }
    private static int[] diagonalSum(StringBuilder s) {
        int[] arr = new int[2];
        // arr[0] main diagonal, arr[1] side diagonal
        arr[0] = ((int) s.charAt(0)) + ((int) s.charAt(4)) + ((int) s.charAt(8));
        arr[1] = ((int) s.charAt(2)) + ((int) s.charAt(4)) + ((int) s.charAt(6));
        return arr;
    }
    private static int[] countCells (StringBuilder s) {
        // 1st element: Num of Empty cells
        // 2nd: Num of X, 3rd: Num of O
        int [] arr = new int[3];
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '_') {
                arr[0]++;
            } else if (c == 'X') {
                arr[1]++;
            } else if (c == 'O') {
                arr[2]++;
            }
        }
        return arr;
    }
    private static boolean checkStatus(StringBuilder s) {
        boolean gameFinished = false;
        int[] rSum = rowSum(s);
        int[] cSum = colSum(s);
        int[] dSum = diagonalSum(s);
        int[] cellCount = countCells(s);

        boolean xRows = rSum[0] == 264 || rSum[1] == 264 || rSum[2] == 264;
        boolean oRows = rSum[0] == 237 || rSum[1] == 237 || rSum[2] == 237;
        boolean xCols = cSum[0] == 264 || cSum[1] == 264 || cSum[2] == 264;
        boolean oCols = cSum[0] == 237 || cSum[1] == 237 || cSum[2] == 237;
        boolean xDiag = dSum[0] == 264 || dSum[1] == 264;
        boolean oDiag = dSum[0] == 237 || dSum[1] == 237;

        if (cellCount[0] == 0) {
            if ((!xRows && !oRows && !xCols && !oCols && !xDiag && !oDiag)) {
                System.out.println("Draw");
                gameFinished = true;
            }
        } else if (!(xRows || oRows || xCols || oCols || xDiag || oDiag)) {
            //  System.out.println("Making move level \"easy\"");
            gameFinished = false;
        }
        if (xRows || xCols) {
            if (!(oRows || oCols)) {
                System.out.println("X wins");
                gameFinished = true;
            }
        }
        if (oRows || oCols) {
            if (!(xRows || xCols)) {
                System.out.println("O wins");
                gameFinished = true;
            }
        }
        if (xDiag) {
            System.out.println("X wins");
            gameFinished = true;
        }
        if (oDiag) {
            System.out.println("O wins");
            gameFinished = true;
        }
        return gameFinished;
    }
    private static int check(StringBuilder s, char c) {
        // check for self to win game or for opponent to stop from winning
        // return the index for where to play to otherwise -1
        int[] rSum = rowSum(s);
        int[] cSum = colSum(s);
        int[] dSum = diagonalSum(s);
        int index = -1;
        switch (c) {
            case 'X':
                if (rSum[0] == 271) {
                    return s.indexOf("_");
                } else if (rSum[1] == 271) {
                    return s.indexOf("_", 3);
                } else if (rSum[2] == 271) {
                    return s.lastIndexOf("_");
                }
                if (cSum[0] == 271) {
                    if (s.charAt(0) == '_') {
                        return 0;
                    } else if (s.charAt(3) == '_') {
                        return 3;
                    } else if (s.charAt(6) == '_') {
                        return 6;
                    }
                } else if (cSum[1] == 271) {
                    if (s.charAt(1) == '_') {
                        return 1;
                    } else if (s.charAt(4) == '_') {
                        return 4;
                    } else if (s.charAt(7) == '_') {
                        return 7;
                    }
                } else if (cSum[2] == 271) {
                    if (s.charAt(2) == '_') {
                        return 2;
                    } else if (s.charAt(5) == '_') {
                        return 5;
                    } else if (s.charAt(8) == '_') {
                        return 8;
                    }
                }
                if (dSum[0] == 271) {
                    if (s.charAt(0) == '_') {
                        return 0;
                    } else if (s.charAt(4) == '_') {
                        return 4;
                    } else if (s.charAt(8) == '_') {
                        return 8;
                    }
                } else if (dSum[1] == 271) {
                    if (s.charAt(2) == '_') {
                        return 2;
                    } else if (s.charAt(4) == '_') {
                        return 4;
                    } else if (s.charAt(6) == '_') {
                        return 6;
                    }
                }
                break;
            case 'O':
                if (rSum[0] == 253) {
                    return s.indexOf("_");
                } else if (rSum[1] == 253) {
                    return s.indexOf("_", 3);
                } else if (rSum[2] == 253) {
                    return s.lastIndexOf("_");
                }
                if (cSum[0] == 253) {
                    if (s.charAt(0) == '_') {
                        return 0;
                    } else if (s.charAt(3) == '_') {
                        return 3;
                    } else if (s.charAt(6) == '_') {
                        return 6;
                    }
                } else if (cSum[1] == 253) {
                    if (s.charAt(1) == '_') {
                        return 1;
                    } else if (s.charAt(4) == '_') {
                        return 4;
                    } else if (s.charAt(7) == '_') {
                        return 7;
                    }
                } else if (cSum[2] == 253) {
                    if (s.charAt(2) == '_') {
                        return 2;
                    } else if (s.charAt(5) == '_') {
                        return 5;
                    } else if (s.charAt(8) == '_') {
                        return 8;
                    }
                }
                if (dSum[0] == 253) {
                    if (s.charAt(0) == '_') {
                        return 0;
                    } else if (s.charAt(4) == '_') {
                        return 4;
                    } else if (s.charAt(8) == '_') {
                        return 8;
                    }
                } else if (dSum[1] == 253) {
                    if (s.charAt(2) == '_') {
                        return 2;
                    } else if (s.charAt(4) == '_') {
                        return 4;
                    } else if (s.charAt(6) == '_') {
                        return 6;
                    }
                }
                break;
        }
        return index;
    }

//    public static char[][] converter1(StringBuilder strB) {
//        char[][] board = new char[3][3];
//        int count = 0;
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                board[i][j] = strB.charAt(count);
//                count++;
//            }
//        }
//        return board;
//    }
//    public static int converter2(int[] array){
//        int index = 0;
//        if (array[0] == 0) {
//            index = array[1];
//        } else if (array[0] == 1) {
//            index = array[1] + 3;
//        } else if (array[0] == 2) {
//            index = array[1] + 6;
//        }
//        return index;
//    }

}



