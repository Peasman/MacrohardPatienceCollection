package ai.freecell;

import java.util.ArrayList;

import model.Battlefield;
import model.GameCard;
import model.GameCard.Rank;
import model.CardStack;
import model.CardStackType;

public class FreecellHeuristic {

	private static FreecellHeuristic instance = new FreecellHeuristic();

	public ArrayList<CardStackType> homeCellList;

	public ArrayList<CardStackType> freeCellList;

	public ArrayList<CardStackType> rowList;
	
	public final int foundationMultipyer = 8;

	private FreecellHeuristic() {
		homeCellList = new ArrayList<>();
		homeCellList.add(CardStackType.STACKER_1);
		homeCellList.add(CardStackType.STACKER_2);
		homeCellList.add(CardStackType.STACKER_3);
		homeCellList.add(CardStackType.STACKER_4);

		freeCellList = new ArrayList<>();
		freeCellList.add(CardStackType.FREECELL_1);
		freeCellList.add(CardStackType.FREECELL_2);
		freeCellList.add(CardStackType.FREECELL_3);
		freeCellList.add(CardStackType.FREECELL_4);

		rowList = new ArrayList<>();
		rowList.add(CardStackType.ROW_1);
		rowList.add(CardStackType.ROW_2);
		rowList.add(CardStackType.ROW_3);
		rowList.add(CardStackType.ROW_4);
		rowList.add(CardStackType.ROW_5);
		rowList.add(CardStackType.ROW_6);
		rowList.add(CardStackType.ROW_7);
		rowList.add(CardStackType.ROW_8);
	}

	public static FreecellHeuristic getInstance() {
		if (instance == null) {
			instance = new FreecellHeuristic();
		}
		return instance;
	}

	public int getHeuristicValue(FreecellGameState gameState) {
//		return 64 - sumTopHomeCards(gameState) - freeCells(gameState) + sequenceBrakes(gameState) + colorDifferenceOnFoundation(gameState);
		
		
		
		
		return getHSDHVariant(gameState) + 4 - freeCells(gameState) + sequenceBrakes(gameState) + colorDifferenceOnFoundation(gameState);
		
		
//		return getHSDHVariant(gameState) + 4 - freeCells(gameState) + sequenceBrakes(gameState);
		
		
//		return (int) (getHSDHVariant(gameState) + 8 - freeCells(gameState) * 2 + sequenceBrakes(gameState) * 1.5); // 1a
		
		
//		return 64 - sumTopHomeCards(gameState) - freeCells(gameState) + sequenceBrakes(gameState);
//		return 140 - (sumTopHomeCards(gameState) * 2) - (freeCells(gameState) * 3) + (sequenceBrakes(gameState) * 3); // + getHSDH(gameState);
	}

	public int sumTopHomeCards(FreecellGameState gameState) {
		int sum = 0;
		for (CardStackType homeCell : homeCellList) {
			GameCard upperCard = gameState.gameState().getStack(homeCell).peek();
			if (upperCard != null) {
				sum += upperCard.getRank().getRankValue();
			}
		}
		return sum;
	}

	public int freeCells(FreecellGameState gameState) {
		int sum = 0;
		for (CardStackType freeCell : freeCellList) {
			if (gameState.gameState().getStack(freeCell).peek() == null) {
				sum++;
			}
		}
		for (CardStackType row : rowList) {
			if (gameState.gameState().getStack(row).peek() == null) {
				sum++;
			}
		}
		return sum;
	}

	public int sequenceBrakes(FreecellGameState gameState) {
		int sum = 0;
		for (CardStackType row : rowList) {
			ArrayList<GameCard> cards = gameState.gameState().getStack(row).toList();
			for (int i = 1; i < cards.size(); i++) {
				GameCard firstCard = cards.get(i - 1);
				GameCard secondCard = cards.get(i);
				if (firstCard.sameColorAs(secondCard)
						&& firstCard.getRank().getRankValue() - secondCard.getRank().getRankValue() != 1) {
					sum++;
				}
				
//				System.out.println(firstCard.toProtocol() + "  " + secondCard.toProtocol() + "  " + (!firstCard.sameColorAs(secondCard)
//						&& firstCard.getRank().getRankValue() - secondCard.getRank().getRankValue() == 1));
			}
		}
		return sum;
	}

	public int getHSDHVariant(FreecellGameState gameState) {
		int score = 52 * foundationMultipyer;
		
		Battlefield battlefield = gameState.gameState();
		ArrayList<GameCard> cardsToSearch = new ArrayList<>(4);
		for (CardStackType homeCell : homeCellList) {
			CardStack stack = battlefield.getStack(homeCell);
			GameCard upperCard = stack.peek();
			if (upperCard != null) {
				score -= upperCard.getRank().getRankValue() * foundationMultipyer;
				if (!upperCard.getRank().equals(Rank.KING)) {
					cardsToSearch.add(new GameCard(stack.getSuit(), Rank.values()[upperCard.getRank().ordinal() + 1], true));
				}
			} else {
				cardsToSearch.add(new GameCard(stack.getSuit(), Rank.ACE, true));
			}
		}
		for (CardStackType row : rowList) {
			ArrayList<GameCard> stack = battlefield.getStack(row).toList();
			for (GameCard card : cardsToSearch) {
				int index = stack.indexOf(card);
				if (index != -1) {
					score += stack.size() - index - 1;
				}
			}
		}
		return score;
	}
	
	public int colorDifferenceOnFoundation(FreecellGameState gameState) {
		Battlefield battlefield = gameState.gameState();
		int reds = 0;
		int blacks = 0;
		for (CardStackType homeCell : homeCellList) {
			ArrayList<GameCard> stack = battlefield.getStack(homeCell).toList();
			for (GameCard card : stack) {
				if (card.isRed()) {
					reds++;
				} else {
					blacks++;
				}
			}
		}
		
		return Math.abs(blacks - reds);
	}
	
}

