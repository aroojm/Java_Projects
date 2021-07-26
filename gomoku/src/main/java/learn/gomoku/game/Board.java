package learn.gomoku.game;

import java.util.List;

public class Board {
    private final char[][] board = new char[Gomoku.WIDTH][Gomoku.WIDTH];

    public void show(List<Stone> stones) {
        setBoard(stones);
        printBoard();
    }

    /**
     * prints 2D array representing board
     */
    private void printBoard() {
        int len = board.length;
        System.out.println(makeHeader(len));
        for (int i = 0; i < len; i++) {
            System.out.printf("%02d ", i + 1);
            for (int j = 0; j < len; j++) {
                System.out.printf( " %c ",(board[i][j] == 0) ?'_' : board[i][j]);
            }
            System.out.print("\n");
        }
    }

    /**
     * maps list of Stone to 2D char array representing board
     * @param stones
     */
    private void setBoard(List<Stone> stones) {
        for (Stone stone : stones) {
            board[stone.getRow()][stone.getColumn()] = stone.isBlack() ? 'X' : 'O';
        }
    }

    private String makeHeader(int num) {
        StringBuilder stringBuilder = new StringBuilder("\n   ");
        for (int i = 1; i <= num; i++) {
            stringBuilder.append(String.format("%02d ", i));
        }
        return stringBuilder.toString();

    }
}
