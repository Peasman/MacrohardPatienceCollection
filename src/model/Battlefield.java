package model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;

import ai.general.GameState;

/**
 * Die Hauptklasse der Control-Schicht verwaltet alle Stapel, Spieler und alle gemachten Kartenbewegungen.
 * @author Alexander Herlez
 */
public class Battlefield extends SuperBattlefield implements Cloneable, Serializable, GameState {
	private static final long serialVersionUID = 6627545853935306671L;

	/**
	 * Das aktuell gespielte Spiel
	 */
	private GameType gameType;

	/**
	 * Gibt an ob der Spieler Hilfe erhalten hat
	 */
	private boolean gotHelp;

	/**
	 * Bigt an, ob das Spiel auf dem Feld beendet ist
	 */
	private boolean isFinished;

	/**
	 * die aktuell verstichene Zeit in dem Spiel in s
	 */
	private long gameTime;

	/** Das Spielfeld beinhaltet alle Kartenstapel, Spieler und die move history
	 * @param gameType das aktuelle Spiel
	 * @param stackMap die Map, welche die Kartenstapel verwaltet
	 * @param playerOne der erste Spieler
	 * @param playerTwo der zweite Spieler */
	public Battlefield(GameType gameType, EnumMap<CardStackType, CardStack> stackMap, Player playerOne,
			Player playerTwo) {
		this.playerOne = playerOne;
		this.playerTwo = playerTwo;
		this.gameType = gameType;
		this.currentPlayer = playerOne;
		this.stackMap = stackMap;
		this.moveHistory = new ArrayList<GameMove>();
		this.gotHelp = false;
		this.isFinished = false;
		this.gameTime = 0;
	}

	/** setzt die Spielzeit
	 * @param newGameTime die neue Spielzeit */
	public void setGameTime(long newGameTime) {
		gameTime = newGameTime;
	}

	/** gibt die aktuelle Spielzeit wieder
	 * @return aktuelle Spieltzeit */
	public long getGameTime() {
		return gameTime;
	}

	/**
	 * gibt die MoveHistory aus
	 */
	public void printHistory() {
		for (GameMove move : moveHistory) {
			//move.getPlayer().getName()
			System.out.println(move.toProtocol());
		}
		System.out.println("+++++++++++");
	}
	

	/** gibt an wie viele Karten ein Spieler noch besitzt. Wird für die Statistik
	 * benutzt.
	 * @return die Anzahl der Restkarten */
	public double getCardsLeft() {
		switch (gameType) {
		case IDIOT_PATIENCE:
			return (double) stackMap.get(CardStackType.ROW_1).size() + stackMap.get(CardStackType.ROW_2).size()
					+ stackMap.get(CardStackType.ROW_3).size() + stackMap.get(CardStackType.ROW_4).size();

		case FREECELL:
			return (double) stackMap.get(CardStackType.ROW_1).size() + stackMap.get(CardStackType.ROW_2).size()
					+ stackMap.get(CardStackType.ROW_3).size() + stackMap.get(CardStackType.ROW_4).size()
					+ stackMap.get(CardStackType.ROW_5).size() + stackMap.get(CardStackType.ROW_6).size()
					+ stackMap.get(CardStackType.ROW_7).size() + stackMap.get(CardStackType.ROW_8).size()
					+ stackMap.get(CardStackType.FREECELL_1).size() + stackMap.get(CardStackType.FREECELL_2).size()
					+ stackMap.get(CardStackType.FREECELL_3).size() + stackMap.get(CardStackType.FREECELL_4).size();

		case AGGRO_PATIENCE:
			return (double) stackMap.get(CardStackType.ZANK_LEFT_1).size()
					+ stackMap.get(CardStackType.ZANK_MIDDLE_1).size()
					+ stackMap.get(CardStackType.ZANK_RIGHT_1).size();
		default:
			return 35505;
		}
	}

	/** gibt die Anzahl an Zügen wieder. Nützlich für die Statistik
	 * @return Zuganzahl */
	public double getNumberOfTurns() {
		switch (gameType) {
		case IDIOT_PATIENCE:
			return (double) moveHistory.size() - 51;
		case FREECELL:
			return (double) moveHistory.size();
		case AGGRO_PATIENCE:
			return calcMoves();
		default:
			return 35505;
		}
	}

	/** gibt die oberste Karte eines Stapels wieder
	 * @param type   der Stapel, von dem die oberste Karte angezeigt werden soll
	 * @return die oberste Karte */
	public GameCard getUpperCard(CardStackType type) {
		return stackMap.get(type).peek();
	}





	/** gibt einen CardStack wieder
	 * @param der Name des CardStacks
	 * @return der CardStack */
	public CardStack getStack(CardStackType type) {
		return stackMap.get(type);
	}

	/**
	 * @return der erste Spieler */
	public Player getPlayerOne() {
		return playerOne;
	}

	/** @return der zweite Spieler */
	public Player getPlayerTwo() {
		return playerTwo;
	}

	/** setzt den aktuellen Spieler
	 * @param newCurrentPlayer der neue aktuelle Spieler */
	public void setCurrentPlayer(Player newCurrentPlayer) {
		this.currentPlayer = newCurrentPlayer;
	}

	/** gibt den Spieltypen wieder
	 * 
	 * @return der Spieltyp */
	public GameType getGameType() {
		return gameType;
	}

	/** gibt den aktuellen Spieler wieder
	 * @return der Spieler der gerade am Zug ist */
	public Player getCurrentPlayer() {
		return currentPlayer;
	}

	/** gibt die movehistory wieder
	 * @return die movehistory */
	public ArrayList<GameMove> getMoveHistory() {
		return moveHistory;
	}

	/** setzt den Wert gotHelp auf true. Der Spieler hat in diesem Spiel Hilfe geholt. */
	public void setGotHelp() {
		gotHelp = true;
	}

	/** gibt zurück ob der Spieler einen Ingame Tipp geholt hat
	 * @return ob der Spieler einen Ingame Tipp geholt hat */
	public boolean getGotHelp() {
		return gotHelp;
	}



	/** erzeugt ein neues Battlefield aus einer Menge von Stapeln
	 * @param die Stapel, aus denen ein neues Battlefield erzeugt werden soll
	 * @return das neue Battlefield */
	public Battlefield createNewGameState(ArrayList<CardStackType> stackTypes) {
		Battlefield battlefield = null;
		battlefield = clone();
		if (battlefield != null) {
			for (CardStackType type : stackTypes) {
				CardStack stack = serializeCardStack(type);
				battlefield.stackMap.replace(type, stack);
			}
		}
		return battlefield;
	}

	/** serialisiert einen Cardstack und gibt ihn wieder
	 * @param stackType  der Name des Stacks
	 * @return der serialisierte Cardstack */
	private CardStack serializeCardStack(CardStackType stackType) {
		CardStack cardStack = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(getStack(stackType));
			oos.flush();
			oos.close();
			bos.close();
			byte[] byteData = bos.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
			ObjectInputStream ois = new ObjectInputStream(bais);
			cardStack = (CardStack) ois.readObject();
			bais.close();
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return cardStack;
	}

	/** gibt einen Klon der EnumMap wieder, wird für die KI benutzt
	 * @return Klon der Stapelstackmap */
	public EnumMap<CardStackType, CardStack> cloneStackMap() {
		return stackMap.clone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Battlefield clone() {
		Battlefield battlefield = null;

		try {
			battlefield = (Battlefield) super.clone();
			battlefield.stackMap = stackMap.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		return battlefield;
	}



	/** prüft ob das Spiel zuende ist
	 * @return boolean der true ist, wenn das Spiel fertig ist */
	public boolean isFinished() {
		return isFinished;
	}

	/** setzt das Spiel auf finished oder nicht finished
	 * @param isFinished boolean, der true ist wenn das Spiel fertig sein soll */
	public void setFinished(boolean isFinished) {
		this.isFinished = isFinished;
	}
}
