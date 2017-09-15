package util;

import controller.GameController;
import functionals.MoveToRule;
import model.Battlefield;
import model.GameCard;
import model.GameCard.Rank;
import model.GameCard.Suit;
import model.CardStack;
import model.CardStackSuperType;
import model.CardStackType;
import model.GameType;
import model.GameMove;
import model.Player;

/**
 * Die Klasse enthält alle MoveToRules für die Spieltypen.
 * 
 * @author Friedemann Runte
 *
 */
public class MoveToRuleCollection {
	/**
	 * Eine always false rule für die MoveToRule.
	 */
	protected final MoveToRule ALWAYS_FALSE_RULE = (Battlefield battlefield, GameMove move) -> false;
	/**
	 * Überprüft, ob der Spieler auf den Linken Stapel legen kann.
	 * 
	 * @param battlefield
	 *            Das Spielfeld auf dem der Zug ausgeführt werden soll.
	 * @param move
	 *            Der Move der überprüft werden soll.
	 * @see Move, Battlefield
	 */
	protected final MoveToRule ZANK_LEFT_RULE = (Battlefield battlefield, GameMove move) -> {
		GameCard toCard = battlefield.getStack(move.getTo()).peek();
		GameCard toMove = move.getCards().get(0);
		if (toCard == null)
			return (move.getFrom().getSuperType() == CardStackSuperType.ZANK_MIDDLE
					&& isOwner(battlefield, move.getPlayer(), move.getTo()));

		return ((move.getFrom().getSuperType() == CardStackSuperType.ZANK_MIDDLE
				&& isOwner(battlefield, move.getPlayer(), move.getTo())))
				|| (!isOwner(battlefield, move.getPlayer(), move.getTo()) && toCard.getSuit().equals(toMove.getSuit())
						&& checkAbsoluteRankValue(toCard, toMove));
	};

	/**
	 * Überprüft, ob der Spieler auf den Rechten Stapel legen kann.
	 * 
	 * @param battlefield
	 *            Das Spielfeld auf dem der Zug ausgeführt werden soll.
	 * @param move
	 *            Der Move der überprüft werden soll.
	 * @see Move, Battlefield
	 */
	protected final MoveToRule ZANK_RIGHT_RULE = (Battlefield battlefield, GameMove move) -> {
		GameCard toCard = battlefield.getStack(move.getTo()).peek();
		GameCard toMove = move.getCards().get(0);

		return toCard != null && !isOwner(battlefield, move.getPlayer(), move.getTo())
				&& toCard.getSuit().equals(toMove.getSuit()) && checkAbsoluteRankValue(toCard, toMove);
	};
	/**
	 * Überprüft, ob der Spieler in die Freecell legen kann.
	 * 
	 * @param battlefield
	 *            Das Spielfeld auf dem der Zug ausgeführt werden soll.
	 * @param move
	 *            Der Move der überprüft werden soll.
	 * @see Move, Battlefield
	 */
	protected final MoveToRule FREECELL_RULE = (Battlefield battlefield, GameMove move) -> {
		if (battlefield.getStack(move.getTo()).size() == 0 && move.getCards().size() == 1) {
			return true;
		}
		return false;
	};
	/**
	 * Überprüft, ob der Spieler bei Freecell in die Kartenreihe legen kann.
	 * 
	 * @param battlefield
	 *            Das Spielfeld auf dem der Zug ausgeführt werden soll.
	 * @param move
	 *            Der Move der überprüft werden soll.
	 * @see Move, Battlefield
	 */
	protected final MoveToRule FREECELL_ROW_RULE = (Battlefield battlefield, GameMove move) -> {
		CardStack toStack = battlefield.getStack(move.getTo());
		int movableCards = 1;
		for (CardStackType type : GameController.getInstance().getCardStackTypes(GameType.FREECELL)) {
			if (type.getSuperType() == CardStackSuperType.ROW
					|| type.getSuperType() == CardStackSuperType.FREECELL) {
				if (battlefield.getStack(type).size() == 0) {
					movableCards++;
				}
			}

		}
		if(toStack.size() == 0 && move.getCards().size() >= movableCards) {
			return false;
		}
		if (toStack.size() == 0 || checkRankValues(toStack.peek(), move.getCards().get(0))
				&& !move.getCards().get(0).sameColorAs(toStack.peek())) {
			return true;
		}

		return false;
	};
	/**
	 * Überprüft, ob der Spieler bei Freecell auf die Ablegestapel legen kann.
	 * 
	 * @param battlefield
	 *            Das Spielfeld auf dem der Zug ausgeführt werden soll.
	 * @param move
	 *            Der Move der überprüft werden soll.
	 * @see Move, Battlefield
	 */
	protected final MoveToRule FREECELL_STACKER_RULE = (Battlefield battlefield, GameMove move) -> {
		if (move.getCards().size() != 0 + 1)
			return false;

		GameCard toCard = battlefield.getStack(move.getTo()).peek();
		GameCard cardToMove = move.getCards().get(0);

		int cardToMoveRank = cardToMove.getRank().getRankValue();

		Suit stackSuit = battlefield.getStack(move.getTo()).getSuit();

		if (toCard == null && cardToMoveRank == 1 && stackSuit == cardToMove.getSuit()
				|| toCard != null && checkRankValues(cardToMove, toCard) && toCard.getSuit() == cardToMove.getSuit()) {
			return true;
		}

		return false;
	};
	/**
	 * Überprüft, ob der Spieler bei Zank-Patience in die Kartenreihe legen
	 * kann.
	 * 
	 * @param battlefield
	 *            Das Spielfeld auf dem der Zug ausgeführt werden soll.
	 * @param move
	 *            Der Move der überprüft werden soll.
	 * @see Move, Battlefield
	 */
	protected final MoveToRule ZANK_ROW_RULE = (Battlefield battlefield, GameMove move) -> {
		GameCard toCard = battlefield.getStack(move.getTo()).peek();
		GameCard toMove = move.getCards().get(0);

		return (toCard == null) || (toCard != null && !toCard.sameColorAs(toMove) && checkRankValues(toCard, toMove));
	};

	/**
	 * Überprüft, ob der Spieler bei Zank-Patience auf den Ablagestapel legen
	 * kann.
	 * 
	 * @param battlefield
	 *            Das Spielfeld auf dem der Zug ausgeführt werden soll.
	 * @param move
	 *            Der Move der überprüft werden soll.
	 * @see Move, Battlefield
	 */
	protected final MoveToRule ZANK_STACKER_RULE = (Battlefield battlefield, GameMove move) -> {
		if (battlefield.getStack(move.getTo()).peek() == null)
			return battlefield.getStack(move.getTo()).getSuit() == move.getCards().get(0).getSuit()
					&& move.getCards().get(0).getRank() == Rank.ACE;

		GameCard toCard = battlefield.getStack(move.getTo()).peek();
		GameCard toMove = move.getCards().get(0);

		return (toCard.getSuit().equals(toMove.getSuit()) && checkRankValues(toMove, toCard));
	};
	/**
	 * Überprüft, ob der Spieler bei Idiot-Patience auf die Kartenreihe legen
	 * kann.
	 * 
	 * @param battlefield
	 *            Das Spielfeld auf dem der Zug ausgeführt werden soll.
	 * @param move
	 *            Der Move der überprüft werden soll.
	 * @see Move, Battlefield
	 */
	protected final MoveToRule IDIOT_ROW_RULE = (Battlefield battlefield, GameMove move) -> {
		if (move.getCards().size() == 1
				&& (battlefield.getStack(move.getTo()).size() == 0 || move.getFrom() == CardStackType.TALON)) {
			return true;
		}
		return false;
	};
	/**
	 * Überprüft, ob der Spieler bei Idiot-Patience auf den Ablagestapel legen
	 * kann.
	 * 
	 * @param battlefield
	 *            Das Spielfeld auf dem der Zug ausgeführt werden soll.
	 * @param move
	 *            Der Move der überprüft werden soll.
	 * @see Move, Battlefield
	 */
	protected final MoveToRule IDIOT_STACKER_RULE = (Battlefield battlefield, GameMove move) -> {

		if (move.getCards().size() == 0 || move.getCards().size() > 1 || move.getCards().get(0) == null)
			return false;

		GameCard moveCard = move.getCards().get(0);
		GameCard[] upperCards = new GameCard[] { battlefield.getStack(CardStackType.ROW_1).peek(),
				battlefield.getStack(CardStackType.ROW_2).peek(), battlefield.getStack(CardStackType.ROW_3).peek(),
				battlefield.getStack(CardStackType.ROW_4).peek() };

		for (GameCard card : upperCards) {
			if (card != null && card.getSuit() == moveCard.getSuit()
					&& card.getRank().getRankValue() > moveCard.getRank().getRankValue()) {
				return true;
			}
		}
		return false;

	};

	/**
	 * Überprüft ob die Werte der Karte um genau 1 unterschiedlich sind.
	 * 
	 * @param toCard
	 *            Die Karte an die angelegt werden soll.
	 * @param toMove
	 *            Die Karte die bewegt werden soll.
	 * @return <i> true </i> wenn toMove genau 1 kleiner ist als toCard <i>
	 *         false </i> wenn toMove nicht genau 1 kleiner ist
	 */
	private boolean checkRankValues(GameCard toCard, GameCard toMove) {
		return toCard.getRank().getRankValue() - toMove.getRank().getRankValue() == 1;
	}

	/**
	 * Überprüft, ob die Differenz der Werte beider Karten genau 1 ist.
	 * 
	 * @param toCard
	 *            Die Karte an die angelegt werden soll.
	 * @param toMove
	 *            Die Karte die bewegt werden soll.
	 * @return <i> true </i> wenn die differenz genau 1 ist <i> false </i> wenn
	 *         die Differenz != 1 ist
	 */
	private boolean checkAbsoluteRankValue(GameCard toCard, GameCard toMove) {
		return Math.abs(toCard.getRank().getRankValue() - toMove.getRank().getRankValue()) == 1;
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
	protected boolean isOwner(Battlefield battlefield, Player player, CardStackType stackType) {
		Player stackOwner = battlefield.getStack(stackType).getOwner();

		if (stackOwner != null && stackOwner.equals(player)) {
			return true;
		}

		return false;
	}

}