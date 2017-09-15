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
 * Testet die Klasse MoveToRuleSetProvider
 * 
 * @author Moritz Ludolph
 *
 */
public class MoveToRuleSetProviderTest {

	Battlefield battlefield;
	Player playerOne;

	/**
	 * Initiliasiert alle benötigten Felder für die Tests
	 */
	@Before
	public void setUp() throws Exception {
		playerOne = new Player(PlayerType.HUMAN, "Hans");
		battlefield = BattlefieldFactory.createFreeCellField(playerOne, null);
	}

	/**
	 * Testet die Methode getMoveToRuleSet(GameType)
	 */
	@Test
	public void testGetMoveToRuleSet() {
		GameMove move = new GameMove(new SingleCardGameMoveParameter(CardStackType.FREECELL_1, CardStackType.STACKER_3, new GameCard(Suit.HEARTS, Rank.ACE, true)), playerOne, false);

		assert (!MoveToRuleSetProvider.getInstance().getMoveToRuleSet(GameType.FREECELL).get(CardStackSuperType.STACKER)
				.isValid(battlefield, move));
	}

	/**
	 * Testet die Methode isOwner(CardStackType, Player)
	 */
	@Test
	public void testIsOwner() {
		Battlefield aggroField = BattlefieldFactory.createAggroField(playerOne, new Player(PlayerType.AI1, "Peter"), null, null);

		MoveToRuleSetProvider.getInstance().isOwner(aggroField, playerOne, CardStackType.ZANK_MIDDLE_1);
	}

}
