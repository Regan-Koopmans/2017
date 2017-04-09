package bao.tree;

import bao.BaoBoard;
import bao.player.PlayerType;
import java.util.ArrayList;
import bao.Move;
import static bao.tree.NodeType.*;

import static java.lang.System.out;

public class GameNode {
    public BaoBoard board = null;
    private NodeType type = null;
    private Double value = null;
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

    // get the relative value of this game state, with regards
    // to a certain players perspective.

    public double getValue(PlayerType player) {

        // currently I am using the following heuristic:

        //     desirability =  number of capture moves made avaliable
        //                      + number of noncapture moves made avaliable
        //                       + number of defensive holes.

        PlayerType opponent = PlayerType.opposite(player);

        int numCapture    = board.getNamuaCapMoves(player).size();
        int frontRow      = board.filledHolesInFrontRow(player);
        int numNonCapture = board.getNamuaNonCapMoves(player).size();
        int seedsOnBoard  = board.seedsOnBoard(player);
        
        int opposeCapture = board.getNamuaCapMoves(PlayerType.opposite(player)).size();
        int frontRowOpponent = board.filledHolesInFrontRow(opponent);
        int seedsOnBoardOp  = board.seedsOnBoard(opponent);
        int amountVulnerable = board.numVulnerable(player);
        int holesGreaterThan6 = board.sumHolesGreaterThan(player, 6);

    	// return seedsOnBoard + frontRow + numCapture 
                // - holesGreaterThan6 - opposeCapture - frontRowOpponent - seedsOnBoardOp - amountVulnerable;
        return frontRow+seedsOnBoard-amountVulnerable;
    }

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

    public static double max(double a, double b) {
        if (a >= b) { return a; } else { return b; }
    }

    public static double min(double a, double b) {
        if (a < b) { return a; } else { return b; }
    }

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

            // base case: set node value at level 4 to current
            // value according to heuristic.
            value = getValue(playerType);
        }
        return returnMove;
    }
}
