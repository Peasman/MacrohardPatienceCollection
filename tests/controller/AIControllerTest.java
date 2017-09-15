package controller;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.Battlefield;
import model.GameType;
import model.GameMove;
import model.Player;
import model.PlayerType;

/**
 * Testet die AI Klasse @AIController
 * @author Alexander Herlez
 *
 */
public class AIControllerTest {

	/**
	 * der Hauptcontroller
	 */
	MPCController mpcCon;
	
	
	/**
	 * der AIController der getestet wird
	 */
	AIController aiCon;
	
	
	/**
	 * initialisiert den Test
	 */
	@Before
	public void setUp() {
		mpcCon = new MPCController();
		Player pOne = new Player(PlayerType.HUMAN, "Siggi");
		mpcCon.setBattlefield(mpcCon.getGameController().initBattlefield(GameType.FREECELL, pOne ,pOne));
	}

	/**
	 * testet den Konstruktor
	 */
	@Test
	public void testAIController() {
		aiCon = new AIController(mpcCon);
		assertEquals(mpcCon, aiCon.getMpcController());
	}

	/**
	 * testet die getNextMove Methode
	 */
	@Test
	public void testGetNextMove() {
		MPCController testMPC = new MPCController();
		Player testPlayer = new Player(PlayerType.AI1, "Hans");
		
		Battlefield testIdiot = testMPC.getGameController().initBattlefield(GameType.IDIOT_PATIENCE, testPlayer, testPlayer);				
		testMPC.setBattlefield(testIdiot);
		aiCon = testMPC.getAIController();
		GameMove nextMove = aiCon.getNextMove(testIdiot);
		assertNotEquals(null, nextMove);
	}
		
		
	/**
	 * testet ob der Start der AI einen Fehler wirft
	 */
	@Test
	public void testStartAI() {
		aiCon = new AIController(mpcCon);
		aiCon.startAI(true, false);
	}
}
