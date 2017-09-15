package controller;

import java.util.ArrayList;

import ai.zankpatience.ZankPatienceAI;
import exceptions.InconsistentMoveException;
import gui.BattlefieldAUI;
import gui.MoveAUI;
import model.Battlefield;
import model.GameCard;
import model.CardStack;
import model.CardStackType;
import model.GameType;
import model.GameMove;
import model.GameMove.SingleCardGameMoveParameter;
import model.Player;
import model.PlayerType;
import util.MoveFromRuleSetProvider;
import util.MoveToRuleSetProvider;
import util.WinConditionProvider;
import util.ZankMoveProvider;

public abstract class  SuperGameLogicController {
	protected MPCController mpcController;

	protected static MoveFromRuleSetProvider moveFromRuleSetProvider = MoveFromRuleSetProvider.getInstance();;
	protected static MoveToRuleSetProvider moveToRuleSetProvider = MoveToRuleSetProvider.getInstance();
	protected static WinConditionProvider winConditionProvider = WinConditionProvider.getInstance();

	protected ArrayList<MoveAUI> moveAUIs;
	protected ArrayList<BattlefieldAUI> battlefieldAUIs;
	protected ZankPatienceAI[] zankAIs;

	/**
	 * Statische Methode zur Überprüfung, ob ein Move Spielregelkonform ist
	 * 
	 * @param battlefield
	 *            Das Battlefield auf dem der Move ausgeführt werden soll
	 * @param move
	 *            Der zu überprüfende Move
	 * @return Gibt <i>true</i> zurück, falls der Zug gültig ist, <i>false</i>
	 *         sonst.
	 */

	/**
	 * Führt implizite Bewegungen auf dem Spielfeld aus
	 * 
	 * @throws InconsistentMoveException
	 *             Falls ein Move inkonsistent zum Inhalt des Spielfeldes ist
	 */
	protected void performImplicitMoves(int turnId) throws InconsistentMoveException {
		Battlefield battlefield = mpcController.getBattlefield();
		for (GameMove move = ZankMoveProvider.getNextImplicitMove(battlefield, turnId); move != null
				&& !battlefield.isFinished(); move = ZankMoveProvider.getNextImplicitMove(battlefield, turnId)) {
			executeMove(move, true, true, true);
		}
	}

	/**
	 * Dreht für den aktuellen Spieler aller Karten korrekt um
	 * 
	 * @param turnId
	 * @throws InconsistentMoveException
	 */
	public void performCardFlips(int turnId) throws InconsistentMoveException {
		Battlefield battlefield = mpcController.getBattlefield();
		CardStackType[] types;
		if (battlefield.getCurrentPlayer().equals(battlefield.getPlayerOne())) {
			types = new CardStackType[] { CardStackType.ZANK_MIDDLE_1, CardStackType.ZANK_RIGHT_1 };
		} else {
			types = new CardStackType[] { CardStackType.ZANK_MIDDLE_2, CardStackType.ZANK_RIGHT_2 };
		}

		for (CardStackType type : types) {
			GameCard upperCard = battlefield.getUpperCard(type);
			if (upperCard != null && upperCard.isFaceup() == false) {
				GameMove flipCard = new GameMove(new SingleCardGameMoveParameter(type, type, upperCard), battlefield.getCurrentPlayer(), true, turnId);
				executeMove(flipCard, true, false, true);
			}
		}
	}

	/**
	 * Dummy Methode für execute Move hat keinen Nutzen
	 * @param move
	 * @param addToHistory
	 * @param isImplicit
	 * @param afterMoveRoutine
	 * @throws InconsistentMoveException
	 */
	protected abstract void executeMove(GameMove move, boolean addToHistory, boolean isImplicit, boolean afterMoveRoutine)
			throws InconsistentMoveException;

	protected void setTurnForPlayer(Player player) throws InconsistentMoveException {
		Battlefield battlefield = mpcController.getBattlefield();
		callOnTurnEnd(battlefield.getCurrentPlayer());
		battlefield.setCurrentPlayer(player);
		callOnTurnBegin(player);
	}

	/**
	 * Bewegt alle Karten vom linken Zank Stapel auf die Mitte falls dieser leer
	 * ist
	 * 
	 * @param fromStack
	 *            Der linke Zank Stack
	 * @param toStack
	 *            Der rechte Zank Stack
	 * @throws InconsistentMoveException
	 *             Falls der Move nicht konsistent zum Spielfeld ist
	 */
	public void performZankLeftStackFlip(int turnId) throws InconsistentMoveException {
		Battlefield battlefield = mpcController.getBattlefield();
		CardStack fromStack, toStack;

		if (battlefield.getCurrentPlayer().equals(battlefield.getPlayerOne())) {
			fromStack = battlefield.getStack(CardStackType.ZANK_LEFT_1);
			toStack = battlefield.getStack(CardStackType.ZANK_MIDDLE_1);
		} else {
			fromStack = battlefield.getStack(CardStackType.ZANK_LEFT_2);
			toStack = battlefield.getStack(CardStackType.ZANK_MIDDLE_2);
		}
		for (GameCard card : fromStack) {
			GameMove move = new GameMove(new SingleCardGameMoveParameter(fromStack.getType(), toStack.getType(), card), battlefield.getCurrentPlayer(), card.isFaceup(), turnId);
			executeMove(move, true, true, false);
		}

	}

	/**
	 * Ruft für alle registrierten MoveAUIs die entsprechende beforeMove Methode
	 * auf.
	 * 
	 * @param move
	 *            Der Move mit dem die AUIs aktualisiert werden sollen.
	 * @see MoveAUI
	 */
	protected void callBeforeMove(GameMove move, boolean implicit) {
		for (MoveAUI aui : moveAUIs) {
			aui.beforeMove(move, implicit);
		}
	}

	/**
	 * Ruft für alle registrierten MoveAUIs die entsprechende beforeMove Methode
	 * auf.
	 * 
	 * @param move
	 *            Der Move mit dem die AUIs aktualisiert werden sollen.
	 * @see MoveAUI
	 */
	protected void callAfterMove(GameMove move, boolean implicit) {
		for (MoveAUI aui : moveAUIs) {
			aui.afterMove(move, implicit);
		}
		if (mpcController.getServerController().isOnline() && move.getPlayer().getType() != PlayerType.HUMAN_ONLINE) {
			System.out.println("Sending: " + move.toProtocol());
			mpcController.getServerController().sendMessage(move.toProtocol());
		}
	}

	/**
	 * Ruft bei allen BattlefieldAUIs onTurnBegin auf
	 * 
	 * @param player
	 *            Der Player, dessen Zug anfängt
	 */
	protected void callOnTurnBegin(Player player) throws InconsistentMoveException {
		Battlefield battlefield = mpcController.getBattlefield();
		if (battlefield.getGameType() == GameType.AGGRO_PATIENCE) {
			int lastTurn = -1;
			if (battlefield.getMoveHistory().size() > 0)
				lastTurn = battlefield.getMoveHistory().get(battlefield.getMoveHistory().size() - 1).getTurn();

			performImplicitMoves(lastTurn);

			if (ZankMoveProvider.middleStackEmpty(battlefield)) {
				performZankLeftStackFlip(lastTurn);
			}

			performCardFlips(lastTurn);
			int kiDiff = 2;
			PlayerType pType = battlefield.getCurrentPlayer().getType();
			switch(pType) {
				case AI1:
					kiDiff = 0;
					break;
				case AI2:
					kiDiff = 1;
					break;
				case AI3:
					kiDiff = 2;
					break;
				default:
					break;
			}
			switch(pType) {
			case AI1:
			case AI2:
			case AI3:
				Thread aiThread = new Thread(zankAIs[kiDiff]::runLoop);
				aiThread.start();
				break;
			default:
				break;
			}
		}

		for (BattlefieldAUI aui : battlefieldAUIs) {
			aui.onTurnBegin(player);
		}
	}

	/**
	 * Ruft bei allen BattlefieldAUIs OnTurnEnd auf
	 * 
	 * @param player
	 *            Der Spieler, dessen Zug beendet wurde
	 */
	protected void callOnTurnEnd(Player player) {
		for (BattlefieldAUI aui : battlefieldAUIs) {
			aui.onTurnEnd(player);
		}
	}

	

}