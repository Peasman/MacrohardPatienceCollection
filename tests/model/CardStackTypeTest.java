package model;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Testet die Methoden des Enums CardStacKType
 * @author Moritz Ludolph
 *
 */
public class CardStackTypeTest {
	/**
	 * Testet die Methode toProtocol()
	 */
	@Test
	public void testToProtocol() {
		assertEquals(CardStackType.ZANK_LEFT_1.toProtocol(), "B6");
		assertEquals(CardStackType.ZANK_MIDDLE_2.toProtocol(), "B1");
	}

	/**
	 * Testet die Methode fromProtocol(String)
	 */
	@Test
	public void testFromProtocol() {
		assertEquals(CardStackType.fromProtocol("A2"), CardStackType.ROW_1);
		assertEquals(CardStackType.fromProtocol("B2"), CardStackType.STACKER_1);
	}

}
