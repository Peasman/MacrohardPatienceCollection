package ai.general;

import exceptions.InconsistentMoveException;

public interface GameState {

	void executeMove(IMove move) throws InconsistentMoveException;
	
}
