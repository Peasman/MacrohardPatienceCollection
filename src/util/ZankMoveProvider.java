package util;

import controller.GameController;
import controller.GameLogicController;
import model.Battlefield;
import model.GameCard;
import model.CardStackSuperType;
import model.CardStackType;
import model.GameType;
import model.GameMove;
import model.GameMove.SingleCardGameMoveParameter;

/**
 * Berechnet implizite Bewegungen auf dem Spielfeld, wie z.B. Bewegungen auf die
 * Stacker
 * 
 * @author Moritz Ludolph
 */
public class ZankMoveProvider {

	/**
	 * Gibt den nächsten Impliziten Zug Zurück
	 * @param battlefield Das Battlefield auf dem der Zug zurückgegeben werden soll
	 * @param turnId Die turnId die den Zügen zugeordnet werden soll
	 * @return Der nächste implizite Zug oder null falls es keien gibt
	 */
	public static GameMove getNextImplicitMove(Battlefield battlefield, int turnId) {
		for (CardStackType type : GameController.getInstance().getCardStackTypes(GameType.AGGRO_PATIENCE)) {
			if (battlefield.getStack(type).getOwner() != null
					&& !(battlefield.getStack(type).getOwner().equals(battlefield.getCurrentPlayer()))) {
				continue;
			}

			CardStackType[] rows = new CardStackType[] { CardStackType.STACKER_1, CardStackType.STACKER_2,
					CardStackType.STACKER_3, CardStackType.STACKER_4, CardStackType.STACKER_5, CardStackType.STACKER_6,
					CardStackType.STACKER_7, CardStackType.STACKER_8 };

			GameCard upperCard = battlefield.getUpperCard(type);
			if (upperCard == null)
				continue;

			for (CardStackType to : rows) {
				GameMove move = new GameMove(new SingleCardGameMoveParameter(type, to, upperCard), battlefield.getCurrentPlayer(), !upperCard.isFaceup(), turnId);
				if (GameLogicController.isValid(battlefield, move)) {
					return move;
				}
			}
		}
		return null;
	}

	/**
	 * Überprüft ob der mittlere Zank Stack des aktuellen Spielers leer ist
	 * @param battlefield Das Battlefield auf dem die Überprüfung stattfinden soll
	 * @return Gibt <i>true</i> zurück falls dieser leer ist, <i>false</i> sonst.
	 */
	public static boolean middleStackEmpty(Battlefield battlefield) {
		if (battlefield.getCurrentPlayer().equals(battlefield.getPlayerOne())) {
			return battlefield.getStack(CardStackType.ZANK_MIDDLE_1).size() == 0;
		} else {
			return battlefield.getStack(CardStackType.ZANK_MIDDLE_2).size() == 0;
		}
	}

	/**
	 * Gibt zurück, ob der gegebene Move den Zug beenden würde
	 * @param battlefield Das Battlefield auf dem die Überprüfung stattfinden soll
	 * @param move Der zu überprüfende Move
	 * @return Gibt <i>true</i> zurück falls der Move den Zug beendet, <i>false</i> sonst.
	 */
	public static boolean isEndTurnMove(Battlefield battlefield, GameMove move) {
		return move.getFrom().getSuperType() == CardStackSuperType.ZANK_MIDDLE
				&& move.getTo().getSuperType() == CardStackSuperType.ZANK_LEFT && battlefield.getStack(move.getFrom())
						.getOwner().equals(battlefield.getStack(move.getTo()).getOwner());
	}
}
