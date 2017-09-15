package ai.freecell;

import ai.general.Evaluator;

public class FreecellEvaluator implements Evaluator<FreecellGameState> {

	@Override
	public int evaluate(FreecellGameState gameState) {
		return FreecellHeuristic.getInstance().getHeuristicValue(gameState);

		// FreecellHeuristic.getInstance().getHeuristicValue(gameState) * 100;
	}

}
