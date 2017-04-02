package bao.tree;

public enum NodeType { 
	MIN, MAX;

	public static NodeType opposite(NodeType input) {
		if (input == MAX) { return MIN; }
		else { return MAX; }
	}
}