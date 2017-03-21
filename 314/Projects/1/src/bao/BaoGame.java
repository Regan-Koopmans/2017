/**
*
*    CLASS       : BaoGame
*    @author Regan Koopmans
*    
*    Encapsulates the procession of a typical Bao game,
*    including turns and winning conditions, and allows for
*    games of different Human/Computer players.
*
*/

package bao;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Observer;
import java.util.Observable;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.*;

import bao.player.BaoPlayer;
import bao.player.PlayerType;
import bao.player.HumanPlayer;
import bao.player.AIPlayer;

public class BaoGame extends Observable {

    public BaoBoard board = new BaoBoard();
    BaoPlayer player1;
    BaoPlayer player2;
    private boolean running = true;

    private Boolean hasWon(PlayerType player) {
        int offset = (player == PlayerType.PLAYER_1)? 1 : 2;
        int [] array = board.getBoard()[offset];
        for (int item:array) {
            if (item != 0) {
                return false;
            }
        }
        return true;
    }

    /** Controls the flow of a idiomatic Bao game, handles turn and win-checking
    * @param isHumanPlayer1 Whether Player 1 in this particular game is played by a human.
    * @param isHumanPlayer2 Whether Player 2 in this particular game is played by a human.
    */
    public void start(Boolean isHumanPlayer1, Boolean isHumanPlayer2) {
        player1 = isHumanPlayer1 ? new HumanPlayer(board, PlayerType.PLAYER_1) : 
                                    new AIPlayer(board, PlayerType.PLAYER_2, 20);
        player2 = new AIPlayer(board, PlayerType.PLAYER_2, 20);
        while (running) {
            System.out.println("\n\tPlayer 1\n");
            player1.turnDone = false;
            if (!running) { return; }
            player1.nextTurn();
            if (hasWon(PlayerType.PLAYER_1)) {
                System.out.println("Player 1 has won.");
                notifyWinner("Player 1");
            }
            if (hasWon(PlayerType.PLAYER_2)) {
                System.out.println("Player 2 has won.");
                notifyWinner("Player 2");
            }
            board.printBoard();
            player1.turnDone = true;
            System.out.println("\n\tPlayer 2\n");
            player2.turnDone = false;
            if (!running) { return; }
            player2.nextTurn();
            if (hasWon(PlayerType.PLAYER_1)) {
                System.out.println("Player 1 has won.");
                notifyWinner("Player 1");
            }
            if (hasWon(PlayerType.PLAYER_2)) {
                notifyWinner("Player 2");
            }
            board.printBoard();
            player2.turnDone = true;
        }
    }

    /** Function that sets flags to stop the running of the game. */
    public void stop() {
        System.out.println("stopping game instance.");
        player1.inRunningInstance = false;
        player2.inRunningInstance = false;
        running = false;
    }

    /** Function to notify that someone has won the game in this instance. 
    *
    * @param winnerName the name of the player that has won.
    *
    */
    public void notifyWinner(String winnerName) {
        setChanged();
        notifyObservers(winnerName);
    }

    public ArrayList<BaoPlayer> returnPlayers() {
        return new ArrayList<BaoPlayer>(Arrays.asList(player1,player2));
    }
}
