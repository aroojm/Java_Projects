package tictactoe;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        gameLoop();
    }
    private static void gameLoop() {
        Scanner scanner = new Scanner(System.in);
        StringBuilder strB = new StringBuilder("_________");
        printGrid(strB.toString());
        boolean check = true; // game finished or not
        boolean turn = true; // true for X, false for O
        int x = 0;
        int y = 0;
        int index = 0;
        do {
            System.out.print("Enter the coordinates: ");
            String coordinates = scanner.nextLine();
            char a = coordinates.charAt(0);
            char b = coordinates.charAt(2);
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
                if (turn) {
                    strB.setCharAt(index, 'X');
                    turn = false;
                } else {
                    strB.setCharAt(index, 'O');
                    turn = true;
                }
                printGrid(strB.toString());
                if (checkStatus(strB.toString())) {
                    check = false;
                }
            } else {
                System.out.println("You should enter numbers!");
                check = true;
            }
        } while (check);
    }
    private static void printGrid(String s) {
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
    private static int[] rowSum(String s) {
        int[] arr = new int[3];
        int j = 0;
        for (int i = 0; i < s.length(); i+= 3) {
            arr[j] = ((int) s.charAt(i)) + ((int) s.charAt(i+1)) + ((int) s.charAt(i+2));
            j++;
        }
        return arr;
    }
    private static int[] colSum(String s) {
        int[] arr = new int[3];
        int j = 0;
        for (int i = 0; i < 3; i++) {
            arr[j] = ((int) s.charAt(i)) + ((int) s.charAt(i+3)) + ((int) s.charAt(i+6));
            j++;
        }
        return arr;
    }
    private static int[] diagonalSum(String s) {
        int[] arr = new int[2];
        // arr[0] main diagonal, arr[1] side diagonal
        arr[0] = ((int) s.charAt(0)) + ((int) s.charAt(4)) + ((int) s.charAt(8));
        arr[1] = ((int) s.charAt(2)) + ((int) s.charAt(4)) + ((int) s.charAt(6));
        return arr;
    }
    private static int[] countCells (String s) {
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
    private static boolean checkStatus(String s) {
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
}
