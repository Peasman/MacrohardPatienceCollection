package model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import model.GameCard.Rank;
import model.GameCard.Suit;
import model.GameMove.GameMoveParameter;

/**
 * Testet die Klasse Move
 * @author Olga Scheftelowitsch
 */
public class MoveTest {
	GameMove move;
	ArrayList<GameCard> cards;
	
	
	/**
	 * Legt alle benötigten Felder für die Tests an
	 */
	@Before
	public void setUp(){
		cards = new ArrayList<GameCard>();
		cards.add(new GameCard(Suit.CLUBS, Rank.ACE, true));
		cards.add(new GameCard(Suit.HEARTS, Rank.JACK, true));
		cards.add(new GameCard(Suit.DIAMONDS, Rank.SEVEN, true));
		cards.add(new GameCard(Suit.DIAMONDS, Rank.EIGHT, true));
		cards.add(new GameCard(Suit.SPADES, Rank.TWO, true));
		Player fred = new Player(PlayerType.HUMAN, "Fred");
		move = new GameMove(new GameMoveParameter(CardStackType.FREECELL_1, CardStackType.TALON, cards), fred, true ,3);
	}
	/**
	 * Testet die Mehoden der Klasse Move
	 */
	@Test
	public void testTurn() {
		assert(move.getTurn()==3);
		assert(move.getCards().equals(cards));
		assert(move.getFlip()==true);
		assertEquals(move.getPlayer().getName(),"Fred");
		assert(move.getFrom().equals(CardStackType.FREECELL_1));
		assert(move.getTo().equals(CardStackType.TALON));
	}

}
