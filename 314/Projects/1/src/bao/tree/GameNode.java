package bao.tree;

import bao.BaoBoard;
import bao.player.PlayerType;
import java.util.ArrayList;
import bao.Move;
import static bao.tree.NodeType.*;

import static java.lang.System.out;

public class GameNode {
    
    /** A BaoBoard encapsulating the node's most important internal state. */

    public BaoBoard board = null;
   
    /** */

    private NodeType type = null;
    
    /** The value of the node state, used to calculate next moves. */

    private Double value = null;
    
    /** An array list */

    public ArrayList<GameNode> children =  null;

    public GameNode(BaoBoard board, NodeType type) {
    	this.type = type;
    	this.board = new BaoBoard(board);
    	if (type == MAX) {
    		value = Double.NEGATIVE_INFINITY;
    	} else {
    		value = Double.POSITIVE_INFINITY;
    	}
        children = new ArrayList<GameNode>();
    }

    /** 
    * Function that calculates the value
    * @param player the "perspective" we are calculating the state value for.
    * @return the value of the current state of the board for a given player.
    */

    public double getValue(PlayerType player) {

        // currently I am using the following heuristic:

        //     desirability =  number of capture moves made avaliable
        //                      + number of noncapture moves made avaliable
        //                       + number of defensive holes.

        PlayerType opponent = PlayerType.opposite(player);

        int numCapture    = board.getNamuaCapMoves(player).size();
        int frontRow      = board.filledHolesInFrontRow(player);
        int seedsOnBoard  = board.seedsOnBoard(player);
        
        int opposeCapture = board.getNamuaCapMoves(PlayerType.opposite(player)).size();
        int frontRowOpponent = board.filledHolesInFrontRow(opponent);
        int seedsOnBoardOp  = board.seedsOnBoard(opponent);
        int amountVulnerable = board.numVulnerable(player);
        int holesGreaterThan6 = board.sumHolesGreaterThan(player, 6);

    	// return seedsOnBoard + frontRow + numCapture 
                // - holesGreaterThan6 - opposeCapture - frontRowOpponent - seedsOnBoardOp - amountVulnerable;
        return frontRow+seedsOnBoard-amountVulnerable-0.2*holesGreaterThan6 -opposeCapture;
    }

    /**
    * Main alpha-beta function, performs recursive search for next move.
    * @param node the first value.
    * @param depth the current depth of the search, decrements from max to zero before terminating.
    * @param alpha the maintained alpha value, as per algorithm.
    * @param beta the maintained beta value, as per algorithm.
    * @param maxPlayer boolean flag determining whether the current play is done by the MAX participle.
    * @param player The type of player in terms of the Bao game (ie PLAYER_1 or PLAYER_2).
    * @return A double containing the value of a given move, with respect the the alpha beta algorithm.
    */

    public double alphabeta(GameNode node, int depth, double alpha, double beta, boolean maxPlayer, PlayerType player) {
        if (depth == 0) { 
            return node.getValue(player); 
        }
        double v;
        ArrayList<Move> moves = node.board.getMoves(player,1); 
        int move_size = moves.size();
        GameNode child;
        int x;
        for (x = 0; x < move_size; x++) {
            child = new GameNode(board, NodeType.opposite(type));
            child.board.makeMove(moves.get(x), player);
            node.children.add(child);
        }
        int child_size = children.size();
        if (maxPlayer) {   
            v = Double.NEGATIVE_INFINITY;
            for (x = 0; x < child_size; x++) {
                child = children.get(x);
                v = max(v, node.getValue(player) + alphabeta(child, depth-1, alpha, beta, false, PlayerType.opposite(player)));
                alpha = max(alpha, v);
                if (beta <= alpha) { break; }
            }
            return v;
        } else {
            v = Double.POSITIVE_INFINITY;
            for (x = 0; x < child_size; x++) {
                child = children.get(x);
                v = min(v, node.getValue(player) + alphabeta(child, depth-1, alpha, beta, true, PlayerType.opposite(player)));
                alpha = min(alpha, v);
                if (beta <= alpha) { break; }
            }
            return v;
        }
    }

    /**
    * Helper function to determine the greater of two doubles
    * @param a the first value.
    * @param b the second value.
    */

    public static double max(double a, double b) {
        if (a >= b) { return a; } else { return b; }
    }

    /**
    * Helper function to determine the lesser of two doubles
    * @param a the first value.
    * @param b the second value.
    */

    public static double min(double a, double b) {
        if (a < b) { return a; } else { return b; }
    }

    /**
    * The main pure MINIMAX function. <b>No longer used</b>, <tt>alphabeta</tt> is now 
    * used to calculate next moves. Function has been kept for comparison purposes. 
    * @param playerType the type of player asking for the move.
    * @param level an integer maintaining the current depth of the recursive search.
    */

    public Move getBestMoveRecursive(PlayerType playerType, int level) {
        
        Move returnMove = null;

        if (level < 4) {

            ArrayList<Double> childValues = new ArrayList<Double>();
            ArrayList<Move> moves = board.getMoves(playerType,1); 
            if (moves.isEmpty()) { 
                return null; 
            }

            for (Move m:moves) {
                GameNode child = new GameNode(board, NodeType.opposite(type));
                child.board.makeMove(m, playerType);
                children.add(child);
            }
            
            for (GameNode child:children) {
                child.getBestMoveRecursive(PlayerType.opposite(playerType), level+1);
                childValues.add(child.value);
            }

            if (type == NodeType.MAX) {
                int max_child = 0;
                for (int x = 1; x < children.size(); ++x) {
                    if (children.get(x).value > children.get(max_child).value) {
                        max_child = x;
                    }
                }
                value = children.get(max_child).value;
                returnMove = moves.get(max_child);
            
            } else {
                int min_child = 0;
                for (int x = 1; x < children.size(); ++x) {
                    if (children.get(x).value < children.get(min_child).value) {
                        min_child = x;
                    }
                }
                value = children.get(min_child).value;
                returnMove = moves.get(min_child);
            }
        } else {
            value = getValue(playerType);
        }
        return returnMove;
    }
}
