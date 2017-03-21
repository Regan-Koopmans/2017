package bao.tree;

import bao.BaoBoard;
import bao.player.PlayerType;

public class GameNode {
    public BaoBoard board = null;
    private NodeType type = null;
    private Double value = null;

    public GameNode(BaoBoard board, NodeType type) {
    	this.type = type;
    	this.board = new BaoBoard(board);
    	if (type == NodeType.MAX) {
    		value = Double.NEGATIVE_INFINITY;   // alpha
    	} else {
    		value = Double.POSITIVE_INFINITY;   // beta
    	}
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
}
