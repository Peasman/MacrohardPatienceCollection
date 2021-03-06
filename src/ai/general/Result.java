package ai.general;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Stack;

/**
 * Stores result of a search, together with more information about 
 * dead-end states and board states that were visited multiple times.
 * 
 * @author George Heineman
 */
public class Result {
	// record stats for dead-ends.
	Hashtable<Integer,long[]> deadEnd;

	// record stats for how many 'repeated states' found, and at what level
	Hashtable<Integer,long[]> duplicated;
	
	// the move sequence.
	Stack<IMove> moveStack;
	   
	// Have we a solution?
	public final boolean success;
	
	/** A solution. */
	public Result (Stack<IMove> moveStack) {
		this.moveStack = moveStack;
		success = true;
	}
	
	/** 
	 * Record a failed attempt to find a solution. 
	 */
	public Result () {
		success = false;
	}
	
	/** 
	 * Combine all stacks into one move sequence. 
	 * <p>
	 * If no solution, then empty stack returned.
	 */
	public Stack<IMove> solution() {
		// no solution.
		if (moveStack == null) {
			return new Stack<IMove>();
		}
		
		return moveStack;
	}
	

	public void outputDeadEnds(java.io.PrintWriter printWriter) {
		printWriter.println("Dead Ends:" + deadEnd.size());
		for (Iterator<Integer> it = deadEnd.keySet().iterator(); it.hasNext(); ) {
			int key = it.next();
			printWriter.println(key + "," + deadEnd.get(key)[0]);
		}
	}
	
	public void outputnDuplicated(java.io.PrintWriter printWriter) {
		printWriter.println("\n\nDuplicated: " + duplicated.size());
		for (Iterator<Integer> iterator = duplicated.keySet().iterator(); iterator.hasNext(); ) {
			int key = iterator.next();
			printWriter.println(key + "," + duplicated.get(key)[0]);
		}
	}

	/** Update information regarding dead ends. */
	public void setDeadEndStates(Hashtable<Integer, long[]> deadEnd) {
		this.deadEnd = deadEnd;
	}

	/** Update information regarding duplicated states. */
	public void setDuplicatedStates(Hashtable<Integer, long[]> dup) {
		duplicated = dup;
	}
}
