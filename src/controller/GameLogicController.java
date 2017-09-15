package controller;

import java.util.ArrayList;

import ai.zankpatience.ZankPatienceAI;
import exceptions.InconsistentMoveException;
import gui.BattlefieldAUI;
import gui.MoveAUI;
import model.Battlefield;
import model.GameType;
import model.GameMove;
import model.GameMove.GameMoveParameter;
import model.Player;
import model.PlayerType;
import util.ZankMoveProvider;

/**
 * Klasse zur Überprüfung und Ausführung der Spiellogik
 * 
 * @author Moritz Ludolph, Friedeman Runte
 */
public class GameLogicController extends SuperGameLogicController {

	/**
	 * Initialisiert einen neuen GameLogicController mit dem übergebenen
	 * MPCController
	 * 
	 * @param mpcController
	 *            Der MPCController
	 */
	public GameLogicController(MPCController mpcController) {
		moveAUIs = new ArrayList<MoveAUI>();
		battlefieldAUIs = new ArrayList<BattlefieldAUI>();
		zankAIs = new ZankPatienceAI[3];
		this.mpcController = mpcController;
	}

	/**
	 * Führt einen übergebenen Move aus, bewegt also alle in Move übergebenen
	 * Karten vom Stack <i>from</i> zum Stack <i>to</i>. <i>Der Zug wird nicht
	 * auf Gültigkeit überprüft, dies muss also (falls erwünscht) vorher
	 * geschehen</i>. Alle registrierten AUIs werden nach Ausführung darüber
	 * aufgerufen.
	 * 
	 * @param move
	 *            Der Move, der ausgeführt werden soll.
	 * @throws InconsistentMoveException
	 *             Falls ein Move inkonsistent zum Inhalt des Spielfeldes ist
	 * @see GameMove
	 */
	public void executeMove(GameMove move) throws InconsistentMoveException {
		executeMove(move, true);
	}

	private void performAfterMoveRoutine(Battlefield battlefield, GameMove move, boolean isImplicit)
			throws InconsistentMoveException {
		if (ZankMoveProvider.isEndTurnMove(battlefield, move)) {
			endCurrentTurn();
		} else {
			if (ZankMoveProvider.middleStackEmpty(battlefield)) {
				performZankLeftStackFlip(move.getTurn());
			}
			performCardFlips(move.getTurn());

			if (!isImplicit) {
				performImplicitMoves(move.getTurn());
			}
		}
	}

	/**
	 * Führt einen übergebenen Move aus, bewegt also alle in Move übergebenen
	 * Karten vom Stack <i>from</i> zum Stack <i>to</i>. <i>Der Zug wird nicht
	 * auf Gültigkeit überprüft, dies muss also (falls erwünscht) vorher
	 * geschehen</i>. Alle registrierten AUIs werden nach Ausführung darüber
	 * aufgerufen.
	 * 
	 * @param move
	 *            Der Move, der ausgeführt werden soll.
	 * @param addToHistory
	 *            Flag, ob der Move zur MoveHistory hinzugefügt werden soll
	 *            (<i>true</i> falls er hinzugefügt werden soll)
	 * @throws InconsistentMoveException
	 * @see GameMove
	 */
	public void executeMove(GameMove move, boolean addToHistory, boolean isImplicit, boolean afterMoveRoutine)
			throws InconsistentMoveException {
		Battlefield battlefield = mpcController.getBattlefield();
		if (battlefield.isFinished()) {
			return;
		}

		callBeforeMove(move, isImplicit);

		battlefield.executeMove(move);

		if (addToHistory) {
			if (battlefield.getGameType() == GameType.AGGRO_PATIENCE && move.getPlayer() != null
					&& move.getPlayer().getType() != PlayerType.HUMAN
					&& move.getPlayer().getType() != PlayerType.HUMAN_ONLINE && move.getFrom() != move.getTo())
				System.out.println(move.toProtocol());

			battlefield.getMoveHistory().add(move);
		}

		callAfterMove(move, isImplicit);
		if (hasWon(battlefield)) {
			callOnGameFinished();
			battlefield.setFinished(true);
		} else if (hasLost(battlefield)) {
			callOnGameFinished();
			battlefield.setFinished(true);
		}

		if (!battlefield.isFinished() && afterMoveRoutine && addToHistory
				&& battlefield.getGameType() == GameType.AGGRO_PATIENCE) {
			performAfterMoveRoutine(battlefield, move, isImplicit);
		}
	}

	/**
	 * Führt einen übergebenen Move aus, bewegt also alle in Move übergebenen
	 * Karten vom Stack <i>from</i> zum Stack <i>to</i>. <i>Der Zug wird nicht
	 * auf Gültigkeit überprüft, dies muss also (falls erwünscht) vorher
	 * geschehen</i>. Alle registrierten AUIs werden nach Ausführung darüber
	 * aufgerufen.
	 * 
	 * @param move
	 *            Der Move, der ausgeführt werden soll.
	 * @param addToHistory
	 *            Flag, ob der Move zur MoveHistory hinzugefügt werden soll
	 *            (<i>true</i> falls er hinzugefügt werden soll)
	 * @throws InconsistentMoveException
	 * @see GameMove
	 */
	public void executeMove(GameMove move, boolean addToHistory) throws InconsistentMoveException {
		executeMove(move, addToHistory, false, true);
	}

	/**
	 * Ruft bei allen BattlefieldAUIs onNewGameStart und onTurnBegin auf
	 * 
	 * @throws InconsistentMoveException
	 */
	public void startGame() throws InconsistentMoveException {
		for (BattlefieldAUI aui : battlefieldAUIs) {
			aui.onGameStart();
		}
		callOnTurnBegin(mpcController.getBattlefield().getCurrentPlayer());
	}

	/**
	 * Beendet den Zug des aktuellen Spielers
	 * 
	 * @throws InconsistentMoveException
	 */
	public void endCurrentTurn() throws InconsistentMoveException {
		Battlefield battlefield = mpcController.getBattlefield();
		Player currentPlayer = battlefield.getCurrentPlayer();
		if (currentPlayer.equals(battlefield.getPlayerOne())) {
			setTurnForPlayer(battlefield.getPlayerTwo());
		} else if (currentPlayer.equals(battlefield.getPlayerTwo())) {
			setTurnForPlayer(battlefield.getPlayerOne());
		}
	}

	/**
	 * Macht den zuletzt durchgeführten Zug rückgängig
	 * 
	 * @return Die Anzahl der Züge die rückgängig gemacht wurde
	 * @throws InconsistentMoveException
	 *             Falls hierbei ein inkonsistenter Move durchgeführt werden
	 *             würde
	 */
	public int undoMove() throws InconsistentMoveException {

		mpcController.getBattlefield().setGotHelp();
		int count = 0;
		Battlefield battlefield = mpcController.getBattlefield();
		if (battlefield.isFinished()) {
			battlefield.setFinished(false);
		}
		ArrayList<GameMove> history = battlefield.getMoveHistory();
		if (history.size() > 0) {
			int lastTurn = history.get(history.size() - 1).getTurn();
			Player playerOfTurn = battlefield.getCurrentPlayer();
			while (history.size() > 0 && history.get(history.size() - 1).getTurn() == lastTurn && lastTurn != -1) {
				GameMove oldMove = history.get(history.size() - 1);
				GameMove reverseMove = new GameMove(
						new GameMoveParameter(oldMove.getTo(), oldMove.getFrom(), oldMove.getCards()),
						battlefield.getCurrentPlayer(), oldMove.getFlip(), oldMove.getTurn());
				executeMove(reverseMove, false);
				count++;
				playerOfTurn = history.get(history.size() - 1).getPlayer();
				history.remove(history.size() - 1);
			}
			if (battlefield.getGameType() == GameType.AGGRO_PATIENCE)
				setTurnForPlayer(playerOfTurn);
		}
		return count;
	}

	/**
	 * Ruft bei allen BattlefieldAUIs onAIMove auf
	 * 
	 * @param end
	 *            Ob der Zug zuende ist
	 */
	public void callOnAIMove(boolean end) {
		for (BattlefieldAUI aui : battlefieldAUIs) {
			aui.onAIMove(end);
		}
	}

	/**
	 * Überprüft einen übergebenen Move, ob er (bezüglich des aktuellen Spiels)
	 * gültig ist.
	 * 
	 * @param move
	 *            Der zu überprüfende Move
	 * @return Gibt <i>true</i> falls der Move gültig ist, <i>false</i> falls er
	 *         es nicht istt.
	 */
	public boolean isValid(GameMove move) {
		return isValid(mpcController.getBattlefield(), move);
	}

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
	public static boolean isValid(Battlefield battlefield, GameMove move) {
		if (move.getTo() == null) {
			return moveFromRuleSetProvider.getMoveFromRuleSet(battlefield.getGameType())
					.get(move.getFrom().getSuperType()).isValid(battlefield, move);
		}
		return moveFromRuleSetProvider.getMoveFromRuleSet(battlefield.getGameType()).get(move.getFrom().getSuperType())
				.isValid(battlefield, move)
				&& moveToRuleSetProvider.getMoveToRuleSet(battlefield.getGameType()).get(move.getTo().getSuperType())
						.isValid(battlefield, move);
	}

	/**
	 * Überprüft, ob das aktuele Spiel gewonnen ist.
	 * 
	 * @param battlefield
	 *            Das Battlefield auf dem überprüft werden soll
	 * @return Gibt <i>true</i> zurück, falls das Spiel gewonnen wurde, sonst
	 *         <i>false</i>.
	 */
	public static boolean hasWon(Battlefield battlefield) {
		return winConditionProvider.getWinConditions(battlefield.getGameType()).hasWon(battlefield);
	}

	/**
	 * Ruft alle BattlefieldAUIs onGameFinished auf
	 */
	protected void callOnGameFinished() {
		for (BattlefieldAUI aui : battlefieldAUIs) {
			aui.onGameFinished();
		}
		// TODO:
		Battlefield currentBattlefield = mpcController.getBattlefield();
		if (!currentBattlefield.getGotHelp() && (currentBattlefield.getPlayerOne().getType().equals(PlayerType.HUMAN))
				|| (currentBattlefield.getGameType().equals(GameType.AGGRO_PATIENCE)
						&& currentBattlefield.getPlayerTwo().getType().equals(PlayerType.HUMAN))) {
			mpcController.getStatisticsController().updateStats(hasWon(currentBattlefield));
		}
	}

	/**
	 * Gibt zurück, ob das Spiel auf dem übergebenen Battlefield verrloren ist
	 * 
	 * @param battlefield
	 *            Das Battlefield auf dem überprüft werden soll
	 * @return Gibt <i>true</i> zurück, falls das Spiel verloren ist,
	 *         <i>false</i> sonst.
	 */
	public static boolean hasLost(Battlefield battlefield) {
		return winConditionProvider.getWinConditions(battlefield.getGameType()).hasLost(battlefield);
	}

	/**
	 * Registriert eine MoveAUI, sodass beim ausführen eines Moves diese
	 * benachrichtigt werden
	 * 
	 * @param moveAUI
	 *            Die neu hinzuzufügende MoveAUI
	 * @see #callBeforeMove(GameMove)
	 * @see #callAfterMove(GameMove)
	 */
	public void addMoveAUI(MoveAUI moveAUI) {
		this.moveAUIs.add(moveAUI);
	}

	/**
	 * Entfernt das übergebene MoveAUI aus den registrierten AUIs. Es wird somit
	 * bei der Durchführung eines Zuges nicht mehr aktualisiert.
	 * 
	 * @param moveAUI
	 *            Das zu entfernende AUI
	 * @see #callBeforeMove(GameMove)
	 * @see #callAfterMove(GameMove)
	 */
	public void removeMoveAUI(MoveAUI moveAUI) {
		this.moveAUIs.remove(moveAUI);
	}

	/**
	 * Registriert eine neue BattlefieldAUI
	 * 
	 * @param battlefieldAUI
	 *            Die neue BattlefieldAUI
	 */
	public void addBattlefieldAUI(BattlefieldAUI battlefieldAUI) {
		this.battlefieldAUIs.add(battlefieldAUI);
	}

	/**
	 * Entfernt die BattlefieldAUi aus den registrierten BattlefieldAUIs
	 * 
	 * @param battlefieldAUI
	 *            Die zu entfernende BattlefieldAUI
	 */
	public void removeBattlefieldAUI(BattlefieldAUI battlefieldAUI) {
		this.battlefieldAUIs.remove(battlefieldAUI);
	}

	/**
	 * @return the zankAIs
	 */
	public ZankPatienceAI getZankAI(int difficulty) {
		return zankAIs[difficulty];
	}

	/**
	 * @param zankAIs
	 *            the zankAIs to set
	 */
	public void setZankAI(ZankPatienceAI zankAI, int difficulty) {
		this.zankAIs[difficulty] = zankAI;
	}

}
