package bao.tree;

import bao.BaoBoard;

public class GameNode {
    private BaoBoard state = null;
    private NodeType type = null;
    private Double value = null;

    public GameNode(BaoBoard state, NodeType type) {
    	this.type = type;
    	this.state = state;
    	if (type == NodeType.MAX) {
    		value = Double.NEGATIVE_INFINITY;   // alpha
    	} else {
    		value = Double.POSITIVE_INFINITY;   // beta
    	}
    }

    public double getValue() {
    	// calculate the desirability of this node.
    	return 0;
    }
}
