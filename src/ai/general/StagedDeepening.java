package ai.general;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;
import java.util.TreeMap;

import controller.MPCController;
import search.StackChain;

/**
 * 
 * @author simon
 *
 * @param <K>
 *            key fuer jede GameState<S, M>
 * @param <S>
 *            gameState
 * @param <M>
 *            move
 */
public abstract class StagedDeepening<K, V extends IGameState> {

	public MPCController mpcController = null;
	
	public V bestState = null;
	
	/**
	 * Enthaelt spaetre eine Liste von Moves als value und einen zugehoerigen key
	 */
	/** Current state of search. */
	protected V currentNode;

	/** Previous states that have been visited. Key=currentNode key. */
	protected TreeMap<K, Integer> nodeArchieve;

	/**
	 * Future boards to be searched are inserted into balanced tree according to
	 * their evaluation.
	 */
	protected TreeMap<V, Integer> newlyAddedNodes;
	
	private TreeMap<V, Integer> boardStates;

	private final int MAX_SIZE = 200000;

	/** Provides a bridge to the earlier search pass. */
	private StackChain<IMove> prevMoveChain = null;

	/** Stack of moves from initial board to current state. */
	private Stack<IMove> moveStack;

	protected int searchDepth = 7;

	protected Evaluator<V> evaluator;
	
	protected IVisitor visitor;
	
	protected Comparator<V> gameStateComparator = new Comparator<V>() {
		
		@SuppressWarnings("unchecked")
		@Override
		public int compare(V toInsert, V pivotState) {
			int diff = toInsert.score - pivotState.score;
			//System.out.println(diff);
			if (diff != 0) {
				return diff;
			} else {
				return archieveComparator.compare((K) toInsert.key(), (K) pivotState.key());
			}
		}
	};
	
	protected Comparator<K> archieveComparator;

	private int visited = 0;

	String[] cardCodes = { " - ", "H01", "H02", "H03", "H04", "H05", "H06", "H07", "H08", "H09", "H10", "H11", "H12",
			"H13", "D01", "D02", "D03", "D04", "D05", "D06", "D07", "D08", "D09", "D10", "D11", "D12", "D13", "C01",
			"C02", "C03", "C04", "C05", "C06", "C07", "C08", "C09", "C10", "C11", "C12", "C13", "S01", "S02", "S03",
			"S04", "S05", "S06", "S07", "S08", "S09", "S10", "S11", "S12", "S13" };

	public StagedDeepening(IVisitor visitor, Evaluator<V> evaluator, Comparator<K> archieveComparator) {
		this.visitor = visitor;
		this.evaluator = evaluator;
		this.archieveComparator = archieveComparator;

		nodeArchieve = null;
		newlyAddedNodes = null;
	}


	public abstract boolean searchComplete(V gameState);
	
	protected abstract boolean earlyFinish(V gameState);

	public Stack<IMove> computeSolution(V gameState) {
		Stack<IMove> res = new Stack<IMove>();
		
		V aCopy = (V) gameState.copy();
		
		StackChain<IMove> moveHistory = aCopy.getMoveHistory();
		if (moveHistory == null) {
			return res;
		}
		
		if (aCopy == null || moveHistory == null) System.out.println("gameState == null 2");

		// must reverse the chain
		Stack<StackChain<IMove>> chainStack = new Stack<StackChain<IMove>>();
		while (moveHistory != null) {
			chainStack.push(moveHistory);
			moveHistory = moveHistory.predecessor;
		}

		while (!chainStack.isEmpty()) {
			moveHistory = chainStack.pop();
			for (Iterator<IMove> it = moveHistory.stack.iterator(); it.hasNext(); ) {
				res.push(it.next());
			}
		}
		return res;
	}

	@SuppressWarnings("unchecked")
	public Result fullSearch(V start) {
		currentNode = (V) start.copy();
		
		visited = 0;
		nodeArchieve = new TreeMap<>(archieveComparator);
		moveStack = new Stack<IMove>();
		boardStates = new TreeMap<>(gameStateComparator);
		newlyAddedNodes = new TreeMap<>(gameStateComparator);
		currentNode.score = evaluator.evaluate((V) currentNode);
		currentNode.moveHistory = null;
		
		boardStates.put((V) currentNode.copy(), evaluator.evaluate(currentNode));
		bestState = (V) currentNode.copy();
		bestState.score = evaluator.evaluate(bestState);
		
		//int passNumber = 0;
		int lastStackChainID;
		while (boardStates.size() > 0) {
//			//passNumber++;
			currentNode = boardStates.firstKey();
			boardStates.remove(currentNode);
			
			prevMoveChain = currentNode.getMoveHistory();
			moveStack = new Stack<IMove>();

			StackChain<IMove> chain = new StackChain<>(stackCopy(moveStack), prevMoveChain);
			currentNode.setMoveHistory(chain);
			
			if (prevMoveChain == null) {
				lastStackChainID = 0;
			} else {
				lastStackChainID = prevMoveChain.lastID;
			}
			
			
			if (search(lastStackChainID, 0)) {
				System.out.println("lasdkgjojasdfhöldaskfnögdaslknöalf");
				chain = new StackChain<>(stackCopy(moveStack), chain);
				currentNode.setMoveHistory(chain);
				
				if (currentNode == null 
						|| currentNode.moveHistory == null 
						|| currentNode.gameState == null) {
					System.out.println("lasdkgjojasdfhöldaskfnögdaslknöalf");
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				System.out.println("++++++++++++++++++++++++++");
				
				moveStack = computeSolution(currentNode);
				
				System.out.println("++++++++++++++++++++++++++");
				
				Result res = new Result(moveStack);
				return res;
			}

			boardStates = newlyAddedNodes;
			
			if (boardStates.size() > 4000+0) {
				while (boardStates.size() > 2000+0) {
					boardStates.remove(boardStates.lastKey());
				}
				System.out.println("Garbagecollection bei : " + visited);
				System.gc();
			}
		}

		Result res = new Result(computeSolution(bestState));
		return res;
	}

	/**
	 * Recursive routine to continue the search at the given depth.
	 * 
	 * @param depth
	 */
	@SuppressWarnings("unchecked")
	private boolean search(final int nodeId, int depth) {
		if (searchComplete(currentNode)/* || visited == 600000*/) {
			return true;
		} else if (earlyFinish(currentNode)) {
			return false;
		}
		
		if (depth > searchDepth) {
			if (nodeArchieve.size() > MAX_SIZE) {
				System.err.println("Emptying prevComputedcurrentNodes set:" + nodeArchieve.size() + " entries...");
				nodeArchieve.clear();
				System.gc();
			}

			nodeArchieve.put((K) currentNode.key(), visited);
			int score = evaluator.evaluate((V) currentNode);
			
			//System.out.println(score + " | | | | | | | | | | | | | | | | | " + visited);

			V aCopy = (V) currentNode.copy();
			aCopy.setMoveHistory(new StackChain<>(stackCopy(moveStack), prevMoveChain));
			aCopy.score = score;
			newlyAddedNodes.put((V) aCopy, score);
			
			if (aCopy.moveHistory.equals(currentNode.moveHistory) 
					|| aCopy.gameState.equals(currentNode.gameState) 
					|| currentNode == null 
					|| aCopy == null 
					|| aCopy.moveHistory == null 
					|| aCopy.gameState == null) {
				System.out.println("lasdkgjojasdfhöldaskfnögdaslknöalf");
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if (bestState == null || bestState.score > score) {
				bestState = (V) currentNode.copy();
				bestState.setMoveHistory(new StackChain<>(stackCopy(moveStack), prevMoveChain));
				bestState.score = score;
			}
			
			if (boardStates.size() > 4000+0) {
				while (boardStates.size() > 2000+0) {
					boardStates.remove(boardStates.lastKey());
				}
				System.out.println("Garbagecollection bei : " + visited+ " Score: " + score + ", Best Score: " + bestState.score);

				System.gc();
			}
			
			return false; // maximale Suchtiefe ueberschritten - Zeit zum Bewerten der Spielsituationen
		}

		return evaluateCalculatedNodes(nodeId, depth);
	}


	@SuppressWarnings("unchecked")
	private boolean evaluateCalculatedNodes(final int nodeId, int depth) {
		LinkedList<IMove> validMoves = currentNode.validMoves();
		Iterator<IMove> iterator = validMoves.iterator();
		
		if (visited % 50000 == 0)
			System.out.println("unique boards analysed: " + visited);

		while (iterator.hasNext()) {
			IMove iMove = iterator.next();
			currentNode.execute(iMove);
			K key = (K) currentNode.key();
			Integer exist = nodeArchieve.get(key);
			
			if (exist == null) {
				visited++;
				visitor.visitNode(currentNode, visited);
				visitor.visitEdge(nodeId, visited);

				if (nodeArchieve.size() > MAX_SIZE) {
					System.err.println("Emptying nodeArchieve set:" + nodeArchieve.size() + " entries...");
					nodeArchieve.clear();
					System.gc();
				}

				nodeArchieve.put(key, visited);
				moveStack.push(iMove);
				if (search(visited, depth + 1)) {
					return true;
				}

				moveStack.pop();
			}
			currentNode.undo(iMove);
		}
		
		return false;
	}

	private Stack<IMove> stackCopy(Stack<IMove> existing) {
		Stack<IMove> aCopy = new Stack<IMove>();
		for (Iterator<IMove> it = existing.iterator(); it.hasNext(); ) {
			aCopy.push(it.next());
		}
		return aCopy;
	}
/*
	private void showBoard() {
		byte[][] field = new byte[9][15];

		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 15; j++)
				field[i][j] = 0;

		byte[] keyCode = (byte[]) currentNode.key();

		int position = 1;

		int j = 0;

		for (int i = 0; i < keyCode.length; i++) {
			if (i < 8)
				field[0][i] = keyCode[i];
			else if (keyCode[i] == (byte) 255) {
				if (position < 8)
					position++;
				j = 0;
			} else
				field[position][j++] = keyCode[i];
		}

		System.out.println("---------------------------------");

		for (int i = 0; i < 8; i++)
			System.out.print(" " + cardCodes[field[0][i]]);

		System.out.println("\n---------------------------------");

		for (int i = 0; i < 15; i++) {
			for (int k = 1; k < 9; k++) {
				System.out.print(" " + cardCodes[field[k][i]]);
			}
			System.out.println();
		}

		System.out.println("---------------------------------");
	}
	*/
/*
	private void printMove(Move move) {
		Card toCard = ((Battlefield) currentNode.gameState()).getStack(move.getTo()).peek();
		int counter = 1;

		if (toCard == null) {
			System.out.println(counter++ + "..  " + move.getCards().size() + " FROM: " + move.getFrom().name()
					+ "   TO: " + move.getTo().name() + ", empy stack" + "   UPPER CARD: "
					+ move.getCards().get(0).getSuit().name() + ", " + move.getCards().get(0).getRank().name());
		} else {
			System.out.println(counter++ + "..  " + move.getCards().size() + " FROM: " + move.getFrom().name()
					+ "   TO: " + move.getTo().name() + ", " + toCard.getSuit().name() + ", " + toCard.getRank().name()
					+ "   UPPER CARD: " + move.getCards().get(0).getSuit().name() + ", "
					+ move.getCards().get(0).getRank().name());
		}
	}
	*/
}

