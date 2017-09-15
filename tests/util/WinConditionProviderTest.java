package util;

import org.junit.Before;
import org.junit.Test;

import model.Battlefield;
import model.GameType;
import model.Player;
import model.PlayerType;

/**
 * Testet die Methode WinConditionProvider
 * @author Moritz Ludolph
 *
 */
public class WinConditionProviderTest {
	Battlefield battlefield;
	Player playerOne;
	/**
	 * Initiliasiert alle benötigten Felder für die Tests
	 */
	@Before
	public void setUp(){
		playerOne = new Player(PlayerType.HUMAN, "Peter");
		battlefield = BattlefieldFactory.createFreeCellField(playerOne, null);
	}

	/**
	 * Testet die Methode getWinConditions() 
	 */
	@Test
	public void testGetWinConditions() {
		assert(!WinConditionProvider.getInstance().getWinConditions(GameType.FREECELL).hasWon(battlefield));
	}

}
