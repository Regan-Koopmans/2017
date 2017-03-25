package bao.player;

import java.util.ArrayList;
import bao.tree.GameTree;
import bao.BaoBoard;
import bao.tree.GameNode;
import bao.tree.NodeType;
import bao.Move;
import bao.MoveType;

/**
*  <h1> AIPlayer </h1>
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

    // At the moment this function only creates a tree of height 1.
    // This means that the function chooses the best current option
    // based on its immediate results. Greedy.

    public Move getNamuaCapMove(ArrayList<Integer> captureMoves) {
        tree = new GameTree(board);
        Move bestMove = tree.getBestMove(playerType);
        return bestMove;
    }

    public Move getNamuaNonCapMove(ArrayList<Integer> nonCaptureMoves) {
        tree = new GameTree(board);
        Move bestMove = tree.getBestMove(playerType);
        return bestMove;
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

    public Move getMtajiCapMove() {
        tree = new GameTree(board);
        Move bestMove = tree.getBestMove(playerType);
        return bestMove;
    }
    public Move getMtajiNonCapMove(){
        tree = new GameTree(board);
        Move bestMove = tree.getBestMove(playerType);
        return bestMove;
    }
}
