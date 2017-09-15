package search;

import java.io.Serializable;
import java.util.Stack;

/** Klasse, die eine Kette aus Stacks ist mit
 * @author 
 *
 * @param <V> Typ der Elemente, die die Stacks beinhalten
 */
public class StackChain<V> implements Serializable {
	private static final long serialVersionUID = -8902050160973658330L;

	/** What are our sequence of stack? */
	public final Stack<V> stack;
	
	/** When our stack above runs out, which is the next chain sequence? */
	public final StackChain<V> predecessor;
	
	/** And what is the boardID at the end of these stack? */
	public final int lastID;
	
	/** Anzahl an Elementen im Stack */
	private int totalStackElements = -1;
	
	/**
	 * @param stack
	 * @param previous
	 * @param lastID
	 */
	public StackChain (Stack<V> stack, StackChain<V> previous, int lastID) {
		this.stack = stack;
		this.predecessor = previous;
		this.lastID = lastID;
	}
	
	/**
	 * @param stack
	 * @param previous
	 */
	public StackChain (Stack<V> stack, StackChain<V> previous) {
		this.stack = stack;
		this.predecessor = previous;
		this.lastID = 0;
	}
	
	/**
	 * @param stack
	 */
	public StackChain (Stack<V> stack) {
		this.predecessor = null;
		this.stack = stack;
		this.lastID = 0;
	}
	
	/**
	 * @param stack
	 * @param lastID
	 */
	public StackChain (Stack<V> stack, int lastID) {
		this.predecessor = null;
		this.stack = stack;
		this.lastID = lastID;
	}

	/**
	 * @return
	 */
	public Stack<V> getStack() {
		return stack;
	}
	
	/** gibt die vorgänger StackChain zurück
	 * @return die vorgänger StackChain
	 */
	public StackChain<V> getPredecessor() {
		return predecessor;
	}


	public int getLastID() {
		return lastID;
	}
	
	/** berechnet die Anzahl an Elementen in allen Stacks neu und gibt sie zurück
	 * @return aktuelle Anzahl an Elementen in allen Stacks in der Chain
	 */
	public int getTotalStackElements() {
		if (totalStackElements < 0) {
			if (predecessor == null) {
				totalStackElements = stack.size();
			} else {
				totalStackElements = stack.size() + predecessor.getTotalStackElements();
			}
		}
		return totalStackElements;
	}

}
