package util;

import java.util.EnumMap;

import controller.AIController;
import functionals.GameEndedCondition;
import model.Battlefield;
import model.GameCard;
import model.GameCard.Rank;
import model.CardStackType;
import model.GameType;
import model.PlayerType;

/**
 * Beinhaltet alle GameEndedConditions für alle GameTypes
 * @author Moritz Ludolph
 */
public class WinConditionProvider {
	private static WinConditionProvider instance;
	private EnumMap<GameType, GameEndedCondition> conditionMap;

	/**
	 * Gibt die Instanz des WinConditions Providers zurück
	 * @return Die Instanz
	 */
	public static WinConditionProvider getInstance() {
		if (instance == null)
			instance = new WinConditionProvider();
		return instance;
	}

	private WinConditionProvider() {
		conditionMap = new EnumMap<GameType, GameEndedCondition>(GameType.class);
		initIdiotConditions();
		initFreeCellConditions();
		initAggroConditions();
	}

	/**
	 * Gibt für den übergebenen GameType alle GameEndedConditions zurück
	 * @param gameType Der GameType für die GameEndedConditions
	 * @return Die GameEndedConditions für den übergebenen GameType
	 */
	public GameEndedCondition getWinConditions(GameType gameType) {
		return conditionMap.get(gameType);
	}

	private boolean onlyAces(Battlefield battlefield) {
		CardStackType[] rows = new CardStackType[] { CardStackType.ROW_1, CardStackType.ROW_2, CardStackType.ROW_3,
				CardStackType.ROW_4 };
		for (CardStackType type : rows) {
			GameCard upperCard = battlefield.getUpperCard(type);
			if (upperCard == null || upperCard.getRank() != Rank.ACE) {
				return false;
			}
		}
		return true;
	}

	private boolean zankStacksEmpty(Battlefield battlefield) {
		CardStackType[] zank1 = new CardStackType[] { CardStackType.ZANK_LEFT_1, CardStackType.ZANK_MIDDLE_1,
				CardStackType.ZANK_RIGHT_1 };
		CardStackType[] zank2 = new CardStackType[] { CardStackType.ZANK_LEFT_2, CardStackType.ZANK_MIDDLE_2,
				CardStackType.ZANK_RIGHT_2 };
		boolean playerOneEmpty = true, playerTwoEmpty = true;
		for (CardStackType type : zank1) {
			if (battlefield.getStack(type).size() > 0) {
				playerOneEmpty = false;
				break;
			}
		}

		for (CardStackType type : zank2) {
			if (battlefield.getStack(type).size() > 0) {
				playerTwoEmpty = false;
				break;
			}

		}
		return playerOneEmpty || playerTwoEmpty;
	}

	private void initIdiotConditions() {
		conditionMap.put(GameType.IDIOT_PATIENCE, new GameEndedCondition() {
			@Override
			public boolean hasWon(Battlefield battlefield) {
				boolean ret = battlefield.getStack(CardStackType.TALON).size() == 0
						&& battlefield.getStack(CardStackType.STACKER_1).size() == 48 && onlyAces(battlefield);
				return ret;
			}

			@Override
			public boolean hasLost(Battlefield battlefield) {
				boolean ret = battlefield.getStack(CardStackType.TALON).size() == 0
						&& AIController.getAllPossibleMoves(battlefield).size() == 0;
				return ret;
			}
		});
	}

	private void initFreeCellConditions() {
		conditionMap.put(GameType.FREECELL, new GameEndedCondition() {

			@Override
			public boolean hasWon(Battlefield battlefield) {
				CardStackType[] stackers = new CardStackType[] { CardStackType.STACKER_1, CardStackType.STACKER_2,
						CardStackType.STACKER_3, CardStackType.STACKER_4 };

				for (CardStackType type : stackers) {
					if (battlefield.getStack(type).size() < 0+13) {
						return false;
					}
				}

				return true;
			}

			@Override
			public boolean hasLost(Battlefield battlefield) {
				return AIController.getAllPossibleMoves(battlefield).size() == 0;
			}

		});
	}

	private void initAggroConditions() {
		conditionMap.put(GameType.AGGRO_PATIENCE, new GameEndedCondition() {

			@Override
			public boolean hasLost(Battlefield battlefield) {
				if(battlefield.getCurrentPlayer().getType() != PlayerType.HUMAN)
				{
					return zankStacksEmpty(battlefield);
				} else {
					return false;
				}
			}

			@Override
			public boolean hasWon(Battlefield battlefield) {
				if(battlefield.getCurrentPlayer().getType() == PlayerType.HUMAN)
				{
					return zankStacksEmpty(battlefield);
				} else {
					return false;
				}
			}
		});

	}
}
