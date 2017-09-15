package util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;

import controller.GameController;
import functionals.MoveFromRule;
import model.Battlefield;
import model.GameCard;
import model.CardStack;
import model.CardStackSuperType;
import model.CardStackType;
import model.GameType;
import model.GameMove;
import model.Player;
import model.PlayerType;

/**
 * Der MoveFromRuleSetProvider gibt die Regeln für die verschiedenen Spielmodi
 * vor, in welchem Fall man von einem Stapel/einer Reihe abheben darf
 * 
 * @author Simon Kurz, Friedemann Runte
 *
 */
public class MoveFromRuleSetProvider implements Serializable {
	private static final long serialVersionUID = -8842253542747609017L;

	private EnumMap<GameType, EnumMap<CardStackSuperType, MoveFromRule>> ruleMap;

	private final MoveFromRule ALWAYS_FALSE_RULE = (Battlefield battlefield, GameMove move) -> false;
	private final MoveFromRule ALWAY_TRUE_RULE = (Battlefield battlefield, GameMove move) -> true;

	private static MoveFromRuleSetProvider instance;

	/**
	 * Gibt die Instanz des MoveFromRuleSetProviders zurück
	 * 
	 * @return Die Instanz
	 */
	public static MoveFromRuleSetProvider getInstance() {
		if (instance == null) {
			instance = new MoveFromRuleSetProvider();
		}
		return instance;
	}

	/**
	 * Der Konstruktor initialisiert alle MoveFromRules und erzeugt die
	 * generelle RuleMap
	 * 
	 * @param mpcController
	 *            Der MPCController als Mittelpunkt des Programms
	 */
	private MoveFromRuleSetProvider() {
		ruleMap = new EnumMap<>(GameType.class);

		initFreecellMoveFromRules();
		initIdiotPatienceMoveFromRules();
		initZankPatienceMoveFromRules();
	}

	/**
	 * Gibt die EnumMap für CardStackSuperType -> MoveFromRule für den
	 * übergebenen GameType zurück
	 * 
	 * @param gameType
	 *            Der GameType für den die Regeln zurück gegeben werden sollen-
	 * @return EnumMap<CardStackSuperType,MoveFromRule> Die EnumMap die für
	 *         jeden StackType eine Regel besitzt
	 * 
	 * @see CardStackSuperType, MoveFromRule, GameType
	 */
	public EnumMap<CardStackSuperType, MoveFromRule> getMoveFromRuleSet(GameType gameType) {
		return ruleMap.get(gameType);
	}

	/**
	 * Initialisiert die MoveFromRules für den Freecell SpielTyp
	 * 
	 * @see MoveFromRule, GameType
	 */
	private void initFreecellMoveFromRules() {
		EnumMap<CardStackSuperType, MoveFromRule> freecellRuleMap = new EnumMap<>(CardStackSuperType.class);

		freecellRuleMap.put(CardStackSuperType.ROW, (Battlefield battlefield, GameMove move) -> {
			ArrayList<GameCard> toMove = move.getCards();
			int movableCards = 1;
			for (CardStackType type : GameController.getInstance().getCardStackTypes(GameType.FREECELL)) {
				if (type.getSuperType() == CardStackSuperType.ROW
						|| type.getSuperType() == CardStackSuperType.FREECELL) {
					if (battlefield.getStack(type).size() == 0) {
						movableCards++;
					}
				}

			}
			if (toMove.size() > movableCards) {
				return false;
			}
			for (int i = 1; i < toMove.size(); i++) {
				if (toMove.get(i).sameColorAs(toMove.get(i - 1))
						|| toMove.get(i - 1).getRank().getRankValue() - toMove.get(i).getRank().getRankValue() != 1) {
					return false;
				}
			}

			return true;
		});

		freecellRuleMap.put(CardStackSuperType.FREECELL, ALWAY_TRUE_RULE);

		freecellRuleMap.put(CardStackSuperType.STACKER, ALWAYS_FALSE_RULE);
		freecellRuleMap.put(CardStackSuperType.TALON, ALWAYS_FALSE_RULE);
		freecellRuleMap.put(CardStackSuperType.ZANK_LEFT, ALWAYS_FALSE_RULE);
		freecellRuleMap.put(CardStackSuperType.ZANK_MIDDLE, ALWAYS_FALSE_RULE);
		freecellRuleMap.put(CardStackSuperType.ZANK_RIGHT, ALWAYS_FALSE_RULE);

		ruleMap.put(GameType.FREECELL, freecellRuleMap);
	}

	/**
	 * Initialisiert die MoveFromRules für den Idiot-Patience SpielTyp
	 * 
	 * @see MoveFromRule, GameType
	 */
	private void initIdiotPatienceMoveFromRules() {
		EnumMap<CardStackSuperType, MoveFromRule> idiotPatienceRuleMap = new EnumMap<>(CardStackSuperType.class);
		final int ONE = 1;
		idiotPatienceRuleMap.put(CardStackSuperType.ROW, new MoveFromRule() {

			@Override
			public boolean isValid(Battlefield battlefield, GameMove move) {
				ArrayList<GameCard> toMove = move.getCards();

				if (toMove.size() == ONE) {
					return true;
				}

				return false;
			}
		});

		idiotPatienceRuleMap.put(CardStackSuperType.STACKER, ALWAYS_FALSE_RULE);
		idiotPatienceRuleMap.put(CardStackSuperType.FREECELL, ALWAYS_FALSE_RULE);
		idiotPatienceRuleMap.put(CardStackSuperType.TALON, ALWAYS_FALSE_RULE);
		idiotPatienceRuleMap.put(CardStackSuperType.ZANK_LEFT, ALWAYS_FALSE_RULE);
		idiotPatienceRuleMap.put(CardStackSuperType.ZANK_MIDDLE, ALWAYS_FALSE_RULE);
		idiotPatienceRuleMap.put(CardStackSuperType.ZANK_RIGHT, ALWAYS_FALSE_RULE);

		ruleMap.put(GameType.IDIOT_PATIENCE, idiotPatienceRuleMap);
	}

	/**
	 * Initialisiert die ALWAYS_FALSE_RULE'S für den Idiot-Patience SpielTyp und
	 * ruf initZankPatienceDefinedRules auf.
	 * 
	 * @see MoveFromRule, GameType
	 */
	private void initZankPatienceMoveFromRules() {
		EnumMap<CardStackSuperType, MoveFromRule> zankPatienceRuleMap = new EnumMap<>(CardStackSuperType.class);

		initZankPatienceDefinedRules(zankPatienceRuleMap);

		zankPatienceRuleMap.put(CardStackSuperType.STACKER, ALWAYS_FALSE_RULE);
		zankPatienceRuleMap.put(CardStackSuperType.FREECELL, ALWAYS_FALSE_RULE);
		zankPatienceRuleMap.put(CardStackSuperType.TALON, ALWAYS_FALSE_RULE);

		ruleMap.put(GameType.AGGRO_PATIENCE, zankPatienceRuleMap);
	}

	/**
	 * Initialisiert alle speziellen MoveFromRules
	 * 
	 * @param zankPatienceRuleMap
	 *            Die EnumMap, in die die Regeln eingesetzt werden.
	 */
	private void initZankPatienceDefinedRules(EnumMap<CardStackSuperType, MoveFromRule> zankPatienceRuleMap) {
		zankPatienceRuleMap.put(CardStackSuperType.ROW, new MoveFromRule() {

			@Override
			public boolean isValid(Battlefield battlefield, GameMove move) {
				return move.getPlayer().getType() != PlayerType.HUMAN_ONLINE && move.getCards().size() == 1
						&& move.getPlayer().equals(battlefield.getCurrentPlayer());
			}
		});
		zankPatienceRuleMap.put(CardStackSuperType.ZANK_LEFT, new MoveFromRule() {

			@Override
			public boolean isValid(Battlefield battlefield, GameMove move) {
				return move.getPlayer().getType() != PlayerType.HUMAN_ONLINE && move.getCards().size() == 1
						&& move.getPlayer().equals(battlefield.getCurrentPlayer())
						&& isOwner(battlefield, move.getPlayer(), move.getFrom());
			}
		});

		zankPatienceRuleMap.put(CardStackSuperType.ZANK_MIDDLE, new MoveFromRule() {

			@Override
			public boolean isValid(Battlefield battlefield, GameMove move) {
				return move.getPlayer().getType() != PlayerType.HUMAN_ONLINE && move.getCards().size() == 1
						&& move.getPlayer().equals(battlefield.getCurrentPlayer())
						&& isOwner(battlefield, move.getPlayer(), move.getFrom())
						&& isMiddleStackOpen(battlefield, move.getPlayer());
			}
		});

		zankPatienceRuleMap.put(CardStackSuperType.ZANK_RIGHT, new MoveFromRule() {

			@Override
			public boolean isValid(Battlefield battlefield, GameMove move) {
				return move.getPlayer().getType() != PlayerType.HUMAN_ONLINE && move.getCards().size() == 1
						&& move.getPlayer().equals(battlefield.getCurrentPlayer())
						&& isOwner(battlefield, move.getPlayer(), move.getFrom());
			}
		});
	}

	/**
	 * Gibt <i> true </i> zurück, wenn die oberste Karte vom Mittleren Stack des
	 * übergebenen Spielers aufgedeckt ist.
	 * 
	 * @param battlefield
	 *            Das Spielfeld auf dem der Stack liegt.
	 * @param player
	 *            Der Spieler, Stack überpüft werden soll.
	 * @return <i> true </i> falls die Karte aufgedeckt ist <i> false </i> falls
	 *         die Karte zu ist.
	 */
	private boolean isMiddleStackOpen(Battlefield battlefield, Player player) {
		CardStack middle = battlefield.getStack(CardStackType.ZANK_MIDDLE_1);

		if (middle.getOwner().equals(player)) {
			if (middle.peek() == null)
				return false;
			else
				return middle.peek().isFaceup();
		}

		middle = battlefield.getStack(CardStackType.ZANK_MIDDLE_2);
		if (middle.peek() == null)
			return false;
		else
			return middle.peek().isFaceup();
	}

	/**
	 * Gibt zurück, ob der Spieler der Besitzer des Stacks ist
	 * 
	 * @param battlefield
	 *            Das Spielfeld
	 * @param player
	 *            Der Spieler
	 * @param stackType
	 *            Der zu überprüfende Stack
	 * @return <true> wenn der Stack dem Spieler gehört <false> wenn der Stack
	 *         dem Spieler nicht gehört
	 */
	private boolean isOwner(Battlefield battlefield, Player player, CardStackType stackType) {
		Player stackOwner = battlefield.getStack(stackType).getOwner();

		if (stackOwner != null && stackOwner.equals(player)) {
			return true;
		}

		return false;
	}

}
