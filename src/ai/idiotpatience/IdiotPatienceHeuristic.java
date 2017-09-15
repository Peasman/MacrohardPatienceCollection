package ai.idiotpatience;

import java.util.ArrayList;

import model.GameCard;
import model.GameCard.Rank;
import model.CardStackType;

public class IdiotPatienceHeuristic {

	private static IdiotPatienceHeuristic instance = new IdiotPatienceHeuristic();

	private ArrayList<CardStackType> rowList;
	private CardStackType stacker;

	private IdiotPatienceHeuristic() {
		stacker = CardStackType.STACKER_1;

		rowList = new ArrayList<>();
		rowList.add(CardStackType.ROW_1);
		rowList.add(CardStackType.ROW_2);
		rowList.add(CardStackType.ROW_3);
		rowList.add(CardStackType.ROW_4);
	}

	public static IdiotPatienceHeuristic getInstance() {
		if (instance == null) {
			instance = new IdiotPatienceHeuristic();
		}
		return instance;
	}

	public int getHeuristicValue(IdiotPatienceGameState gameState) {
		int value = (48 - gameState.gameState().getStack(stacker).size()) * 2 + (4 - numAcesOnBottom(gameState)) * 2
				+ (-4 + emptyRows(gameState)) + (-4 + rowCount(gameState));

		return value;
	}

	private int rowCount(IdiotPatienceGameState gameState) {
		int count = 0;
		for (CardStackType row : rowList) {
			count += gameState.gameState().getStack(row).size();
		}
		return count;
		}
	
	private int numAcesOnBottom(IdiotPatienceGameState gameState) {
		int count = 0;
		for (CardStackType row : rowList) {
			if (gameState.gameState().getStack(row).size() > 0) {
				GameCard lowest = gameState.gameState().getStack(row).toList().get(0);
				if (lowest.getRank() == Rank.ACE)
					count++;
			}
		}
		return count;
	}

	private int emptyRows(IdiotPatienceGameState gameState) {
		int count = 0;
		for (CardStackType row : rowList) {
			if (gameState.gameState().getStack(row).size() == 0)
				count++;
		}

		return count;

	}
}
