package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import model.Battlefield;
import model.CardStackType;
import model.GameType;
import model.Player;
import model.PlayerType;

/**
 * Testet, ob der GameController Battlefields vernünftig erstellt.
 * @author Friedemann Runte
 *
 */
public class GameControllerTest {

	MPCController mpcController;
	GameController gameController;

	/**
	 * erzeugt einen GameController und den dazugehörigen mpcController.
	 */
	@Before
	public void setUp() {
		mpcController = new MPCController();
		gameController = mpcController.getGameController();
	}

	/**
	 *  Überprüft ob die getInstance Methode eine gültige Instanz zurück gibt.
	 */
	@Test
	public void testGetInstance() {
		assertNotNull(GameController.getInstance());
	}

	/**
	 * Überprüft ob jeder Spielmodus ein passendes Battlefield erzeugt bekommt.
	 */
	@Test
	public void testInitBattlefield() {
		Player playerOne = new Player(PlayerType.HUMAN, "Hans");
		Player playerTwo = new Player(PlayerType.HUMAN, "Peter");

		Battlefield battlefield = gameController.initBattlefield(GameType.IDIOT_PATIENCE, playerOne, null);
		assertNotNull(battlefield);
		assertEquals(battlefield.getPlayerOne().getName(), "Hans");
		assertEquals(battlefield.getGameType(), GameType.IDIOT_PATIENCE);
		for(CardStackType type : gameController.getCardStackTypes(GameType.IDIOT_PATIENCE))
		{
			assertNotNull(battlefield.getStack(type));
		}
		battlefield = gameController.initBattlefield(GameType.FREECELL, playerOne, null);
		assertNotNull(battlefield);
		assertEquals(battlefield.getPlayerOne().getName(), "Hans");
		assertEquals(battlefield.getGameType(), GameType.FREECELL);
		assertNotNull(battlefield);
		for(CardStackType type : gameController.getCardStackTypes(GameType.FREECELL))
		{
			assertNotNull(battlefield.getStack(type));
		}
		battlefield = gameController.initBattlefield(GameType.AGGRO_PATIENCE, playerOne, playerTwo);
		assertEquals(battlefield.getPlayerOne().getName(), "Hans");
		assertEquals(battlefield.getGameType(), GameType.AGGRO_PATIENCE);
		assertEquals(battlefield.getPlayerTwo().getName(), "Peter");
		for(CardStackType type : gameController.getCardStackTypes(GameType.AGGRO_PATIENCE))
		{
			assertNotNull(battlefield.getStack(type));
		}

	}
	/**
	 * Überpüft, ob die CardStackTypes ihren SpielModi zugeordnet wurden.
	 */
	@Test
	public void testGetCardStackTypes()
	{
		assertNotNull(gameController.getCardStackTypes(GameType.AGGRO_PATIENCE));
		assertNotNull(gameController.getCardStackTypes(GameType.IDIOT_PATIENCE));
		assertNotNull(gameController.getCardStackTypes(GameType.FREECELL));
	}

}
