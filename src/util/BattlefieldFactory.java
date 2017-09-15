package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;

import controller.GameController;
import model.Battlefield;
import model.GameCard;
import model.GameCard.Suit;
import model.CardStack;
import model.CardStackSuperType;
import model.CardStackType;
import model.GameType;
import model.Player;

/**
 * Dient zur Erstellung von Battlefields für einen übergebenen GameType
 * 
 * @author Simon Kurz, Moritz Ludolph
 */
public class BattlefieldFactory {

	/**
	 * Erstellt ein neues Battlefield für Idioten-Patience
	 * 
	 * @param playerOne
	 *            Der Spieler
	 * @return Ein neues Battlefield für Idioten-Patience
	 */
	public static Battlefield createIdiotField(Player playerOne, CardStack loadedStack) {
		EnumMap<CardStackType, CardStack> stackMap = new EnumMap<>(CardStackType.class);
		ArrayList<GameCard> talon = new ArrayList<GameCard>();
		if (loadedStack != null) {
			talon = loadedStack.toList();
		} else {
			talon = CardStack.createDefaultDeck(true).toList();

		}
		for (GameCard c : talon) {
			c.setFaceup(false);
		}

		for (CardStackType type : GameController.getInstance().getCardStackTypes(GameType.IDIOT_PATIENCE)) {
			stackMap.put(type, new CardStack(type));
		}

		stackMap.get(CardStackType.TALON).pushAll(talon);

		Battlefield battlefield = new Battlefield(GameType.IDIOT_PATIENCE, stackMap, playerOne, null);

		return battlefield;
	}

	/**
	 * Erstellt ein neues Battlefield für FreeCell
	 * 
	 * @param playerOne
	 *            Der Spieler
	 * @return Ein Battlefield für den GameType Freecell
	 */
	public static Battlefield createFreeCellField(Player playerOne, CardStack loadedStack) {
		EnumMap<CardStackType, CardStack> stackMap = new EnumMap<>(CardStackType.class);
		ArrayList<GameCard> talon = new ArrayList<GameCard>();
		if (loadedStack != null) {
			talon = loadedStack.toList();
			Collections.reverse(talon);
		} else {
			talon = CardStack.createDefaultDeck(true).toList();

		}
		for (CardStackType type : GameController.getInstance().getCardStackTypes(GameType.FREECELL)) {
			CardStack stack = assignSuitToStack(talon, type);

			stackMap.put(type, stack);
		}

		Battlefield battlefield = new Battlefield(GameType.FREECELL, stackMap, playerOne, null);

		return battlefield;
	}

	/**
	 * Erstellt ein neues Battlefield für Zank-Patience
	 * 
	 * @param playerOne
	 *            Spieler 1
	 * @param playerTwo
	 *            Spieler 2
	 * @return Ein neues Battlefield für Zank
	 */
	public static Battlefield createAggroField(Player playerOne, Player playerTwo, CardStack playerOneStack,
			CardStack playerTwoStack) {
		EnumMap<CardStackType, CardStack> stackMap = new EnumMap<>(CardStackType.class);
		ArrayList<GameCard> talonOneCards = CardStack.createDefaultDeck(true, false).toList();
		ArrayList<GameCard> talonTwoCards = CardStack.createDefaultDeck(true, true).toList();
		if (playerOneStack != null) {
			talonOneCards = playerOneStack.toList();
			Collections.reverse(talonOneCards);
		}
		if (playerTwoStack != null) {
			talonTwoCards = playerTwoStack.toList();
			Collections.reverse(talonTwoCards);

		}
		setFaceDown(talonOneCards);
		setFaceDown(talonTwoCards);

		for (CardStackType type : GameController.getInstance().getCardStackTypes(GameType.AGGRO_PATIENCE)) {
			CardStack stack = assignZankStackToSuit(playerOne, playerTwo, type);

			stackMap.put(type, stack);
		}

		int size = talonOneCards.size();

		initRightZankStacks(stackMap, talonOneCards, talonTwoCards, size);
		stackMap.get(CardStackType.ZANK_RIGHT_1).peek().setFaceup(true);
		stackMap.get(CardStackType.ZANK_RIGHT_2).peek().setFaceup(true);

		pushAggroCardsToStacks(stackMap, talonOneCards, talonTwoCards);

		return new Battlefield(GameType.AGGRO_PATIENCE, stackMap, playerOne, playerTwo);
	}

	private static void pushAggroCardsToStacks(EnumMap<CardStackType, CardStack> stackMap,
			ArrayList<GameCard> talonOneCards, ArrayList<GameCard> talonTwoCards) {
		CardStackType[] rows14 = new CardStackType[] { CardStackType.ROW_4, CardStackType.ROW_3, CardStackType.ROW_2,
				CardStackType.ROW_1 };
		CardStackType[] rows58 = new CardStackType[] { CardStackType.ROW_5, CardStackType.ROW_6, CardStackType.ROW_7,
				CardStackType.ROW_8 };

		for (int i = 0; i < 4; i++) {
			stackMap.get(rows58[i]).push(talonOneCards.remove(38 - i));
			stackMap.get(rows58[i]).peek().setFaceup(true);

			stackMap.get(rows14[i]).push(talonTwoCards.remove(38 - i));
			stackMap.get(rows14[i]).peek().setFaceup(true);
		}
		stackMap.get(CardStackType.ZANK_MIDDLE_1).pushAll(talonOneCards);
		stackMap.get(CardStackType.ZANK_MIDDLE_2).pushAll(talonTwoCards);
	}

	private static void initRightZankStacks(EnumMap<CardStackType, CardStack> stackMap, ArrayList<GameCard> talonOneCards,
			ArrayList<GameCard> talonTwoCards, int size) {
		for (int i = size - 1; i >= size - 13; i--) {
			GameCard card = talonTwoCards.remove(i);
			card.setFaceup(false);
			stackMap.get(CardStackType.ZANK_RIGHT_1).push(card);
			card = talonOneCards.remove(i);
			card.setFaceup(false);
			stackMap.get(CardStackType.ZANK_RIGHT_2).push(card);
		}
	}

	private static CardStack assignZankStackToSuit(Player playerOne, Player playerTwo, CardStackType type) {
		CardStack stack;

		switch (type) {
		case ZANK_LEFT_1:
		case ZANK_MIDDLE_1:
		case ZANK_RIGHT_1:
			stack = new CardStack(type, playerOne);
			break;
		case ZANK_LEFT_2:
		case ZANK_MIDDLE_2:
		case ZANK_RIGHT_2:
			stack = new CardStack(type, playerTwo);
			break;
		case STACKER_1:
		case STACKER_5:
			stack = new CardStack(type, Suit.DIAMONDS);
			break;
		case STACKER_2:
		case STACKER_6:
			stack = new CardStack(type, Suit.SPADES);
			break;
		case STACKER_3:
		case STACKER_7:
			stack = new CardStack(type, Suit.CLUBS);
			break;
		case STACKER_4:
		case STACKER_8:
			stack = new CardStack(type, Suit.HEARTS);
			break;
		default:
			stack = new CardStack(type);
		}
		return stack;
	}

	private static CardStack assignSuitToStack(ArrayList<GameCard> talon, CardStackType type) {
		CardStack stack;

		if (type.getSuperType() == CardStackSuperType.ROW) {
			stack = new CardStack(type);

			for (int i = type.getNum() - 1; i < talon.size(); i += 8) {
				stack.push(talon.get(i));
			}
		} else {
			switch (type) {
			case STACKER_1:
				stack = new CardStack(type, GameCard.Suit.SPADES);
				break;
			case STACKER_2:
				stack = new CardStack(type, GameCard.Suit.HEARTS);
				break;
			case STACKER_3:
				stack = new CardStack(type, GameCard.Suit.CLUBS);
				break;
			case STACKER_4:
				stack = new CardStack(type, GameCard.Suit.DIAMONDS);
				break;
			default:
				stack = new CardStack(type);
			}
		}
		return stack;
	}

	private static void setFaceDown(ArrayList<GameCard> cardsToFlip) {
		for (GameCard card : cardsToFlip) {
			card.setFaceup(false);
		}
	}
}
