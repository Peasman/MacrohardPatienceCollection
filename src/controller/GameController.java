package controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;

import model.Battlefield;
import model.GameCard;
import model.CardStack;
import model.CardStackType;
import model.GameType;
import model.Player;
import util.BattlefieldFactory;

/**
 * Klasse zum Anlegen neuer Battlefields
 * 
 * @author Simon Kurz
 */
public class GameController {
	private static GameController instance;

	private static EnumMap<GameType, ArrayList<CardStackType>> gameStackMapper = new EnumMap<>(GameType.class);

	/**
	 * Gibt die Instanz des GameControllers zurück
	 * 
	 * @return Die Instanz
	 */
	public static GameController getInstance() {
		if (instance == null) {
			instance = new GameController();
		}
		return instance;
	}

	private GameController() {
		ArrayList<CardStackType> idiotPatienceStackList = new ArrayList<>(
				Arrays.asList(new CardStackType[] { CardStackType.STACKER_1, CardStackType.TALON, CardStackType.ROW_1,
						CardStackType.ROW_2, CardStackType.ROW_3, CardStackType.ROW_4 }));

		ArrayList<CardStackType> freecellStackList = new ArrayList<>(Arrays.asList(new CardStackType[] {
				CardStackType.FREECELL_1, CardStackType.FREECELL_2, CardStackType.FREECELL_3, CardStackType.FREECELL_4,
				CardStackType.STACKER_1, CardStackType.STACKER_2, CardStackType.STACKER_3, CardStackType.STACKER_4,
				CardStackType.ROW_1, CardStackType.ROW_2, CardStackType.ROW_3, CardStackType.ROW_4, CardStackType.ROW_5,
				CardStackType.ROW_6, CardStackType.ROW_7, CardStackType.ROW_8 }));

		ArrayList<CardStackType> zankPatienceStackList = new ArrayList<>(Arrays.asList(new CardStackType[] {
				CardStackType.STACKER_1, CardStackType.STACKER_2, CardStackType.STACKER_3, CardStackType.STACKER_4,
				CardStackType.STACKER_5, CardStackType.STACKER_6, CardStackType.STACKER_7, CardStackType.STACKER_8,
				CardStackType.ROW_1, CardStackType.ROW_2, CardStackType.ROW_3, CardStackType.ROW_4, CardStackType.ROW_5,
				CardStackType.ROW_6, CardStackType.ROW_7, CardStackType.ROW_8, CardStackType.ZANK_LEFT_1,
				CardStackType.ZANK_LEFT_2, CardStackType.ZANK_MIDDLE_1, CardStackType.ZANK_MIDDLE_2,
				CardStackType.ZANK_RIGHT_1, CardStackType.ZANK_RIGHT_2 }));

		gameStackMapper.put(GameType.IDIOT_PATIENCE, idiotPatienceStackList);
		gameStackMapper.put(GameType.FREECELL, freecellStackList);
		gameStackMapper.put(GameType.AGGRO_PATIENCE, zankPatienceStackList);
	}

	/**
	 * Initialisiert ein neues Battlefield für den übergebenen GameType und Spieler
	 * 
	 * @param gameType
	 *            Der GameType von dem das Battlefield sein soll
	 * @param playerOne
	 *            Spieler 1
	 * @param playerTwo
	 *            Spieler 2
	 * @return Das erzeugt Battlefield
	 */
	public Battlefield initBattlefield(GameType gameType, Player playerOne, Player playerTwo) {
		return initBattlefield(gameType, new InitBattlefieldParameter(playerOne, playerTwo, null, null));
	}
	public static class InitBattlefieldParameter {
		public Player playerOne;
		public Player playerTwo;
		public CardStack loadedStack;
		public CardStack secondStack;

		public InitBattlefieldParameter(Player playerOne, Player playerTwo, CardStack loadedStack,
				CardStack secondStack) {
			this.playerOne = playerOne;
			this.playerTwo = playerTwo;
			this.loadedStack = loadedStack;
			this.secondStack = secondStack;
		}
	}


	/**
	 * Initialisiert ein neues Battlefield für den übergebenen GameType und Spieler
	 * 
	 * @param gameType
	 *            Der GameType von dem das Battlefield sein soll
	 * @param parameterObject TODO
	 * @return Das erzeugt Battlefield
	 */
	public Battlefield initBattlefield(GameType gameType, InitBattlefieldParameter parameterObject) {
		Battlefield battlefield;

		switch (gameType) {
		case AGGRO_PATIENCE:
			battlefield = BattlefieldFactory.createAggroField(parameterObject.playerOne, parameterObject.playerTwo,parameterObject.loadedStack,parameterObject.secondStack);
			GameCard.Rank.ACE.setRankValue(1);
			break;
		case IDIOT_PATIENCE:
			battlefield = BattlefieldFactory.createIdiotField(parameterObject.playerOne,parameterObject.loadedStack);
			GameCard.Rank.ACE.setRankValue(14);
			break;
		case FREECELL:
			battlefield = BattlefieldFactory.createFreeCellField(parameterObject.playerOne,parameterObject.loadedStack);
			GameCard.Rank.ACE.setRankValue(1);
			break;
		default:
			return null;
		}
		return battlefield;
	}


	/**
	 * Gibt die alle CardStackTypes zu den GameTypes zurück
	 * @param type Der GameType zu dem die CardStackType gefragt sind
	 * @return Eine Liste aller CardStackTypes für den übergebenen GameType
	 */
	public ArrayList<CardStackType> getCardStackTypes(GameType type) {
		return gameStackMapper.get(type);
	}
}
