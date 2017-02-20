/*

    CLASS       : BaoGame
    AUTHOR      : Regan Koopmans
    DESCRIPTION : Encapsulates the procession of a typical Bao game,
                  including turns and winning conditions, and allows for
                  games of different Human/Computer players.

 */

public class BaoGame {

BaoBoard board = new BaoBoard();

private Boolean hasWon(Player player) {
        int offset;
        if (player == Player.PLAYER_1) {
                offset = 2;
        } else {
                offset = 1;
        }

        int [] array = board.getBoard()[offset];
        for (int item:array) {
                if (item != 0) { return false; }
        }
        return true;
}

public void start(Boolean isHumanPlayer1, Boolean isHumanPlayer2) {

        BaoPlayer player1 = new HumanPlayer(board, Player.PLAYER_1);
        BaoPlayer player2 = new AIPlayer(board, Player.PLAYER_2, 20);

        while (!hasWon(Player.PLAYER_1) && !hasWon(Player.PLAYER_2)) {

                System.out.println("\n\tPlayer1\n");
                player1.nextTurn();
                board.printBoard();
                System.out.println("\n\tPlayer2\n");
                player2.nextTurn();
                board.printBoard();

        }
}
}