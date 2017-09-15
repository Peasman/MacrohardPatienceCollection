package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.EnumMap;

import org.junit.Before;
import org.junit.Test;

import exceptions.InconsistentMoveException;
import model.GameCard.Rank;
import model.GameCard.Suit;
import model.GameMove.GameMoveParameter;
import model.GameMove.SingleCardGameMoveParameter;

/**
 * Testklasse zum @Battlefield
 * @author Alexander Herlez
 *
 */
public class BattlefieldTest {

	
/**
 * Das Testfeld
 */
private Battlefield freeCell;

/**
 * Die Teststapel 
 */
private EnumMap<CardStackType, CardStack> stackMap;

/**
 * Testspieler
 */
private Player pl1, pl2, easyBot, hardBot;

/**
 * Testkarten
 */
private GameCard ka5, heartAce, spades2, kr4, heart3;

/**
 * TestStapel
 */
private CardStack row1, row2, row3;

/**
 * absteigende Karten mit Farbwechsel, lassen sich in Freecell alle verschieben
 */
private ArrayList<GameCard> descCards;

	/**
	 * initialisiert die Testumgebung. Es werden Karten, Stapel und ein Battlefield erzeugt
	 */
	@Before
    public void setUp() {
		pl1 = new Player(PlayerType.HUMAN, "Alice");
		pl2 = new Player(PlayerType.HUMAN, "Bob");
		easyBot = new Player(PlayerType.AI1, "LarryBot");
		hardBot = new Player(PlayerType.AI3, "KruppstahlBot");
		
		//ein paar Karten
		heartAce = new GameCard(Suit.HEARTS, Rank.ACE, true);
		spades2 = new GameCard(Suit.SPADES, Rank.TWO, true);
		kr4 = new GameCard (Suit.CLUBS, Rank.FOUR, true);
		heart3 = new GameCard (Suit.HEARTS, Rank.THREE, true);
		ka5 = new GameCard (Suit.DIAMONDS, Rank.FIVE, true);
				
		//Eine ArrayList mit einem Stapel, der in Freecell verschiebar ist
		descCards = new ArrayList<>();
		descCards.add(ka5);
		descCards.add(kr4);
		descCards.add(heart3);
		descCards.add(spades2);

			
		//Die Reihen
		row1 = new CardStack(CardStackType.ROW_1);
		row1.pushAll(descCards);
		row2 = new CardStack(CardStackType.ROW_2);
		row2.push(heartAce);
		row3 = new CardStack(CardStackType.ROW_3);

		
		stackMap = new EnumMap<>(CardStackType.class);
		stackMap.put(row1.getType(), row1);
		stackMap.put(row2.getType(), row2);
		stackMap.put(row3.getType(), row3);
		freeCell = new Battlefield(GameType.FREECELL, stackMap, pl1, pl2);
    }
	
	/**
	 * testet den Konstruktor
	 */
	@Test
	public void testBattlefield() {
		freeCell = new Battlefield(GameType.FREECELL, stackMap, pl1, pl2);
		assertNotEquals (freeCell.getStack(CardStackType.ROW_1), null);
		CardStack firstRow= freeCell.getStack(CardStackType.ROW_1);
		assertTrue(firstRow.peek().sameCardAs(spades2));
		firstRow.pop();
		assertTrue(firstRow.peek().sameCardAs(heart3));
	}

	/**
	 * testet die getUpperCard Methode
	 */
	@Test
	public void testGetUpperCard() {
		assertTrue(freeCell.getUpperCard(CardStackType.ROW_1).sameCardAs(spades2));
		//die Karte sollte immernoch oben sein
		assertTrue(freeCell.getUpperCard(CardStackType.ROW_1).sameCardAs(spades2));
		assertEquals(freeCell.getUpperCard(CardStackType.ROW_3), null);
	}


	/**
	 * testet das entfernen einer Karte
	 */
	@Test
	public void testPopCard() {
		assertEquals(freeCell.popCard(CardStackType.ROW_1), spades2);
		assertEquals(freeCell.popCard(CardStackType.ROW_1), heart3);
		assertEquals(freeCell.popCard(CardStackType.ROW_1), kr4);
		assertEquals(freeCell.popCard(CardStackType.ROW_1), ka5);
		assertEquals(freeCell.popCard(CardStackType.ROW_1), null);
	}

	/**
	 * testet das hinzufuegen einer Karte
	 */
	@Test
	public void testPushCard() {
		//pusht auf Stapel, auf dem schon Karten liegen
		descCards.add(kr4);
		freeCell.pushCard(CardStackType.ROW_1, descCards);
		assertEquals(freeCell.popCard(CardStackType.ROW_1), kr4);
		freeCell.pushCard(CardStackType.ROW_3, descCards);
		assertEquals(freeCell.popCard(CardStackType.ROW_3), kr4);
	}


	/**
	 * testet das ausführen eines Zuges auf dem Battlefield
	 * @throws InconsistentMoveException falls ein unmöglicher GameMove durchgeführt werden soll
	 */
	@Test (expected = ArrayIndexOutOfBoundsException.class)
	public void testExecuteMove() throws InconsistentMoveException {
		GameMove testMove = new GameMove (new SingleCardGameMoveParameter(CardStackType.ROW_2, CardStackType.ROW_3, heartAce), easyBot, false);
		GameMove testMove2 = new GameMove (new GameMoveParameter(CardStackType.ROW_1, CardStackType.ROW_2, descCards), hardBot, false);
		try {
			freeCell.executeMove(testMove);
			assertEquals(freeCell.popCard(CardStackType.ROW_2), null);
			freeCell.executeMove(testMove2);
		} catch (InconsistentMoveException e) {
			//dieser GameMove muss 
			assertTrue("dieser GameMove muss konsistent sein", false);
		}
		assertEquals(freeCell.popCard(CardStackType.ROW_3), heartAce);
		assertEquals(freeCell.popCard(CardStackType.ROW_2), spades2);
		assertEquals(freeCell.popCard(CardStackType.ROW_2), heart3);
		assertEquals(freeCell.popCard(CardStackType.ROW_2), kr4);
		assertEquals(freeCell.popCard(CardStackType.ROW_2), ka5);
		
		//dieser GameMove gibt ist nun inkonsistent:
		freeCell.executeMove(testMove);
		
		
	}

}
