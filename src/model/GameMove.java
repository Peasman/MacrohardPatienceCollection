package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Diese Klasse verwaltet eine Kartenbewegung und wird in der movehistory vom
 * Battlefield gespeichert. Es können einzelne und mehrere Karten gleichzeitig
 * bewegt werden.
 * 
 * @author Alexander Herlez
 */
public class GameMove implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4838760570486033964L;

	/**
	 * gibt an von welchem Kartenstapel die Karte kommt
	 */
	private CardStackType fromStack;

	/**
	 * gibt an zu welchem Kartenstapel die Karte gelegt wurde
	 */
	private CardStackType toStack;

	/**
	 * die Karten welche bewegt wurde
	 */
	private ArrayList<GameCard> cards;

	/**
	 * der Spieler der die Karte bewegt hat
	 */
	private Player player;

	/**
	 * gibt an ob die Karten gedreht werden sollen
	 */
	private boolean flip;

	
	/**
	 * Karten die innerhalb eines Zuges bewegt werden haben den gleichen Wert. Nützlich um mehrere Kartenbewegungen auf unterschiedliche Stapel direkt zurücksetzen zu können.
	 */
	private int turn;

	/**
	 * @return gibt den Kartenstapel von dem die Karte kommt zurück
	 */
	public CardStackType getFrom() {
		return fromStack;
	}

	/**
	 * @return gibt den Kartenstapel zu dem die Karte gelegt wird zurück
	 */
	public CardStackType getTo() {
		return toStack;
	}

	/**
	 * @return gibt die Karte, welche bewegt wurde, zurück
	 */
	public ArrayList<GameCard> getCards() {
		return cards;
	}

	/**
	 * @return gibt den Spieler, der die Karte bewegt hat, zurück
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return gibt an, ob die Karten gedreht wurden
	 */
	public boolean getFlip() {
		return flip;
	}

	/**
	 * Gibt den Protokolstring für den Move zurück
	 * @return Der Protokollstring (z.B. A1->B2)
	 */
	public String toProtocol() {
		return getFrom().toProtocol() + "->" + getTo().toProtocol();
	}

	/**
	 * Gibt für einen übergebenen Protokollstring und ein Battlefield einen dazugehörigen Move zurück
	 * @param protocol Der Protokolstring
	 * @param battlefield Das Battlefield auf dem der Move ausgeführt wird
	 * @return Der Move, null falls nicht parsebar.
	 */
	public static GameMove fromProtocol(String protocol, Battlefield battlefield) {
		StringTokenizer tokenizer = new StringTokenizer(protocol, "->");
		String fromStk = tokenizer.nextToken();
		String toStk = tokenizer.nextToken();
		CardStackType fromStack = CardStackType.fromProtocol(fromStk);
		CardStackType toStack = CardStackType.fromProtocol(toStk);
		if (fromStack == null || toStack == null) {
			return null;
		}
		GameMove move = new GameMove(new SingleCardGameMoveParameter(fromStack, toStack, battlefield.getUpperCard(fromStack)), battlefield.getCurrentPlayer(), false);
		return move;
	}

	public static class GameMoveParameter {
		public CardStackType fromStk;
		public CardStackType toStk;
		public ArrayList<GameCard> cards;

		public GameMoveParameter(CardStackType fromStk, CardStackType toStk, ArrayList<GameCard> cards) {
			this.fromStk = fromStk;
			this.toStk = toStk;
			this.cards = cards;
		}
	}


	/**
	 * Die Bewegung mehrerer Karten
	 * @param parameterObject TODO
	 * @param player
	 *            der Spieler, der die Karte bewegt
	 * @param flip
	 *            ob die Karten gedreht werden sollen
	 * @param from
	 *            der Kartenstapel, von dem die Karte kommt
	 * @param to
	 *            der Kartenstapel zu dem die Karte gelegt wird
	 */

	public GameMove(GameMoveParameter parameterObject, Player player, boolean flip) {
		this(parameterObject, player, flip, 0);
	}


	/**
	 * Die Bewegung mehrerer Karten
	 * @param parameterObject TODO
	 * @param player
	 *            der Spieler, der die Karte bewegt
	 * @param flip
	 *            ob die Karten gedreht werden sollen
	 * @param turn ein int für den Turn
	 * @param from
	 *            der Kartenstapel, von dem die Karte kommt
	 * @param to
	 *            der Kartenstapel zu dem die Karte gelegt wird
	 */
	public GameMove(GameMoveParameter parameterObject, Player player, boolean flip, int turn) {
		this.fromStack = parameterObject.fromStk;
		this.toStack = parameterObject.toStk;
		this.flip = flip;
		this.cards = parameterObject.cards;
		this.player = player;
		this.turn = turn;
	}

	public static class SingleCardGameMoveParameter {
		public CardStackType fromStk;
		public CardStackType toStk;
		public GameCard card;

		public SingleCardGameMoveParameter(CardStackType fromStk, CardStackType toStk, GameCard card) {
			this.fromStk = fromStk;
			this.toStk = toStk;
			this.card = card;
		}
	}


	/**
	 * Die Bewegung einer einzelnen Karte
	 * @param parameterObject TODO
	 * @param player
	 *            der Spieler, der die Karte bewegt
	 * @param flip
	 *            ob die Karten gedreht werden sollen
	 * @param from
	 *            der Kartenstapel, von dem die Karte kommt
	 * @param to
	 *            der Kartenstapel zu dem die Karte gelegt wird
	 */
	public GameMove(SingleCardGameMoveParameter parameterObject, Player player, boolean flip) {
		this(parameterObject, player, flip, 0);
	}


	/**
	 * Die Bewegung einer einzelnen Karte
	 * @param parameterObject TODO
	 * @param player
	 *            der Spieler, der die Karte bewegt
	 * @param flip
	 *            ob die Karten gedreht werden sollen
	 * @param turn ein int für den Turn
	 * @param from
	 *            der Kartenstapel, von dem die Karte kommt
	 * @param to
	 *            der Kartenstapel zu dem die Karte gelegt wird
	 */
	public GameMove(SingleCardGameMoveParameter parameterObject, Player player, boolean flip, int turn) {
		this(new GameMoveParameter(parameterObject.fromStk, parameterObject.toStk, new ArrayList<GameCard>()), player, flip, turn);
		ArrayList<GameCard> cardToAdd = new ArrayList<GameCard>();
		cardToAdd.add(parameterObject.card);
		this.cards = cardToAdd;
	}

	
	/**
	 * Gibt den Turn des Zuges zurück
	 * @return Der Turn des Zuges
	 */
	public int getTurn() {
		return turn;
	}

}
