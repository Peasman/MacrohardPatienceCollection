package controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import model.GameType;
import model.Player;
import model.PlayerType;

/**
 * Klasse zum testen des MPCControlelrs
 * @author Friedemann Runte
 *
 */
public class MPCControllerTest {
	MPCController mpcController;

	/**
	 * Erzeugt einen MPCController und gibt diesem ein FREECELL Battlefield.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		mpcController = new MPCController();	
		mpcController.setBattlefield(mpcController.getGameController().initBattlefield(GameType.FREECELL,new Player(PlayerType.HUMAN,"Test"),null));
	}

	/**
	 * Überprüft ob der GameLogicController initialisiert wird.
	 */
	@Test
	public void getGameLogicControllerTest()
	{
		assertNotNull(mpcController.getGameLogicController());
	}
	/**
	 * Überprüft ob der AIControllerTest initialisiert wird.
	 */
	@Test 
	public void getAIControllerTest()
	{
		assertNotNull(mpcController.getAIController());
	}
	/**
	 * Überprüft ob das Battlefield gesetzt wird.
	 */
	@Test
	public void setBattlefieldTest()
	{
		mpcController.setBattlefield(mpcController.getGameController().initBattlefield(GameType.IDIOT_PATIENCE,null,null));
		assertEquals(GameType.IDIOT_PATIENCE,mpcController.getBattlefield().getGameType());
	}
	/**
	 * Überprüft ob das Battlefield initialisiert wird.
	 */
	@Test
	public void getBattlefieldTest()
	{
		assertNotNull(mpcController.getBattlefield());
	}
	/**
	 * Überprüft ob der StatisticsController initialisiert wird.
	 */
	@Test
	public void getStatisticsControllerTest()
	{
		assertNotNull(mpcController.getStatisticsController());
	}
	/**
	 * Überprüft ob der GameController initialisiert wird.
	 */
	@Test
	public void getGameControllerTest()
	{
		assertNotNull(mpcController.getGameController());
	}
	/**
	 * Überprüft ob der ServerController initialisiert wird.
	 */
	@Test
	public void getServerControllerTest() {
		assertNotNull(mpcController.getServerController());
	}
	/**
	 * Überprüft ob der StatisticsController gesetzt wird.
	 */
	@Test
	public void setServerControllerTest()
	{
		ServerController server = new ServerController(mpcController);
		mpcController.setServerController(server);
		assertEquals(server, mpcController.getServerController());
	}
	/**
	 * Überprüft, ob ein Battlefield gespeichert und geladen werden kann.
	 */
	@Test
	public void loadBattlefieldTest()
	{
		try {
			IOController.saveGame(mpcController.getBattlefield(), IOController.getProperty("FREECELL_PATH"));
		} catch (IOException e) {
			fail();
			e.printStackTrace();
		}
		mpcController.loadBattlefield(IOController.getProperty("FREECELL_PATH"));
		assertEquals(mpcController.getBattlefield().getPlayerOne().getName(), "Test");
		
	}
	
	

}
