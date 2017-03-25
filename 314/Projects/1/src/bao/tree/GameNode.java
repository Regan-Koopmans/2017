package bao.tree;

import bao.BaoBoard;
import bao.player.PlayerType;
import java.util.ArrayList;
import bao.Move;

public class GameNode {
    public BaoBoard board = null;
    private NodeType type = null;
    private Double value = null;
    private ArrayList<GameNode> children =  null;

    public GameNode(BaoBoard board, NodeType type) {
    	this.type = type;
    	this.board = new BaoBoard(board);
    	if (type == NodeType.MAX) {
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

        int numCapture    = board.getNamuaCapMoves(player).size();
        int numNonCapture = board.getNamuaNonCapMoves(player).size();
        int frontRow      = board.filledHolesInFrontRow(player);
        int seedsOnBoard  = board.seedsOnBoard(player);
    	return seedsOnBoard + frontRow + 0.5*(numCapture+numNonCapture);
    }

    public Move getBestMoveRecursive(PlayerType playerType, int level) {
        
        Move returnMove = null;

        /* Tree is hard coded to depth of 4 */

        if (level < 4) {

            ArrayList<Double> childValues = new ArrayList<Double>();

            ArrayList<Move> moves = board.getMoves(playerType,1); 

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
