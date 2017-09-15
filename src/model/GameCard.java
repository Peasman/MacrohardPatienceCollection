package model;

import java.io.Serializable;
import java.util.StringTokenizer;

/**
 * Klasse zur Verwaltung einer Spielkarte
 * 
 * @author Moritz Ludolph
 */
public class GameCard implements Serializable {
	private static final long serialVersionUID = 1740509618956329889L;

	/**
	 * Aufzählung aller möglichen Ränge einer Spielkarte.
	 * 
	 * @author moritz
	 */
	public enum Rank implements Serializable {
		ACE(1, "Ass"), TWO(2, "2"), THREE(3, "3"), FOUR(4, "4"), FIVE(5, "5"), SIX(6, "6"), SEVEN(7, "7"), EIGHT(8, "8"), NINE(9, "9"), TEN(10, "10"), JACK(11, "Bube"), QUEEN(
				12, "Dame"), KING(13, "Koenig");

		private int rankValue;
		private String germanName;

		private Rank(int rank, String germanName) {
			this.rankValue = rank;
			this.germanName = germanName;
		}

		/**
		 * Der zusätzliche zugewiesene Rang des Karteranges.
		 * 
		 * @return Der Rang als int-Wert.
		 */
		public int getRankValue() {
			return rankValue;
		}

		/**
		 * Setzt den zusätzlichen Rangwert eines Kartenranges.
		 * 
		 * @param rankValue
		 *            Der neue Rang als int-Wert.
		 */
		public void setRankValue(int rankValue) {
			this.rankValue = rankValue;
		}
		
		
		/**
		 * Gibt den Protokolnamen für den Rang zurück (z.B. Ass für ACE)
		 * @return Der Protokolstring für den Rang
		 */
		public String toProtocol(){
			return germanName;
		}
		
		/**
		 * Gibt für einen übergebenen Protokolnamen den korrekten Rang zurück
		 * @param string Der übergebene Protokolname
		 * @return Gibt den zugehörigen Rang zurück
		 */
		public static Rank fromProtocol(String string){
			for (Rank rank : Rank.values()){
				if (rank.toProtocol().equals(string)){
					return rank;
				}
			}
			return null;
		}
	}

	/**
	 * Aufzählung aller möglichen Farben einer Spielkarte.
	 * 
	 * @author moritz
	 */
	public enum Suit  implements Serializable{
		HEARTS("Herz"), DIAMONDS("Karo"), CLUBS("Kreuz"), SPADES("Pik");
		
		private String germanName;
		private Suit(String germanName){
			this.germanName = germanName;
		}
		
		/**
		 * Gibt den Protokolnamen für eine Suit zurück (z.B. Herz für HEARTS) 
		 * @return Gibt den 
		 */
		public String toProtocol() {
			return this.germanName;
		}
		
		/**
		 * Gibt den Suit für einen übergebenen Protokolnamen zurück
		 * @param string Der Protokolname
		 * @return Gibt den zugehörigenn Suit zurück oder null falls es keine Übereinstimmung gibt
		 */
		public static Suit fromProtocol(String string){
			for (Suit suit : Suit.values()){
				if (suit.toProtocol().equals(string)){
					return suit;
				}
			}
			return null;
		}
	}

	private Rank rank;
	private Suit suit;
	private boolean faceup;
	private boolean blueBack;

	/**
	 * Erstellt ein neues Spielkarten-Objekt.
	 * 
	 * @param suit
	 *            Die Farbe der neuen Karte
	 * @param rank
	 *            Der Rang der neuen Karte
	 * @param faceup
	 *            Ob die Karte mit der Spielseite nach oben liegt
	 * @see Suit
	 * @see Rank
	 */
	public GameCard(Suit suit, Rank rank, boolean faceup) {
		this.suit = suit;
		this.rank = rank;
		this.faceup = faceup;
	}

	/**
	 * Gibt zurück, ob die Karte eine blaue Rückseite hat
	 * 
	 * @return Gibt <i>true</i> zurück, falls die Karte eine blaue Rückseite
	 *         hat, sonst <i>false</i>
	 */
	public boolean getBlueBack() {
		return blueBack;
	}

	/**
	 * Setzt, ob die Karte eine blaue Rückseite hat
	 * 
	 * @param blueBack
	 *            Ob die Karte eine blaue Rückseite haben soll
	 */
	public void setBlueBack(boolean blueBack) {
		this.blueBack = blueBack;
	}

	/**
	 * Gibt die Farbe der Karte zurück.
	 * 
	 * @return Die Farbe der Karte als Suit.
	 * @see Suit
	 */
	public Suit getSuit() {
		return suit;
	}

	/**
	 * Gibt den Rang der Karte zurück.
	 * 
	 * @return Der Rang der Karte als Rank.
	 * @see Rank
	 */
	public Rank getRank() {
		return rank;
	}

	/**
	 * Setzt, ob die Karte mit der Spielseite nach oben ist
	 * 
	 * @param faceup
	 *            Boolscher Wert, ob die Spielseite oben ist
	 */
	public void setFaceup(boolean faceup) {
		this.faceup = faceup;
	}

	/**
	 * Gibt zurück, ob die Karte mit der Spielseite nach oben liegt
	 * 
	 * @return Gibt <i>true</i> zurück falls ja, sonst <i>false</i>
	 */
	public boolean isFaceup() {
		return faceup;
	}

	/**
	 * Überprüft, ob eine Karte denselben Rang und Suit hat.
	 * 
	 * @param otherCard
	 *            Die andere Karte
	 * @return Gibt <i>true</i> zurück, falls die Karten denselben Rang und Suit
	 *         haben, <i>false</i> sonst.
	 */
	public boolean sameCardAs(GameCard otherCard) {
		return getSuit() == otherCard.getSuit() && getRank() == otherCard.getRank();
	}

	/**
	 * Überprüft, ob die Karte eine rote Farbe hat.
	 * 
	 * @return Gibt <i>true</i> falls die Karte eine rote Farbe hat, sonst
	 *         <i>false</i>-
	 */
	public boolean isRed() {
		return this.suit == Suit.DIAMONDS || this.suit == Suit.HEARTS;
	}

	/**
	 * Überprüft, ob die Karte eine schwarze Farbe hat.
	 * 
	 * @return Gibt <i>true</i> falls die Karte eine schwarze Farbe hat, sonst
	 *         <i>false</i>.
	 */
	public boolean isBlack() {
		return this.suit == Suit.SPADES || this.suit == Suit.CLUBS;
	}

	/**
	 * Überprüft, ob eine Karte dieselbe Farbe wie eine andere Karte hat.
	 * 
	 * @param otherCard
	 *            Die andere Karte
	 * @return Gibt <i>true</i> zurück falls die Karte dieselbe Farbe haben,
	 *         <i>false</i> sonst.
	 */
	public boolean sameColorAs(GameCard otherCard) {
		if (isRed())
			return otherCard.isRed();
		else
			return otherCard.isBlack();
	}
	
	/**
	 * Gibt für einen übergebenen Protokolnamen die zugehörige Karte zurück
	 * @param string Der Protokolname
	 * @return Die Karte für den Protokolname oder null, falls die Karte nicht parsebar ist.
	 */
	public static GameCard fromProtocol(String string, boolean faceUp){
		StringTokenizer tokenizer = new StringTokenizer(string, "-");
		String suitStk = tokenizer.nextToken();
		String rankStk = tokenizer.nextToken();
		
		Suit suit = Suit.fromProtocol(suitStk);
		Rank rank = Rank.fromProtocol(rankStk);
		if (suit == null || rank == null)
			return null;
		return new GameCard(suit, rank, faceUp);
	}
	/**
	 * Gibt den Protokolnamen für die Karte zurück
	 * @return Den Protokolnamen für die Karte (z.B. Pik-Ass)
	 */
	public String toProtocol(){
		return suit.toProtocol() + "-" + rank.toProtocol();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (blueBack ? 1231 : 1237);
		result = prime * result + (faceup ? 1231 : 1237);
		result = prime * result + ((rank == null) ? 0 : rank.hashCode());
		result = prime * result + ((suit == null) ? 0 : suit.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		GameCard other = (GameCard) obj;
		if (blueBack != other.blueBack) {
			return false;
		}
		if (faceup != other.faceup) {
			return false;
		}
		if (rank != other.rank) {
			return false;
		}
		if (suit != other.suit) {
			return false;
		}
		return true;
	}	
}
