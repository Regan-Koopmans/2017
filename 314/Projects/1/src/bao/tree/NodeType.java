package bao.tree;

/** 
* A helper class that encapsulates the types of game tree nodes.
*/

public enum NodeType { 
	MIN, MAX;

	public static NodeType opposite(NodeType input) {
		if (input == MAX) { return MIN; }
		else { return MAX; }
	}
}