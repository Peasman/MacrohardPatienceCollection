package util;

import org.junit.Before;
import org.junit.Test;

import model.Battlefield;
import model.CardStackSuperType;
import model.CardStackType;
import model.GameCard;
import model.GameCard.Rank;
import model.GameCard.Suit;
import model.GameMove;
import model.GameMove.SingleCardGameMoveParameter;
import model.GameType;
import model.Player;
import model.PlayerType;

/**
 * Testet die Klasse MoveFromRuleSetProvider
 * 
 * @author Moritz Ludolph
 *
 */
public class MoveFromRuleSetProviderTest {
	private Battlefield battlefield;
	private Player playerOne;

	/**
	 * Initiliasiert alle benötigten Felder für die Tests
	 */
	@Before
	public void setUp() {
		playerOne = new Player(PlayerType.HUMAN, "Hans");
		battlefield = BattlefieldFactory.createFreeCellField(playerOne, null);
	}

	/**
	 * Testet die Methode getMoveFromRuleSet(GameType)
	 */
	@Test
	public void testGetMoveFromRuleSet() {
		GameMove move = new GameMove(new SingleCardGameMoveParameter(CardStackType.STACKER_1, null, new GameCard(Suit.HEARTS, Rank.ACE, true)), playerOne, false);

		assert (!MoveFromRuleSetProvider.getInstance().getMoveFromRuleSet(GameType.FREECELL)
				.get(CardStackSuperType.STACKER).isValid(battlefield, move));
	}

}
