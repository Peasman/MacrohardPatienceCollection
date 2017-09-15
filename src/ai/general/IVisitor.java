package ai.general;

public interface IVisitor {
	
	void visitNode (IGameState gameState, int identifier);
	
	void visitEdge (int parent, int child);
	
}
