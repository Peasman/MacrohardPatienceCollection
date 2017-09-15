package ai.freecell;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import ai.general.GameState;
import ai.general.IGameState;
import ai.general.IMove;
import controller.GameController;
import controller.GameLogicController;
import exceptions.InconsistentMoveException;
import functionals.MoveFromRule;
import functionals.MoveToRule;
import model.Battlefield;
import model.GameCard;
import model.GameCard.Rank;
import model.GameCard.Suit;
import model.CardStackSuperType;
import model.CardStackType;
import model.GameType;
import model.GameMove;
import model.GameMove.GameMoveParameter;
import model.GameMove.SingleCardGameMoveParameter;
import model.SuperBattlefield;
import search.StackChain;
import util.MoveFromRuleSetProvider;
import util.MoveToRuleSetProvider;

public class FreecellGameState extends IGameState implements Serializable, Cloneable {

	private static final long serialVersionUID = 198996248646536401L;
	/**
	 * protected int score = 0; protected GameState gameState = null; protected
	 * StackChain<IMove> moveHistory = null;
	 */

	private ArrayList<CardStackType> rowList;
	private ArrayList<CardStackType> freeCellList;
	private ArrayList<CardStackType> homeCellList;
	
	public static EnumMap<CardStackSuperType, MoveFromRule> moveFromRules = MoveFromRuleSetProvider.getInstance().getMoveFromRuleSet(GameType.FREECELL);
	public static EnumMap<CardStackSuperType, MoveToRule> moveToRules = MoveToRuleSetProvider.getInstance().getMoveToRuleSet(GameType.FREECELL);

	public static EnumMap<Suit, EnumMap<Rank, Byte>> cardToByteMap = null;

	public static GameCard[] byteToCardArray = null;

	public FreecellGameState(SuperBattlefield battlefield) {
		super((GameState) battlefield);

		rowList = new ArrayList<>();
		rowList.add(CardStackType.ROW_1);
		rowList.add(CardStackType.ROW_2);
		rowList.add(CardStackType.ROW_3);
		rowList.add(CardStackType.ROW_4);
		rowList.add(CardStackType.ROW_5);
		rowList.add(CardStackType.ROW_6);
		rowList.add(CardStackType.ROW_7);
		rowList.add(CardStackType.ROW_8);

		freeCellList = new ArrayList<>();
		freeCellList.add(CardStackType.FREECELL_1);
		freeCellList.add(CardStackType.FREECELL_2);
		freeCellList.add(CardStackType.FREECELL_3);
		freeCellList.add(CardStackType.FREECELL_4);

		homeCellList = new ArrayList<>();
		homeCellList.add(CardStackType.STACKER_1);
		homeCellList.add(CardStackType.STACKER_2);
		homeCellList.add(CardStackType.STACKER_3);
		homeCellList.add(CardStackType.STACKER_4);

		cardToByteMap = new EnumMap<>(Suit.class);
		byteToCardArray = new GameCard[53];

		for (Suit suit : Suit.values()) {
			cardToByteMap.put(suit, new EnumMap<>(Rank.class));
			for (Rank rank : Rank.values()) {
				Byte index = new Byte(new Integer(suit.ordinal() * 13 + rank.ordinal() + 1).byteValue());
				cardToByteMap.get(suit).put(rank, index);
				byteToCardArray[index] = new GameCard(suit, rank, true);

				// System.out.println(index);
			}
		}
	}

	public Battlefield gameState() {
		return (Battlefield) super.gameState();
	}	

	@Override
	public FreecellGameState copy() {
		FreecellGameState gameState = null;

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(this);
			oos.flush();
			oos.close();
			bos.close();

			byte[] byteData = bos.toByteArray();

			ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
			ObjectInputStream ois = new ObjectInputStream(bais);
			gameState = (FreecellGameState) ois.readObject();
			bais.close();
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
//		FreeCellGameStateTest gameState = null;
//		try {
//			gameState = this.clone();
//		} catch (CloneNotSupportedException e) {
//			e.printStackTrace();
//		}

		return gameState;
	}
	
	@Override
	protected FreecellGameState clone() throws CloneNotSupportedException {
		FreecellGameState gameState = (FreecellGameState) super.clone();
		
//		Gson gson = new Gson();
//		Battlefield battlefield = gson.fromJson(gson.toJson(this.gameState()), Battlefield.class);
		
		Battlefield battlefield = gameState().createNewGameState(GameController.getInstance().getCardStackTypes(GameType.FREECELL));
		
		gameState.gameState = battlefield;
		gameState.score = this.score;
		if (this.moveHistory != null) {
			gameState.moveHistory = new StackChain<>(stackCopy(this.moveHistory.stack), this.moveHistory.predecessor);
		} else {
			gameState.moveHistory = null;
		}
		
		return gameState;
	}

	private Stack<IMove> stackCopy(Stack<IMove> existing) {
		Stack<IMove> aCopy = new Stack<IMove>();
		for (Iterator<IMove> it = existing.iterator(); it.hasNext();) {
			aCopy.push(it.next());
		}
		return aCopy;
	}
	
	@Override
	public Object key() {
		byte[] key = new byte[65];

		int index = 0;
		key[0] = 0;

		for (CardStackType freeCell : freeCellList) {
			GameCard card = gameState().getStack(freeCell).peek();
			if (card != null) {
				key[0] += cardToByteMap.get(card.getSuit()).get(card.getRank());
			}
		}

		index++;
		
		for (CardStackType homeCell : homeCellList) {
			GameCard card = gameState().getStack(homeCell).peek();
			if (card != null) {
				key[index++] = cardToByteMap.get(card.getSuit()).get(card.getRank());
			} else {
				key[index++] = (byte) 0;
			}
		}

		for (CardStackType row : rowList) {
			ArrayList<GameCard> cards = gameState().getStack(row).toList();
			for (GameCard card : cards) {
				if (card != null) {
					key[index++] = cardToByteMap.get(card.getSuit()).get(card.getRank());
				} else {
					key[index++] = (byte) 0;
				}
			}
			key[index++] = (byte) 255;
		}

		return key;
	}

	@Override
	public LinkedList<IMove> validMoves() {
		ArrayList<CardStackType> stacks = GameController.getInstance().getCardStackTypes(GameType.FREECELL);
		LinkedList<IMove> possibleMoves = new LinkedList<>();
		FreecellHeuristic heuristic = FreecellHeuristic.getInstance();
		if (heuristic.freeCells(this) >= heuristic.sequenceBrakes(this)) {
			Battlefield battlefield = gameState();
			// from ROW to FOUNDATION
			for (CardStackType row : rowList) {
				for (CardStackType homeCell : homeCellList) {
					GameCard card = battlefield.getStack(row).peek();
					if (card != null) {
						GameMove move = new GameMove(new SingleCardGameMoveParameter(row, homeCell, card), null, false);
						if (GameLogicController.isValid(battlefield, move)) {
							possibleMoves.add(new IMove(move));
						}
					}
				}
			}
			// from FREECELL to FOUNDATION
			for (CardStackType freeCell : freeCellList) {
				for (CardStackType homeCell : homeCellList) {
					GameCard card = battlefield.getStack(freeCell).peek();
					if (card != null) {
						GameMove move = new GameMove(new SingleCardGameMoveParameter(freeCell, homeCell, card), null, false);
						if (moveToRules.get(homeCell.getSuperType()).isValid(battlefield, move)) {
							possibleMoves.add(new IMove(new GameMove(new SingleCardGameMoveParameter(freeCell, homeCell, card), null, false)));
						}
					}
				}
			}
		} 
		
		validMovesHelp(stacks, possibleMoves);
		return possibleMoves;
	}

	private void validMovesHelp(ArrayList<CardStackType> stacks, LinkedList<IMove> possibleMoves) {
		if (possibleMoves.size() == 0) {
			for (CardStackType stack : stacks) {
				for (CardStackType otherStack : stacks) {
					if (stack.getSuperType().equals(CardStackSuperType.FREECELL)
							&& stack.getSuperType().equals(otherStack.getSuperType())) {
						continue;
					}
					ArrayList<GameCard> cardsToMove = new ArrayList<GameCard>();
					GameCard upperCard = gameState().getUpperCard(stack);
					if (upperCard == null) {
						continue;
					}
					ArrayList<GameCard> cardsInStack = gameState().getStack(stack).toList();
					if (cardsInStack.size() == 0)
						continue;
					for (int i = cardsInStack.size() - 1; i >= 0; i--) {
						cardsToMove.add(0, cardsInStack.get(i));
						GameMove multipleMove = new GameMove(new GameMoveParameter(stack, otherStack, new ArrayList<GameCard>(cardsToMove)), null, false);
						if (GameLogicController.isValid(gameState(), multipleMove)) {
							possibleMoves.add(new IMove(multipleMove));
						}
					}
				}
			}
		}
	}
	
	

	// private ArrayList<Move> fromRows (Battlefield battlefield) {
	// ArrayList<Move> possibleMove = new ArrayList<>();
	//
	// for (CardStackType row : rowList) {
	// ArrayList<Card> stack = battlefield.getStack(row).toList();
	//
	// DoubleLinkedList<Card> linkedList = new DoubleLinkedList<>();
	// linkedList.
	//
	// if (stack.size() > 0) {
	//
	// stack.i
	// }
	// }
	//
	//
	// return null;
	// }
/*
	private ArrayList<Move> toRows(Battlefield battlefield, CardStackType fromRow, ArrayList<Card> cards) {
		ArrayList<Move> possibleMoves = new ArrayList<>();

		for (CardStackType row : rowList) {
			if (!row.equals(fromRow)) {
				CardStack stack = battlefield.getStack(row);
				if (stack.size() == 0 || (stack.peek().getSuit().equals(cards.get(0).getSuit())
						&& stack.peek().getRank().getRankValue() - cards.get(0).getRank().getRankValue() == 1)) {
					possibleMoves.add(new Move(fromRow, row, cards, null, false));
				}
			}
		}

		return possibleMoves;
	}
*/
	@Override
	public boolean execute(IMove move) {
		try {
			gameState().executeMove(move.getMoves().get(0));
			setLastMove(move);
		} catch (InconsistentMoveException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean undo(IMove move) {
		try {
			gameState().executeMove(reverse(move.getMoves().get(0)));
		} catch (InconsistentMoveException e) {
			e.printStackTrace();
			System.out.println("########################### bei " + move.getMoves().get(0).getCards().get(0).toProtocol());
			return false;
		}

		return true;
	}

	private GameMove reverse(GameMove move) {
		return new GameMove(new GameMoveParameter(move.getTo(), move.getFrom(), move.getCards()), move.getPlayer(), move.getFlip());
	}

}

