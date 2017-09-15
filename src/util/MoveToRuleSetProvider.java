package util;

import java.io.Serializable;
import java.util.EnumMap;

import functionals.MoveToRule;
import model.CardStackSuperType;
import model.GameType;

/**
 * /**
 * Die Klasse verwaltet die EnumMap zum abbilden von GameType -> EnumMap<CardStackSuperType, MoveToRule>>
 * @author Simon Kurz , Moritz Ludolph, Friedemann Runte.
 */
public class MoveToRuleSetProvider extends MoveToRuleCollection implements Serializable {
	private static final long serialVersionUID = -8080152129848494180L;

	private EnumMap<GameType, EnumMap<CardStackSuperType, MoveToRule>> ruleMap;

	private static MoveToRuleSetProvider instance;
	
	/**
	 * Gibt die Instanz des MoveToRuleSetProviders zurück
	 * @return Die Instanz
	 */
	public static MoveToRuleSetProvider getInstance(){
		if (instance == null)
			instance = new MoveToRuleSetProvider();
		return instance;
	}
	

	/**
	 * Initilialisiert die EnumMap für alle Regeln für die Spiele.
	 * @param mpcController
	 */
	private MoveToRuleSetProvider() {
		ruleMap = new EnumMap<>(GameType.class);

		initFreecellMoveToRules();
		initIdiotPatienceMoveToRules();
		initZankPatienceMoveToRules();
	}

	/**
	 * Gibt die Regeln für den eingegebenen GameType zurück.
	 * @param gameType
	 * @return EnumMap<CardStackSuperType, MoveToRule>
	 */
	public EnumMap<CardStackSuperType, MoveToRule> getMoveToRuleSet(GameType gameType) {
		return ruleMap.get(gameType);
	}

	/**
	 * Fügt alle Idiot-Patience-Rules zur RuleMap hinzu.
	 */
	private void initIdiotPatienceMoveToRules() {
		EnumMap<CardStackSuperType, MoveToRule> idiotPatienceRuleMap = new EnumMap<>(CardStackSuperType.class);

		idiotPatienceRuleMap.put(CardStackSuperType.ROW, IDIOT_ROW_RULE);

		idiotPatienceRuleMap.put(CardStackSuperType.STACKER,IDIOT_STACKER_RULE);
		
		idiotPatienceRuleMap.put(CardStackSuperType.TALON, ALWAYS_FALSE_RULE);
		idiotPatienceRuleMap.put(CardStackSuperType.FREECELL, ALWAYS_FALSE_RULE);
		idiotPatienceRuleMap.put(CardStackSuperType.ZANK_LEFT, ALWAYS_FALSE_RULE);
		idiotPatienceRuleMap.put(CardStackSuperType.ZANK_MIDDLE, ALWAYS_FALSE_RULE);
		idiotPatienceRuleMap.put(CardStackSuperType.ZANK_RIGHT, ALWAYS_FALSE_RULE);

		ruleMap.put(GameType.IDIOT_PATIENCE, idiotPatienceRuleMap);
	}

	/**
	 * Fügt alle Freecell-Rules zur RuleMap hinzu.
	 */
	private void initFreecellMoveToRules() {
		EnumMap<CardStackSuperType, MoveToRule> freecellRuleMap = new EnumMap<>(CardStackSuperType.class);

		freecellRuleMap.put(CardStackSuperType.FREECELL, FREECELL_RULE);
		freecellRuleMap.put(CardStackSuperType.ROW, FREECELL_ROW_RULE);
		freecellRuleMap.put(CardStackSuperType.STACKER, FREECELL_STACKER_RULE);
		freecellRuleMap.put(CardStackSuperType.TALON, ALWAYS_FALSE_RULE);
		freecellRuleMap.put(CardStackSuperType.ZANK_LEFT, ALWAYS_FALSE_RULE);
		freecellRuleMap.put(CardStackSuperType.ZANK_MIDDLE, ALWAYS_FALSE_RULE);
		freecellRuleMap.put(CardStackSuperType.ZANK_RIGHT, ALWAYS_FALSE_RULE);

		ruleMap.put(GameType.FREECELL, freecellRuleMap);
	}



	/**
	 * Fügt alle Zank-Rules zur RuleMap hinzu.
	 */
	private void initZankPatienceMoveToRules() {
		EnumMap<CardStackSuperType, MoveToRule> zankPatienceRuleMap = new EnumMap<>(CardStackSuperType.class);

		zankPatienceRuleMap.put(CardStackSuperType.ZANK_LEFT, ZANK_LEFT_RULE);
		zankPatienceRuleMap.put(CardStackSuperType.ZANK_RIGHT, ZANK_RIGHT_RULE);
		zankPatienceRuleMap.put(CardStackSuperType.ROW, ZANK_ROW_RULE);
		zankPatienceRuleMap.put(CardStackSuperType.STACKER, ZANK_STACKER_RULE);
		zankPatienceRuleMap.put(CardStackSuperType.ZANK_MIDDLE, ALWAYS_FALSE_RULE);
		zankPatienceRuleMap.put(CardStackSuperType.TALON, ALWAYS_FALSE_RULE);
		zankPatienceRuleMap.put(CardStackSuperType.FREECELL, ALWAYS_FALSE_RULE);

		ruleMap.put(GameType.AGGRO_PATIENCE, zankPatienceRuleMap);
	}
}
