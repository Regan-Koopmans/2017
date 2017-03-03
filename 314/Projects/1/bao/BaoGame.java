/*

    CLASS       : BaoGame
    AUTHOR      : Regan Koopmans
    DESCRIPTION : Encapsulates the procession of a typical Bao game,
                  including turns and winning conditions, and allows for
                  games of different Human/Computer players.

 */

package bao;

import java.util.ArrayList;

public class BaoGame {

public BaoBoard board = new BaoBoard();

    private Boolean hasWon(Player player) {
        int offset;
        if (player == Player.PLAYER_1) {
                offset = 1;
        } else {
                offset = 2;
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

        while (true) {

                System.out.println("\n\tPlayer 1\n");
                player1.nextTurn();
                if (hasWon(Player.PLAYER_1)) {
                  notifyWinner("Player 1");
                }
                board.printBoard();
                System.out.println("\n\tPlayer 2\n");
                player2.nextTurn();
                if (hasWon(Player.PLAYER_1)) {
                  notifyWinner("Player 2");
                }
                board.printBoard();

        }
    }

    public void notifyWinner(String winnerName) {
      System.out.println(winnerName + " has won!");
      System.exit(0);
    }
}
