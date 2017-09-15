package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;

import ai.general.GameState;
import ai.general.IMove;
import exceptions.InconsistentMoveException;

/**
 * Container zum Auslagern von Methoden vom SuperBattlefield.
 * 
 * @author Friedemann Runte
 *
 */
public class SuperBattlefield implements Cloneable, Serializable, GameState {

	/**
	 * 
	 */
	protected static final long serialVersionUID = -8361922065598656606L;
	
	/**
	 * alle zuvor gemachten moves
	 */
	protected ArrayList<GameMove> moveHistory;
	/**
	 * die EnumMap die den benutzten CardStackTypes genau einen Kartenstapel
	 * zuordnet
	 */
	protected EnumMap<CardStackType, CardStack> stackMap;

	/**
	 * die teilnehmenden Spieler
	 */
	protected Player playerOne, playerTwo;
	/**
	 * der Spieler, der momentan an der Reihe ist
	 */
	protected Player currentPlayer;

	/**
	 * tauscht die beiden Spieler, also ihre Stapel.
	 */
	public void swapPlayers() {
		for (CardStack stack : stackMap.values()) {
			if (stack.getOwner() != null && stack.getOwner().equals(playerOne)) {
				stack.setOwner(playerTwo);
			} else {
				stack.setOwner(playerOne);
			}
		}
		Player temp = playerOne;
		playerOne = playerTwo;
		playerTwo = temp;
		if (currentPlayer != null && currentPlayer.equals(playerOne)) {
			currentPlayer = playerTwo;
		} else {
			currentPlayer = playerOne;
		}
		String newPlayerTwoName = playerOne.getName();
		playerOne.setName(playerTwo.getName());
		playerTwo.setName(newPlayerTwoName);
	}

	/**
	 * berechnet die Anzahl an meisten Zügen hintereinander.
	 * 
	 * @return Zuganzahl
	 */
	protected double calcMoves() {
		double longestTurn = 0;
		double longestTurnCandidate = 0;
		for (GameMove moveToCheck : moveHistory) {
			if (moveToCheck.getPlayer().getType().equals(PlayerType.HUMAN) && moveToCheck.getFrom() != moveToCheck.getTo()) {
				CardStackSuperType stackFromToCheck = moveToCheck.getFrom().getSuperType();
				if (stackFromToCheck.equals(CardStackSuperType.ZANK_MIDDLE)
						|| stackFromToCheck.equals(CardStackSuperType.ZANK_LEFT)
						|| stackFromToCheck.equals(CardStackSuperType.ZANK_RIGHT)) {
					longestTurnCandidate++;
				}
				if (longestTurnCandidate > longestTurn) {
					longestTurn = longestTurnCandidate;
				}
			} else {
				longestTurnCandidate = 0;
			}
		}
		return longestTurn;
	}

	/**
	 * Führt einen move aus. Verschiebt die Karte/n und fügt den move zur
	 * history hinzu.
	 * 
	 * @param move
	 *            der auszuführende move
	 * @throws InconsistentMoveException
	 *             ein internes Problem ist aufgetreten, die entfernte Karte
	 *             entspricht nicht der hinzugefügten Karte
	 */
	public void executeMove(GameMove move) throws InconsistentMoveException {
		ArrayList<GameCard> fromCards = stackMap.get(move.getFrom()).toList();
		int index = fromCards.size() - move.getCards().size();
		for (GameCard cardToCheck : move.getCards()) {
			if (!cardToCheck.sameCardAs(fromCards.get(index))) {
				throw new InconsistentMoveException();
			}
			index++;
		}
		ArrayList<GameCard> poppedCards = new ArrayList<>();
		for (index = 0; index < move.getCards().size(); index++) {
			GameCard poppedCard = popCard(move.getFrom());
			if (move.getFlip())
				poppedCard.setFaceup(!poppedCard.isFaceup());
			poppedCards.add(poppedCard);
		}
		Collections.reverse(poppedCards);
		pushCard(move.getTo(), poppedCards);
		// TODO: weg
		// printHistory();
	}

	/**
	 * fügt Karten auf einen Stapel hinzu
	 * 
	 * @param type
	 *            der Kartenstapel
	 * @param cards
	 *            die Karten, welche hinzugefügt werden sollen
	 */
	public void pushCard(CardStackType type, ArrayList<GameCard> cards) {
		stackMap.get(type).pushAll(cards);
	}

	/**
	 * entfernt die oberste Karte eines Stapels
	 * 
	 * @param type
	 *            der Stapel von dem entfernt werden soll
	 * @return die oberste Karte
	 */
	public GameCard popCard(CardStackType type) {
		GameCard card = stackMap.get(type).pop();
		return card;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see search.GameState#executeMove(search.IMove)
	 */
	@Override
	public void executeMove(IMove iMove) throws InconsistentMoveException {
		for (GameMove move : iMove.getMoves()) {
			executeMove(move);
		}
	}

}