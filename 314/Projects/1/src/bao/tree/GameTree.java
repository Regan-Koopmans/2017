package bao.tree;

import bao.BaoBoard;
import java.util.ArrayList;
import bao.Move;
import bao.MoveType;
import bao.player.Direction;
import bao.player.PlayerType;

/** 
* Class that is concerned with the construction and various operations of a game tree,
* as used by an artificially intelligent player.
*/

public class GameTree {

    /** The constant max depth of the tree. */

    public static int DEPTH = 7;
    
    private GameNode root = null;

    public GameTree(BaoBoard board) {
    	root = new GameNode(board, NodeType.MAX);
    }

    /** 
    * Function to perform pure MINIMAX, no longer used in main program. Kept for comparison purposes.
    * @param player The type of player performing the recursive move search.
    * @return A Move object representing the most ideal move as identified by the algorithm.
    */

    public Move getBestMove(PlayerType playerType) {
    	return root.getBestMoveRecursive(playerType, 0);
    }

    /** 
    * Performs alpha-beta pruning to determine a beneficial next move. 
    * @param player The type of player performing the recursive move search.
    * @return A Move object representing the most ideal move as identified by the algorithm.
    */

    public Move abprune(PlayerType player) {
        double v = Double.NEGATIVE_INFINITY;
        double old_v = Double.NEGATIVE_INFINITY;
        int max_move;
        ArrayList<Move> moves = root.board.getMoves(player,1); 
        GameNode child;
        int depth = DEPTH;
        double alpha = Double.NEGATIVE_INFINITY; 
        double beta = Double.POSITIVE_INFINITY;
        int x;
        for (x = 0; x < moves.size(); x++) {
            child = new GameNode(root.board, NodeType.MIN);
            child.board.makeMove(moves.get(x), player);
            root.children.add(child);
        }
        v = Double.NEGATIVE_INFINITY;
        max_move = 0;
        for (x = 0; x < root.children.size(); x++) {
            child = root.children.get(x);
            old_v = v;
            v = GameNode.max(v, root.alphabeta(child, depth-1, alpha, beta, false, PlayerType.opposite(player)));
            System.out.println("Move " + moves.get(x) + " equals " + v);
            if (v > old_v) {
                max_move = x;
            }
            alpha = GameNode.max(alpha, v);
            if (beta <= alpha) { break; }
            child.board.printBoard();
        }
        System.out.println("Choosing " + max_move);
        return moves.get(max_move);
    }
}
