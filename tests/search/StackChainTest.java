package search;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Iterator;
import java.util.Stack;

import org.junit.Before;
import org.junit.Test;

/**
 * Testklasse zur @StackChain
 * @author Simon Kurz
 *
 */
public class StackChainTest {

	/**
	 * Test-StackCain ohne Vorgaenger und 30 aufsteigend sortierten Elementen mit einem Wert von 0 bis 9 im Stack
	 */
	private StackChain<Integer> stackChainOne;
	
	/**
	 * Test-StackCain mit 'stackChainOne' als Vorgaenger und 20 aufsteigend sortierten Elementen mit einem Wert von 0 bis 19 im Stack
	 */
	private StackChain<Integer> stackChainTwo;
	
	/**
	 * Test-StackCain mit 'stackChainTwo' als Vorgaenger und 30 aufsteigend sortierten Elementen mit einem Wert von 0 bis 29 im Stack
	 */
	private StackChain<Integer> stackChainThree;
	
	/**
	 * Test-StackCain mit 'stackChainThree' als Vorgaenger und 40 aufsteigend sortierten Elementen mit einem Wert von 0 bis 39 im Stack
	 */
	private StackChain<Integer> stackChainFour;
	
	/**
	 * Dient zum erzeugen der Test-StackCains
	 */
	private Stack<Integer> integerStack;
	
	/**
	 * Initialisiert die Test-StackChains
	 */
	@Before
	public void setUp() {
		integerStack = new Stack<>();
		for (int i = 0; i < 10; i++) {
			integerStack.push(i);
		}
		stackChainOne = new StackChain<>(stackCopy(integerStack));
		
		for (int i = 10; i < 20; i++) {
			integerStack.push(i);
		}
		stackChainTwo = new StackChain<>(stackCopy(integerStack), stackChainOne, 0);
		
		for (int i = 20; i < 30; i++) {
			integerStack.push(i);
		}
		stackChainThree = new StackChain<>(stackCopy(integerStack), stackChainTwo, 0);
		
		for (int i = 30; i < 40; i++) {
			integerStack.push(i);
		}
		stackChainFour = new StackChain<>(stackCopy(integerStack), stackChainThree, 0);
	}
	
	/**
	 * Testet, ob die die uebergebenen Parameter in den Konstruktoren richtig gesetzt wurden
	 */
	@Test
	public void testConstructors() {
		assertNull(stackChainOne.getPredecessor());
		
		assertEquals(stackChainOne, stackChainTwo.getPredecessor());
		assertEquals(stackChainTwo, stackChainThree.getPredecessor());
		assertEquals(stackChainThree, stackChainFour.getPredecessor());
		
		assertArrayEquals(integerStack.subList(0, 10).toArray(), stackChainOne.getStack().toArray());
		assertArrayEquals(integerStack.subList(0, 20).toArray(), stackChainTwo.getStack().toArray());
		assertArrayEquals(integerStack.subList(0, 30).toArray(), stackChainThree.getStack().toArray());
		assertArrayEquals(integerStack.subList(0, 40).toArray(), stackChainFour.getStack().toArray());
	}

	/**
	 * Testet die Funktionalitaet der rekursiv arbeitenden Methode 'getTotalStackElements()'
	 */
	@Test (expected = NullPointerException.class)
	public void testGetTotalStackElements() {
		assertEquals(10, stackChainOne.getTotalStackElements());
		assertEquals(60, stackChainThree.getTotalStackElements());
		
		StackChain<Integer> stackChainFife = new StackChain<>(new Stack<>(), stackChainFour, 0);
		
		assertEquals(100, stackChainFife.getTotalStackElements());
		
		StackChain<Integer> stackChainSix = new StackChain<>(stackCopy(integerStack), stackChainTwo, 0);
		
		assertEquals(70, stackChainSix.getTotalStackElements());
		
		StackChain<Integer> stackChainSeven = new StackChain<>(new Stack<>(), null, 0);
		
		assertEquals(0, stackChainSeven.getTotalStackElements());
		
		StackChain<Integer> stackChainEight = new StackChain<>(null, null, 0);
		
		// hier wird nun eine NullPointerException geworfen
		assertEquals(0, stackChainEight.getTotalStackElements());
	}
	
	/**
	 * eine Helpermethode zum Kopieren eines Stacks von Integer Werten - wird in der Methode 'setUp()' zum Initialisieren der Test-StackChains verwendet
	 * @param existing der zu kopierende Stack von Integer Werten
	 * @return eine Kopie des Stacks 'existing'
	 */
	private Stack<Integer> stackCopy(Stack<Integer> existing) {
		Stack<Integer> aCopy = new Stack<Integer>();
		for (Iterator<Integer> it = existing.iterator(); it.hasNext(); ) {
			aCopy.push(it.next());
		}
		return aCopy;
	}
	
}
