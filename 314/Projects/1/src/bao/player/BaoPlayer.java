/*

    CLASS       : BaoPlayer
    AUTHOR      : Regan Koopmans
    DESCRIPTION : Defines an abstract Bao player, which is fully implemented
                  by HumanPlayer and AI Player

 */

package bao.player;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import bao.BaoBoard;
import bao.Move;

public abstract class BaoPlayer {

    public AtomicInteger seedLocation = null;
    public AtomicInteger takasaLocation = null;
    public Direction direction = null;
    public volatile boolean turnDone = true;
    public TurnType turnType = null;

    protected PlayerType playerType;
    protected BaoBoard board;
    public int seedsInStock = 22;

// These methods will have different implementations, depending on whether
// a player is human or an artificial intelligence.

    // Functions to handle actions in the Numua stage

    public abstract int getNamuaCapLoc(ArrayList<Integer> captureMoves);
    public abstract int getNamuaNonCapLoc(ArrayList<Integer> nonCaptureMoves);
    
    // Functions to handle actions in the Mtaji stage

    public abstract int getMtajiCapMove();
    public abstract int getMtajiNonCapMove();

    public abstract Direction getDirection();

// The concept of a turn is concrete for both, and hence can be described
// abstractly

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
                int location = getNamuaNonCapLoc(nonCaptureMoves);
                if (location <= 1) {
                    direction = Direction.LEFT;
                }
                else if (location >= 6) {
                    direction = Direction.RIGHT;
                }
                else {
                    direction = getDirection();
                }
                board.spread(playerType, location, direction);

            }
            else {
                
                turnType = TurnType.CAPTURE;
                int location = getNamuaCapLoc(captureMoves);
                int captured = board.placeSeed(playerType, location);
                if (captured > 0) {
                    int sowLocation;
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
                        sowFromDirection = getDirection();
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
                ArrayList<Move> nonCaptureMoves = board.getMtajiNonCapMoves(playerType);
            }
            
        }
        direction = null;
        System.out.println(seedsInStock);
    }

    public BaoPlayer(BaoBoard board, PlayerType playerType) {
        this.board = board;
        this.playerType = playerType;
    }
}
