package ai.zankpatience;

import model.Battlefield;
import model.CardStack;
import model.CardStackType;
import model.Player;

public class ZankGameState {

	private boolean isPlayerOne;
	private CardStack myTalon;
	private CardStack myReserve;
	private CardStack myRest;
	private CardStack enemyReserve;
	private CardStack enemyRest;
	private CardStack[] myStacks;
	private CardStack[] enemyPunishStacks;
	private CardStack[] fieldStacks = new CardStack[8];

	public ZankGameState(Battlefield field, Player myPlayer) {
		isPlayerOne = field.getPlayerOne().equals(myPlayer);
		if (isPlayerOne) {
			myTalon = field.getStack(CardStackType.ZANK_MIDDLE_1);
			myReserve = field.getStack(CardStackType.ZANK_RIGHT_1);
			myRest = field.getStack(CardStackType.ZANK_LEFT_1);
			enemyReserve = field.getStack(CardStackType.ZANK_RIGHT_2);
			enemyRest = field.getStack(CardStackType.ZANK_LEFT_2);
		} else {
			myTalon = field.getStack(CardStackType.ZANK_MIDDLE_2);
			myReserve = field.getStack(CardStackType.ZANK_RIGHT_2);
			myRest = field.getStack(CardStackType.ZANK_LEFT_2);
			enemyReserve = field.getStack(CardStackType.ZANK_RIGHT_1);
			enemyRest = field.getStack(CardStackType.ZANK_LEFT_1);
		}
		myStacks = new CardStack[] { myReserve, myRest, myTalon };
		enemyPunishStacks = new CardStack[] { enemyReserve, enemyRest };
		for (int i = 0; i < 8; i++) {
			fieldStacks[i] = field.getStack(CardStackType.valueOf("ROW_" + (i + 1)));
		}
	}

	public CardStack getMyTalon() {
		return myTalon;
	}

	public CardStack getMyReserve() {
		return myReserve;
	}

	public CardStack getMyRestStack() {
		return myRest;
	}

	public CardStack getEnemyReserve() {
		return enemyReserve;
	}

	public CardStack getEnemyRest() {
		return enemyRest;
	}

	public CardStack[] getMyStacks() {
		return myStacks;
	}

	public CardStack[] getEnemyPunishStacks() {
		return enemyPunishStacks;
	}

	public CardStack[] getFieldStacks() {
		return fieldStacks;
	}

}