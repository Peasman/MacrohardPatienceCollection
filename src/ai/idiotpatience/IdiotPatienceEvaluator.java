package ai.idiotpatience;

import ai.general.Evaluator;

public class IdiotPatienceEvaluator implements Evaluator<IdiotPatienceGameState> {

	@Override
	public int evaluate(IdiotPatienceGameState gameState) {
		return IdiotPatienceHeuristic.getInstance().getHeuristicValue(gameState);

		// FreecellHeuristic.getInstance().getHeuristicValue(gameState) * 100;
	}

}
