public class BaoBoard {

private int[][] board = new int[4][8];

public BaoBoard() {
        board[1][1] = 2;
        board[1][2] = 2;
        board[1][3] = 6;

        board[2][4] = 6;
        board[2][5] = 2;
        board[2][6] = 2;
}

public int[][] getBoard() {
        return board;
}

public int placeSeed(Player player, int position) {
        int captured = 0;
        if (player == Player.PLAYER_1) {
                board[2][position] += 1;
                captured = board[1][position];
                board[1][position] = 0;
        }
        else {
                board[1][7-position] += 1;
                captured = board[2][7-position];
                board[2][position] = 0;
        }
        return captured;
}

public void sow(Player player, int numSeeds, int startPosition, Direction direction) {
        int netDirection = (direction.ordinal() == 0) ? -1 : 1;

        // Player 2 has a reversed perspective.

        if (player == Player.PLAYER_2) {
                netDirection = netDirection* -1;
        }


}

public void printBoard() {
        for (int [] array:board) {
                for (int x:array) {
                        System.out.print("[" + x + "]");
                }
                System.out.println("");
        }
}
}
