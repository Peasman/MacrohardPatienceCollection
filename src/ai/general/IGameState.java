package ai.general;

import java.io.Serializable;
import java.util.LinkedList;

import search.StackChain;

public abstract class IGameState implements Serializable {
	private static final long serialVersionUID = -2171647228108046418L;

	protected int score = 0;

	protected GameState gameState = null;

	protected StackChain<IMove> moveHistory = null;

	protected IMove lastMove = null;

	public IGameState(GameState gameState) {
		this.gameState = gameState;
	}

	int getScore() {
		return score;
	}

	void setScore(int newScore) {
		score = newScore;
	}

	public void setMoveHistory(StackChain<IMove> newMoveHistory) {
		moveHistory = newMoveHistory;
	}

	public StackChain<IMove> getMoveHistory() {
		return moveHistory;
	}

	public GameState gameState() {
		return gameState;
	}

	public GameState gameState(GameState newGameState) {
		GameState oldState = gameState;
		gameState = newGameState;
		return oldState;
	}

	public void setLastMove(IMove lastMove) {
		this.lastMove = lastMove;
	}

	public IMove getLastMove() {
		return lastMove;
	}

	public abstract IGameState copy();

	public abstract Object key();

	public abstract LinkedList<IMove> validMoves();

	public abstract boolean execute(IMove move);

	public abstract boolean undo(IMove move);
}
