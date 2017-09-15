package controller;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.EnumMap;
import org.junit.*;
import model.*;
import model.GameCard.*;

/**
 * Testet die Speicher und Ladefunktionalität des IOControllers
 * @author Miran Shefketi
 */
public class IOControllerTest {
	
	
	/**
	 * Testet, ob Eigenschaften richtig gespeichert und geladen werden, 
	 * indem es zwei Einträge macht, diese speichert, überschreibt, und wieder lädt.
	 */
	@Test
	public void testSaveLoadProperties() {
		IOController.setProperty("Test1", "testResult1");
		IOController.setProperty("coins", "whatever");
		IOController.saveProperties();
		IOController.setProperty("Test1", "whrh");
		IOController.setProperty("coins", "whatwhrhwever");
		IOController.loadProperties();
		assertTrue(IOController.getProperty("Test1").equals("testResult1"));
		assertTrue(IOController.getProperty("coins").equals("whatever"));
	}
	
	/**
	 * Testet, ob ein GameStatistics-Objekt richtig gespeichert und geladen wird, 
	 * indem es GameStatistics-Objekt anlegt, dieses mit Werten füllt, es Speichert 
	 * und anschließend in eine Kopie lädt und auf Gleichheit prüft.
	 */
	@Test
	public void testSaveLoadStats() {
		final String testPath = "./tests/testStats.test";
		GameStatistics testStats = new GameStatistics();
		testStats.setAggroPatienceAi1CardsLeftAvrg(58.346);
		testStats.setAggroPatienceGamesAi1(5362D);
		testStats.setFreecellFastestWin(2983752975275L);
		try {
			IOController.saveStats(testStats, testPath);
		} catch (IOException e) {
			fail();
		}
		try {
			GameStatistics compareStats = IOController.loadStats(testPath);
			assertTrue(compareStats.equals(testStats));
		} catch (IOException e) {
			fail();
		}
		//TODO: entweder GameStats Gottlos machen oder einzelne Checks implementieren
	}
	
	/**
	 * Testet, ob ein Battlefield-Objekt richtig gespeichert und geladen wird, 
	 * indem es Battlefield-Objekt anlegt, dieses mit Werten füllt, es Speichert 
	 * und anschließend in eine Kopie lädt und auf Gleichheit prüft.
	 */
	@Test
	public void testSaveLoadGame() {
		final String testPath = "./tests/testSave.testField";
		EnumMap<CardStackType, CardStack> stackMap = new EnumMap<>(CardStackType.class);
		CardStack testStack = new CardStack(CardStackType.ROW_1);
		GameCard testCard = new GameCard(Suit.HEARTS, Rank.ACE, true);
		testStack.push(testCard);
		stackMap.put(testStack.getType(), testStack);
		Player testPlayer = new Player(PlayerType.HUMAN, "Test");
		Battlefield testBattlefield = new Battlefield(GameType.IDIOT_PATIENCE, stackMap, testPlayer, null);
		Battlefield testBattlefield2;
		try {
			IOController.saveGame(testBattlefield, testPath);
		} catch (IOException e) {
			fail();
		}
		try {
			testBattlefield2 = IOController.loadGame(testPath);
			assertEquals(testBattlefield2.getCurrentPlayer().getType(), PlayerType.HUMAN);
			assertEquals(testBattlefield2.getCurrentPlayer().getName(), "Test");
			assertEquals(testBattlefield2.getStack(CardStackType.ROW_1).peek(), testCard);
		} catch (IOException e) {
			fail();
		}
	}

}
