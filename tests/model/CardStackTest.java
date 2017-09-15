package model;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import model.GameCard.Rank;
import model.GameCard.Suit;

/**
 * 
 * Diese Klasse testet die Funktionen der CardStack Klasse.
 * 
 * @author Christian Walczuch, Moritz Ludolph
 */
public class CardStackTest {
	private ArrayList<GameCard> cards;
	private Player owner;
	private CardStackType type;
	private CardStack stack1;

	/**
	 * Erzeugt alle nötigen Daten für den Test
	 */
	@Before
	public void setup() {
		cards = new ArrayList<GameCard>();
		cards.add(new GameCard(Suit.CLUBS, Rank.ACE, false));
		cards.add(new GameCard(Suit.DIAMONDS, Rank.JACK, false));

		stack1 = new CardStack(type, owner);
	}

	/**
	 * Testet den Konstruktor CardStack(CardStackType, Player)
	 */
	@Test
	public void testCardStackCardStackTypePlayer() {
		stack1 = new CardStack(CardStackType.ROW_1, owner);

		assertEquals(stack1.getType(), CardStackType.ROW_1);
		assertEquals(stack1.getOwner(), owner);
	}

	/**
	 * Testet den Konstruktor CardStack(CardStackType, Suit)
	 */
	@Test
	public void testCardStackCardStackTypeSuit() {
		stack1 = new CardStack(CardStackType.ROW_1, Suit.CLUBS);

		assertEquals(stack1.getType(), CardStackType.ROW_1);
		assertEquals(stack1.getSuit(), Suit.CLUBS);
	}

	/**
	 * Testet die Methode push
	 */
	@Test
	public void testPush() {

		stack1.push(cards.get(0));
		assertEquals(stack1.peek(), cards.get(0));

		stack1.push(cards.get(1));
		assertEquals(stack1.peek(), cards.get(1));
	}

	/**
	 * Testet die Methode pushAll(ArrayList<Card>)
	 */
	@Test
	public void testPushAll() {
		stack1.pushAll(cards);
		assertEquals(stack1.peek(), cards.get(1));
	}

	/**
	 * Testet die Methode peek()
	 */
	@Test
	public void testPeek() {
		stack1.push(cards.get(0));
		assertEquals(stack1.peek(), cards.get(0));

		stack1.pop();
		assert (stack1.peek() == null);
	}

	/**
	 * Testet die Methode pop()
	 */
	@Test
	public void testPop() {
		stack1.push(cards.get(0));
		GameCard card = stack1.pop();
		assert (stack1.size() == 0);
		assertEquals(card, cards.get(0));

		assert (stack1.pop() == null);
	}

	/**
	 * Testet die Methode size()
	 */
	@Test
	public void testSize() {
		stack1.push(cards.get(0));
		assert (stack1.size() == 1);
		stack1.pop();
		assert (stack1.size() == 0);
	}

	/**
	 * Testet die Methode clear()
	 */
	@Test
	public void testClear() {
		stack1.push(cards.get(0));
		stack1.push(cards.get(1));
		assert (stack1.size() == 2);
		stack1.clear();
		assert (stack1.size() == 0);
	}

	/**
	 * Testet die Methode createDefaultDeck(boolean, boolean)
	 */
	@Test
	public void testCreateDefaultDeckBooleanBoolean() {
		CardStack deck = CardStack.createDefaultDeck(true, true);

		assert (deck.size() == 52);
		assert (deck.peek().isFaceup() == true);
		assert (deck.peek().getBlueBack() == true);
	}

	/**
	 * Testet die fromProtocol(String) Methode
	 */
	@Test
	public void testFromProtocol() {
		String protocolString = "Pik-Ass Herz-4";
		CardStack stack = CardStack.fromProtocol(protocolString, false);

		assert (stack.size() == 2);

		GameCard pop = stack.pop();

		assert (pop.getSuit() == Suit.SPADES);
		assert (pop.getRank() == Rank.ACE);

		pop = stack.pop();

		assert (pop.getSuit() == Suit.HEARTS);
		assert (pop.getRank() == Rank.FOUR);
	}

	/**
	 * Testet die Methode toProtocol()
	 */
	@Test
	public void testToProtocol() {
		stack1.pushAll(cards);
		assertEquals(stack1.toProtocol(), "Karo-Bube Kreuz-Ass");
	}
}
