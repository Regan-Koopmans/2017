/*

    CLASS       : AIPlayer
    AUTHOR      : Regan Koopmans
    DESCRIPTION : Defines a concrete BaoPlayer, which uses a game tree
                  to determine moves.

 */

package bao.player;

import java.util.ArrayList;
import bao.tree.GameTree;
import bao.BaoBoard;

public class AIPlayer extends BaoPlayer {

    private GameTree tree = null;

    public AIPlayer(BaoBoard board, PlayerType playerType, int depth) {
        super(board, playerType);
    }

    public int getCaptureLocation(ArrayList<Integer> captureMoves) {
        tree = new GameTree();
        for (Integer x: captureMoves) {
            
        }
        return captureMoves.get(0);
    }

    public int getNonCaptureLocation(ArrayList<Integer> nonCaptureMoves) {
        tree = new GameTree();
        for (Integer x: nonCaptureMoves) {

        }
        return nonCaptureMoves.get(0);
    }

    public Direction getDirection() {
        System.out.println("AI HAS CHOSEN TO SOW FROM THE LEFT");
        return Direction.LEFT;
    }

    public int getCascadeLocation() {
        return 0;
    }

    public Direction getCascadeDirection() {
        return Direction.LEFT;
    }
}
