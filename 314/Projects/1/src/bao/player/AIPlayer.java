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
*  @author Regan Koopmans
*/


public class AIPlayer extends BaoPlayer {

    /** The GameTree which the class uses to calculate moves */
    private GameTree tree = null;

    public AIPlayer(BaoBoard board, PlayerType playerType, int depth) {
        super(board, playerType);
    }

    // At the moment this function only creates a tree of height 1.
    // This means that the function chooses the best current option
    // based on its immediate results. Greedy.

    public Move getNamuaCapMove(ArrayList<Integer> captureMoves) {
        tree = new GameTree();
        int best_move_location = 0;
        Direction best_move_direction = Direction.RIGHT;
        GameNode state;
        
        double greatest_payoff = 0;

        for (int x = 0; x < captureMoves.size(); x++) {
            state = new GameNode(board, NodeType.MAX);
            int captured = state.board.placeSeed(playerType, captureMoves.get(x));
            state.board.sow(playerType, captured, Direction.RIGHT);
            double stateValue = state.getValue(playerType);
            if (stateValue > greatest_payoff) {
                greatest_payoff = stateValue;
                best_move_location = x;
            }
        }
        System.out.println("Selecting " + captureMoves.get(best_move_location));
        return new Move(captureMoves.get(best_move_location), best_move_direction, 
                            MoveType.NamuaCapture);
    }

    public Move getNamuaNonCapMove(ArrayList<Integer> nonCaptureMoves) {
        tree = new GameTree();
        for (Integer x: nonCaptureMoves) {

        }
        return new Move(nonCaptureMoves.get(0), Direction.RIGHT, MoveType.NamuaTakasa);
    }

    /**
    *
    * @return Direction The direction that the algorithm has selected.
    */
    public Direction getDirection() {
        System.out.println("AI HAS CHOSEN TO SOW FROM THE LEFT");
        return Direction.LEFT;
    }

    public Move getMtajiCapMove() {
        return new Move(0, Direction.LEFT, MoveType.MtajiCapture);
    }
    public Move getMtajiNonCapMove(){
        return new Move(0, Direction.LEFT, MoveType.MtajiTakasa);
    }
}
