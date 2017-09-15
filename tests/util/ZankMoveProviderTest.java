package util;

import org.junit.Before;
import org.junit.Test;

import model.Battlefield;
import model.CardStackType;
import model.GameCard;
import model.GameCard.Rank;
import model.GameCard.Suit;
import model.GameMove;
import model.GameMove.SingleCardGameMoveParameter;
import model.Player;
import model.PlayerType;

/**
 * Testet die Klasse ZankMoveProvider
 * @author Moritz Ludolph
 */
public class ZankMoveProviderTest {
	Battlefield battlefield;
	Player playerOne, playerTwo;
	/**
	 * Initiliasiert alle benötigten Felder für die Tests
	 */
	@Before
	public void setUp(){
		playerOne = new Player(PlayerType.HUMAN, "Hans");
		playerOne = new Player(PlayerType.HUMAN, "Peter");
		battlefield = BattlefieldFactory.createAggroField(playerOne, playerTwo, null, null); 
	}

	/**
	 * Testet die Methode getNextImplicitMove(Battlefield)
	 */
	@Test
	public void testGetNextImplicitMove() {
		
	}

	/**
	 * Testet die Methode middleStackEmpty(Battlefield)
	 */
	@Test
	public void testMiddleStackEmpty() {
		battlefield.getStack(CardStackType.ZANK_MIDDLE_1).clear();
		assert(ZankMoveProvider.middleStackEmpty(battlefield));
	}

	/**
	 * Testet die Methode isEndTurnMove(Battlefield, Move)
	 */
	@Test
	public void testIsEndTurnMove() {
		GameMove move = new GameMove(new SingleCardGameMoveParameter(CardStackType.ZANK_MIDDLE_1, CardStackType.ZANK_LEFT_1, new GameCard(Suit.HEARTS, Rank.ACE, true)), playerOne, false);
		
		assert(ZankMoveProvider.isEndTurnMove(battlefield, move));
	}

}
