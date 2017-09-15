package model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.GameCard.Rank;
import model.GameCard.Suit;

/**
 * Diese Klasse testet die Funktionen der Klasse Card sowie deren Enums Rank und
 * Suit
 *
 * @author Christian Walczuch, Moritz Ludolph
 */

public class CardTest {
	private GameCard card1, card2;

	/*
	 * Initialisiert alle benötigten Felder für die Tests
	 */

	@Before
	public void setup() {
		card1 = new GameCard(Suit.HEARTS, Rank.ACE, true); // HEARTS, DIAMONDS,
														// SPADES, CLUBS
		card2 = new GameCard(Suit.DIAMONDS, Rank.JACK, false);
	}

	/**
	 * Testet die Methoden des Rank Enums
	 */
	@Test
	public void testRank() {
		assertEquals(Rank.fromProtocol("Ass"), Rank.ACE);
		assertEquals(Rank.fromProtocol("2"), Rank.TWO);

		assertEquals(Rank.EIGHT.toProtocol(), "8");
		assertEquals(Rank.KING.toProtocol(), "Koenig");
	}

	/**
	 * Testet die Methoden des Suit Enums
	 */
	@Test
	public void testSuit() {
		assertEquals(Suit.fromProtocol("Herz"), Suit.HEARTS);
		assertEquals(Suit.fromProtocol("Pik"), Suit.SPADES);

		assertEquals(Suit.DIAMONDS.toProtocol(), "Karo");
		assertEquals(Suit.CLUBS.toProtocol(), "Kreuz");
	}

	/*
	 * Testet den Konstruktor der Klasse
	 */
	@Test
	public void testCard() {
		assert (card1.getSuit() == Suit.HEARTS);
		assert (card1.getRank() == Rank.ACE);
		assert (card1.isFaceup());

		assert (!card2.isFaceup());
		assert (card2.getSuit() == Suit.DIAMONDS);
		assert (card2.getRank() == Rank.JACK);
	}

	/**
	 * Testet die Methode sameCardAs(Card)
	 */
	@Test
	public void testSameCardAs() {
		GameCard other = new GameCard(Suit.HEARTS, Rank.ACE, true);
		assert (card1.sameCardAs(other));
	}

	/**
	 * Testet die Methode isRed()
	 */
	@Test
	public void testIsRed() {
		assert (card1.isRed());
		assert (card2.isRed());

		GameCard card = new GameCard(Suit.SPADES, Rank.ACE, true);
		assert (!card.isRed());

	}

	/**
	 * Testet die Methode isBlack()
	 */
	@Test
	public void testIsBlack() {
		assert (!card1.isBlack());
		assert (!card2.isBlack());

		GameCard card = new GameCard(Suit.SPADES, Rank.ACE, true);
		assert (card.isBlack());
	}

	/**
	 * Testet die Methode sameColorAs(Card)
	 */
	@Test
	public void testSameColorAs() {
		assert (card1.sameColorAs(card2));
		GameCard card = new GameCard(Suit.SPADES, Rank.ACE, true);
		assert (!card.sameColorAs(card2));
	}

	/**
	 * Testet die Methode fromProtocl(String)
	 */
	@Test
	public void testFromProtocol() {
		GameCard card = GameCard.fromProtocol("Herz-Ass", false);
		assert (card.sameCardAs(card1));
	}

	/**
	 * Testet die Methode toProtocol()
	 */
	@Test
	public void testToProtocol() {
		assertEquals(card1.toProtocol(), "Herz-Ass");
	}

}
