/*

    CLASS       : BaoGame
    AUTHOR      : Regan Koopmans
    DESCRIPTION : Encapsulates the procession of a typical Bao game,
                  including turns and winning conditions, and allows for
                  games of different Human/Computer players.

 */
package bao;

import java.util.ArrayList;
import java.util.Arrays;

public class BaoGame {

    public BaoBoard board = new BaoBoard();
    BaoPlayer player1;
    BaoPlayer player2;

    private Boolean hasWon(Player player) {
        int offset;
        if (player == Player.PLAYER_1) {
            offset = 1;
        }
        else {
            offset = 2;
        }
        int [] array = board.getBoard()[offset];
        for (int item:array) {
            if (item != 0) {
                return false;
            }
        }
        return true;
    }

    public void start(Boolean isHumanPlayer1, Boolean isHumanPlayer2) {
        player1 = new HumanPlayer(board, Player.PLAYER_1);
        player2 = new AIPlayer(board, Player.PLAYER_2, 20);

        while (true) {
            System.out.println("\n\tPlayer 1\n");
            player1.turnDone = false;
            player1.nextTurn();
            if (hasWon(Player.PLAYER_1)) {
                notifyWinner("Player 1");
            }
            board.printBoard();
            player1.turnDone = true;
            System.out.println("\n\tPlayer 2\n");
            player2.turnDone = false;
            player2.nextTurn();
            if (hasWon(Player.PLAYER_1)) {
                notifyWinner("Player 2");
            }
            board.printBoard();
            player2.turnDone = true;
        }
    }

    public void notifyWinner(String winnerName) {
        System.out.println(winnerName + " has won!");
        player1.turnDone = true;
        player2.turnDone = true;
        System.exit(0);
    }

    public ArrayList<BaoPlayer> returnPlayers() {
        return new ArrayList<BaoPlayer>(Arrays.asList(player1,player2));
    }
}
