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

public class GameTree {
    private GameNode root = null;
    int ply_depth = 2;

    public GameTree(BaoBoard board) {
    	root = new GameNode(board, NodeType.MAX);
    }

    public Move getBestMove(ArrayList<Integer> captureMoves) {
    	// root.getBestMoveRecursive()
    }
}
