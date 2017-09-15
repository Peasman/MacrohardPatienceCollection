package util;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import model.Battlefield;
import model.CardStackType;
import model.Player;
import model.PlayerType;

/**
 * Testet die Klasse BattlefieldFactory
 * 
 * @author Moritz Ludolph
 */
public class BattlefieldFactoryTest {
	private Player playerOne, playerTwo;

	/**
	 * Initialisiert alle benötigten Felder für die Tests
	 */
	@Before
	public void setUp() {
		playerOne = new Player(PlayerType.HUMAN, "Hans");
		playerTwo = new Player(PlayerType.HUMAN, "Peter");
	}

	/**
	 * Testet die Methode createIdiotField
	 */
	@Test
	public void testCreateIdiotField() {
		Battlefield battlefield = BattlefieldFactory.createIdiotField(playerOne, null);

		assertEquals(battlefield.getStack(CardStackType.TALON).size(), 52);
		assertEquals(battlefield.getStack(CardStackType.STACKER_1).size(), 0);
		assertEquals(battlefield.getStack(CardStackType.ROW_1).size(), 0);
		assertEquals(battlefield.getStack(CardStackType.ROW_2).size(), 0);
		assertEquals(battlefield.getStack(CardStackType.ROW_3).size(), 0);
		assertEquals(battlefield.getStack(CardStackType.ROW_4).size(), 0);
	}

	/**
	 * Testet die Methode createFreeCellField
	 */
	@Test
	public void testCreateFreeCellField() {
		Battlefield battlefield = BattlefieldFactory.createFreeCellField(playerOne, null);

		assertEquals(battlefield.getStack(CardStackType.ROW_1).size(), 7);
		assertEquals(battlefield.getStack(CardStackType.ROW_2).size(), 7);
		assertEquals(battlefield.getStack(CardStackType.ROW_3).size(), 7);
		assertEquals(battlefield.getStack(CardStackType.ROW_4).size(), 7);

		assertEquals(battlefield.getStack(CardStackType.ROW_5).size(), 6);
		assertEquals(battlefield.getStack(CardStackType.ROW_6).size(), 6);
		assertEquals(battlefield.getStack(CardStackType.ROW_7).size(), 6);
		assertEquals(battlefield.getStack(CardStackType.ROW_8).size(), 6);

		assertEquals(battlefield.getStack(CardStackType.STACKER_1).size(), 0);
		assertEquals(battlefield.getStack(CardStackType.STACKER_2).size(), 0);
		assertEquals(battlefield.getStack(CardStackType.STACKER_3).size(), 0);
		assertEquals(battlefield.getStack(CardStackType.STACKER_4).size(), 0);

		assertEquals(battlefield.getStack(CardStackType.FREECELL_1).size(), 0);
		assertEquals(battlefield.getStack(CardStackType.FREECELL_2).size(), 0);
		assertEquals(battlefield.getStack(CardStackType.FREECELL_3).size(), 0);
		assertEquals(battlefield.getStack(CardStackType.FREECELL_4).size(), 0);
	}

	/**
	 * Testet die Methode createAggroField() 
	 */
	@Test
	public void testCreateAggroField() {
		Battlefield battlefield = BattlefieldFactory.createAggroField(playerOne, playerTwo, null, null);

		assertEquals(battlefield.getStack(CardStackType.ZANK_MIDDLE_1).size(), 35);
		assertEquals(battlefield.getStack(CardStackType.ZANK_MIDDLE_1).getOwner(), playerOne);
		assertEquals(battlefield.getStack(CardStackType.ZANK_MIDDLE_2).size(), 35);
		assertEquals(battlefield.getStack(CardStackType.ZANK_MIDDLE_2).getOwner(), playerTwo);

		assertEquals(battlefield.getStack(CardStackType.ZANK_LEFT_1).size(), 0);
		assertEquals(battlefield.getStack(CardStackType.ZANK_LEFT_1).getOwner(), playerOne);
		assertEquals(battlefield.getStack(CardStackType.ZANK_LEFT_2).size(), 0);
		assertEquals(battlefield.getStack(CardStackType.ZANK_LEFT_2).getOwner(), playerTwo);

		assertEquals(battlefield.getStack(CardStackType.ZANK_RIGHT_1).size(), 13);
		assertEquals(battlefield.getStack(CardStackType.ZANK_RIGHT_1).getOwner(), playerOne);
		assertEquals(battlefield.getStack(CardStackType.ZANK_RIGHT_2).size(), 13);
		assertEquals(battlefield.getStack(CardStackType.ZANK_RIGHT_2).getOwner(), playerTwo);

	}

}
