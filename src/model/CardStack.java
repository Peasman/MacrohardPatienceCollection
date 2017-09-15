package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import model.GameCard.Suit;

/**
 * Klasse zur Verwaltung eines Kartenstapels
 * 
 * @author Moritz Ludolph
 */
public class CardStack implements Iterable<GameCard>, Serializable {
	private static final long serialVersionUID = 1345161791354815069L;
	private ArrayList<GameCard> cards;
	private Player owner = null;
	private Suit suit = null;
	private CardStackType type;

	/**
	 * Erzeugt einen leeren Kartenstapel. <i>(wird nur für das Erstellen eines
	 * DefaultDecks benötigt)</i>
	 */
	private CardStack() {
		cards = new ArrayList<GameCard>();
	}

	/**
	 * Erzeugt einen leeren Kartenstapel mit dem übergeben Typ
	 * 
	 * @param type
	 *            Der Typ, der dem Kartenstapel zugeordnet werden soll
	 */
	public CardStack(CardStackType type) {
		this();
		this.type = type;
	}

	/**
	 * Erzeugt einen leeren Kartenstapel mit dem übergebenen Typ und Player als
	 * Besitzer
	 * 
	 * @param type
	 *            Der Typ, der dem Kartenstapel zugeordnet werden soll
	 * @param owner
	 *            Der Besitzer des Kartenstapels
	 * @see CardStackType
	 * @see Player
	 */
	public CardStack(CardStackType type, Player owner) {
		this(type);
		this.owner = owner;
	}

	/**
	 * Erzeugt einen leeren Kartenstapel mit dem übergebenen Typ und Suite
	 * 
	 * @param type
	 *            Der Typ der dem Stack zugeordnet sein soll
	 * @param suit
	 *            Die Suit die dem Stack zugeordnet sein soll
	 * @see CardStackType
	 * @see Suit
	 */
	public CardStack(CardStackType type, Suit suit) {
		this(type);
		this.suit = suit;
	}

	/**
	 * Gibt den zugeordnet Type des Stacks zurück
	 * 
	 * @return Der Typ des Stacks, null falls dieser nicht zugeordnet ist
	 */
	public CardStackType getType() {
		return this.type;
	}

	/**
	 * Gibt den Suit zurück, für den dieser Stapel fungiert (z.B. Herz)
	 * 
	 * @return Gibt den Suit des Stacks zurück, null falls dieser nicht gesetzt
	 *         wurde.
	 */
	public Suit getSuit() {
		return suit;
	}

	/**
	 * Gibt den Besitzer des Kartenstapels zurück
	 * 
	 * @return Der Besitzer des Kartenstapels, <i>null</i> falls Besitzer nicht
	 *         festgelget oder nicht existent
	 * @see Player
	 */
	public Player getOwner() {
		return owner;
	}

	/**
	 * Packt eine Karte als oberste Karte auf den Stapel
	 * 
	 * @param card
	 *            Die neue Karte
	 * @see GameCard
	 */
	public void push(GameCard card) {
		cards.add(card);
	}

	/**
	 * Packt alle übergebenen Karten in der übergebenen Reihenfolge auf den
	 * Kartenstapel
	 * 
	 * @param cards
	 *            Die neuen Karten
	 * @see GameCard
	 */
	public void pushAll(ArrayList<GameCard> cards) {
		this.cards.addAll(cards);
	}

	/**
	 * Gibt die oberste Karte des Kartenstapels zurück ohne diese zu entfernen.
	 * 
	 * @return Die oberste Karte des Stapels, <i>null</i> falls der Stapel leer ist
	 * @see GameCard
	 */
	public GameCard peek() {
		if (cards.size() > 0)
			return cards.get(cards.size() - 1);
		else
			return null;
	}

	/**
	 * Entfernt die oberste Karte des Kartenstapels und gibt diese zurück
	 * 
	 * @return Die entfernte Karte, <i>null</i> falls der Stapel leer ist
	 * @see GameCard
	 */
	public GameCard pop() {
		if (cards.size() > 0) {
			GameCard card = cards.get(cards.size() - 1);
			cards.remove(cards.size() - 1);
			return card;
		} else
			return null;
	}

	/**
	 * Gibt die Anzahl der Karten auf dem Kartenstapel zurück
	 * 
	 * @return Die Anzahl Karten
	 */
	public int size() {
		return cards.size();
	}

	/**
	 * Mischt die Karten auf dem Stack neu
	 */
	public void shuffle() {
		Collections.shuffle(cards);
	}

	/**
	 * Gibt den Kartenstapel als Liste zurück
	 * 
	 * @return Eine Liste aller Karten des Kartenstapels
	 */
	public ArrayList<GameCard> toList() {
		return cards;
	}

	/**
	 * Leert den Kartenstapel
	 */
	public void clear() {
		cards.clear();
	}

	/**
	 * Erzeugt einen neuen Kartenstapel mit zufälligen 52 Karten
	 * 
	 * @param faceup
	 *            Gibt an, ob die Karten mit der Spielseite nach oben gedreht sein
	 *            sollen oder nicht
	 * @return Zufälliger Kartenstapel mit 52 Karten
	 * @see Cards
	 */
	public static CardStack createDefaultDeck(boolean faceup) {
		return createDefaultDeck(faceup, false);
	}

	/**
	 * Erzeugt einen neuen Kartenstapel mit zufälligen 52 Karten
	 * 
	 * @param faceup
	 *            Gibt an, ob die Karten mit der Spielseite nach oben gedreht sein
	 *            sollen oder nicht
	 * @return Zufälliger Kartenstapel mit 52 Karten
	 * @see Cards
	 */
	public static CardStack createDefaultDeck(boolean faceup, boolean blueBack) {
		CardStack cardStack = new CardStack();

		for (GameCard.Suit suit : GameCard.Suit.values()) {
			for (GameCard.Rank rank : GameCard.Rank.values()) {
				GameCard card = new GameCard(suit, rank, faceup);
				card.setBlueBack(blueBack);
				cardStack.push(card);
			}
		}

		cardStack.shuffle();
		return cardStack;
	}
	
	/**
	 * Gibt für einen übergebenen Protokollstring den CardStack mit aufgefüllten Karten zurück
	 * @param protocolString Der Protokollstring
	 * @return Der CardStack für den Protokollstring, null bei Fehler
	 */
	public static CardStack fromProtocol(String protocolString, boolean faceUp){
		CardStack stack = new CardStack();
		StringTokenizer tokenizer = new StringTokenizer(protocolString, " ");
		
		while (tokenizer.hasMoreTokens()){
			String token = tokenizer.nextToken();
			GameCard card = GameCard.fromProtocol(token, faceUp);
			
			if (card == null)
				return null;
			
			stack.push(card);
		}
		
		Collections.reverse(stack.cards);
		
		return stack;
	}
	
	/**
	 * Gibt für den CardStack den Protokollstring zurück
	 * @return Der Protokolstring
	 */
	public String toProtocol(){
		String serialized = "";
		for (GameCard card : this){
			serialized += card.toProtocol() + " ";
		}
		
		return serialized.trim();
	}
	

	/** (non-Javadoc)
	 * Gibt eine iterator zurück, der den Stack von oben nach unten durchläuft
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<GameCard> iterator() {
		return new Iterator<GameCard>() {
			int currentIndex = size() - 1;

			@Override
			public boolean hasNext() {
				return currentIndex >= 0;
			}

			@Override
			public GameCard next() {
				if (!hasNext())
					throw new NoSuchElementException();
				GameCard card = cards.get(currentIndex);
				currentIndex--;
				return card;
			}
		};
	}

	public void setOwner(Player owner) {
		this.owner = owner;
	}
}
