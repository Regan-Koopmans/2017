package bao.player;

import java.util.ArrayList;
import bao.tree.GameTree;
import bao.BaoBoard;
import bao.tree.GameNode;
import bao.tree.NodeType;
import bao.Move;
import bao.MoveType;

/**
*  
*  Defines a concrete BaoPlayer, which uses a game tree
*  and suitable heuristics to determine next moves.
*
*
*  @author Regan Koopmans.
*/

public class AIPlayer extends BaoPlayer {

    /** The GameTree which the class uses to calculate moves */
    private GameTree tree = null;

    /** Constructor, currently does not use the depth parameter */
    public AIPlayer(BaoBoard board, PlayerType playerType, int depth) {
        super(board, playerType);
    }

    /** 
    * Gets next Numua capture move. Invokes GameTree's abprune method.
    * @param captureMoves an ArrayList of integers listing the possible capture locations.
    * @return A Move object representing the most ideal move as identified by the algorithm.
    */

    public Move getNamuaCapMove(ArrayList<Integer> captureMoves) {
        tree = new GameTree(board);
        return tree.abprune(playerType);
    }

    /** 
    * Gets next Numua capture move. Invokes GameTree's abprune method.
    * @param nonCaptureMoves an ArrayList of integers listing the possible capture locations.
    * @return A Move object representing the most ideal move as identified by the algorithm.
    */

    public Move getNamuaNonCapMove(ArrayList<Integer> nonCaptureMoves) {
        tree = new GameTree(board);
        return tree.abprune(playerType);
    }

    /**
    * Function that determines the optimal direction for a move to be made in.
    *
    * @return Direction The direction that the algorithm has selected.
    */

    public Direction getDirection() {
        // System.out.println("AI HAS CHOSEN TO SOW FROM THE LEFT");
        return Direction.LEFT;
    }

    /** 
    * Gets next Mtaji capture move. Invokes GameTree's abprune method.
    * @param captureMoves an ArrayList of integers listing the possible capture locations.
    * @return A Move object representing the most ideal move as identified by the algorithm.
    */

    public Move getMtajiCapMove() {
        tree = new GameTree(board);
        return tree.abprune(playerType);
    }

    /** 
    * Gets next Mtaji Takasa move. Invokes GameTree's abprune method.
    * @param captureMoves an ArrayList of integers listing the possible capture locations.
    * @return A Move object representing the most ideal move as identified by the algorithm.
    */

    public Move getMtajiNonCapMove(){
        tree = new GameTree(board);
        return tree.abprune(playerType);
    }
}
