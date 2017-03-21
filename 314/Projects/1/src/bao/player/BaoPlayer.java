package bao.player;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import bao.BaoBoard;
import bao.Move;

/**
*
* Defines an abstract Bao player, which is fully implemented
* by HumanPlayer and AI Player  
*
*
* @author Regan Koopmans
*/
public abstract class BaoPlayer {

    public AtomicInteger seedLocation = null;
    public AtomicInteger takasaLocation = null;

    public AtomicInteger mtajiCapLocation = null;
    public AtomicInteger mtajiNonCapLocation = null;
    
    public Direction direction = null;
    public volatile boolean turnDone = true;
    public TurnType turnType = null;

    protected PlayerType playerType;
    protected BaoBoard board;
    public int seedsInStock = 22;
    public volatile boolean inRunningInstance = true;

// These methods will have different implementations, depending on whether
// a player is human or an artificial intelligence.

    // Functions to handle actions in the Numua stage

    public abstract Move getNamuaCapMove(ArrayList<Integer> captureMoves);
    public abstract Move getNamuaNonCapMove(ArrayList<Integer> nonCaptureMoves);
    
    // Functions to handle actions in the Mtaji stage

    public abstract Move getMtajiCapMove();
    public abstract Move getMtajiNonCapMove();

    public abstract Direction getDirection();


/**
* Function that controls the structure of a Bao turn, and directs behaviour based on the
* type of turn that is occuring.
*
* The concept of a turn is fixed for both Human and AI players, and hence can be described
* abstractly
*/ 
    public void nextTurn() {
        if (seedsInStock > 0) {

            // Numua Turn

            ArrayList<Integer> captureMoves = board.getNamuaCapMoves(playerType);
            if (captureMoves.isEmpty()) {
                
                turnType = TurnType.TAKASA;
                ArrayList<Integer> nonCaptureMoves =
                    board.getNamuaNonCapMoves(playerType);
                System.out.println("Noncapture moves = " +
                                   nonCaptureMoves.toString());
                Move namuaNonCapMove = getNamuaNonCapMove(nonCaptureMoves);
                int offset  = (playerType == PlayerType.PLAYER_1) ? 2 : 1; 
                int location = namuaNonCapMove.getLocation();
                if (location <= 1) {

                    System.out.println("SOWING");
                    direction = Direction.LEFT;
                    int num_seeds = board.getBoard()[offset][location] + 1; 
                    board.getBoard()[offset][location] = 0;
                    board.sow(playerType, num_seeds,direction);
                    seedsInStock--;
                }
                else if (location >= 6) {

                    System.out.println("SOWING");
                    direction = Direction.RIGHT;
                    int num_seeds = board.getBoard()[offset][location] + 1; 
                    board.getBoard()[offset][location] = 0;
                    board.sow(playerType, num_seeds,direction);
                    seedsInStock--;
                }
                else {
                    direction = namuaNonCapMove.getDirection();
                    board.spread(playerType, location, direction);
                }

            }
            else {
                
                System.out.println("Capture moves: " + captureMoves);
                turnType = TurnType.CAPTURE;
                Move namuaCapMove = getNamuaCapMove(captureMoves);
                int location = namuaCapMove.getLocation();

                int captured = board.placeSeed(playerType, location);

                if (captured > 0) {
                    Direction sowFromDirection;

                    // Locations 0-1 and 6-7 automatically sow from the
                    // opposite side.

                    if (location <= 1) {
                        sowFromDirection = Direction.LEFT;
                    }
                    else if (location >= 6) {
                        sowFromDirection = Direction.RIGHT;
                    }
                    else {
                        sowFromDirection = namuaCapMove.getDirection();
                    }
                    board.sow(playerType, captured, sowFromDirection);
                }
                else {
                    System.out.println("Player did not capture any seeds");
                }
                --seedsInStock;
            }
        }
        else {

            // Mtaji Turn

            System.out.println("Player has no seeds in stock.");
            ArrayList<Move> captureMoves = board.getMtajiCapMoves(playerType);
            if (!captureMoves.isEmpty()) {
                // yay!
            } else {
                ArrayList<Move> nonCaptureMoves = 
                                board.getMtajiNonCapMoves(playerType);
            }
            
        }
        direction = null;
    }

    /**
    * Constructor for BaoPlayer.
    * @param board the board that this player will bind to and play on.
    *
    * @param playerType the type of player (Player1/Player2) that the player 
    * will play as.
    */

    public BaoPlayer(BaoBoard board, PlayerType playerType) {
        this.board = board;
        this.playerType = playerType;
    }
}
