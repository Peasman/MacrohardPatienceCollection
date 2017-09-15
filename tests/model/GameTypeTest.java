package model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Testet die Methoden des Enzms GameType
 * @author Moritz Ludolph
 *
 */
public class GameTypeTest {

	/**
	 * Testet die Methode toLocale()
	 */
	@Test
	public void testToLocale() {
		assertEquals(GameType.FREECELL.toLocale(), "FreeCell");
		assertEquals(GameType.AGGRO_PATIENCE.toLocale(), "Zank-Patience");
		assertEquals(GameType.IDIOT_PATIENCE.toLocale(), "Idioten-Patience");
	}

}
