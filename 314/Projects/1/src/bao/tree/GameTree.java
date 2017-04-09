/*

    CLASS       : GameTree
    AUTHOR      : Regan Koopmans
    DESCRIPTION : Defines a MINIMAX tree, using GameNodes. Used by AIPlayer to
    			  make a decision.

 */
package bao.tree;

import bao.BaoBoard;
import java.util.ArrayList;
import bao.Move;
import bao.MoveType;
import bao.player.Direction;
import bao.player.PlayerType;

public class GameTree {

    public static int DEPTH = 7;
    
    private GameNode root = null;
    int ply_depth = 2;

    public GameTree(BaoBoard board) {
    	root = new GameNode(board, NodeType.MAX);
    }

    public Move getBestMove(PlayerType playerType) {
    	return root.getBestMoveRecursive(playerType, 0);
    }

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
