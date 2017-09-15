package ai.idiotpatience;

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

public class IdiotPatienceGameState extends IGameState implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3700751047244596586L;
	/**
	 * protected int score = 0; protected GameState gameState = null; protected
	 * StackChain<IMove> moveHistory = null;
	 */

	private CardStackType talon;
	private ArrayList<CardStackType> rowList;
	private CardStackType stacker;

	public static EnumMap<CardStackSuperType, MoveFromRule> moveFromRules = MoveFromRuleSetProvider.getInstance()
			.getMoveFromRuleSet(GameType.IDIOT_PATIENCE);
	public static EnumMap<CardStackSuperType, MoveToRule> moveToRules = MoveToRuleSetProvider.getInstance()
			.getMoveToRuleSet(GameType.IDIOT_PATIENCE);

	public static EnumMap<Suit, EnumMap<Rank, Byte>> cardToByteMap = null;

	public static GameCard[] byteToCardArray = null;

	public IdiotPatienceGameState(SuperBattlefield battlefield) {
		super((GameState) battlefield);

		rowList = new ArrayList<>();
		rowList.add(CardStackType.ROW_1);
		rowList.add(CardStackType.ROW_2);
		rowList.add(CardStackType.ROW_3);
		rowList.add(CardStackType.ROW_4);

		talon = CardStackType.TALON;

		stacker = CardStackType.STACKER_1;

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
	public IdiotPatienceGameState copy() {
		IdiotPatienceGameState gameState = null;

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
			gameState = (IdiotPatienceGameState) ois.readObject();
			bais.close();
			ois.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return gameState;
	}

	@Override
	protected IdiotPatienceGameState clone() throws CloneNotSupportedException {
		IdiotPatienceGameState gameState = (IdiotPatienceGameState) super.clone();

		Battlefield battlefield = gameState()
				.createNewGameState(GameController.getInstance().getCardStackTypes(GameType.FREECELL));

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
		byte[] key = new byte[11];

		int index = 0;
		key[0] = (byte) gameState().getStack(talon).size();
		index = 1;
		for (CardStackType row : rowList) {
			GameCard card = gameState().getStack(row).peek();
			if (card != null) {
				key[index] = cardToByteMap.get(card.getSuit()).get(card.getRank());
			} else {
				key[index] = (byte) 0;
			}

			index++;
		}
		key[6] = (byte) gameState().getStack(stacker).size();
		index = 7;
		for (CardStackType row : rowList) {
			key[index] = (byte) gameState().getStack(row).size();
			index++;
		}

		return key;
	}

	@Override
	public LinkedList<IMove> validMoves() {

		ArrayList<CardStackType> stacks = GameController.getInstance().getCardStackTypes(GameType.IDIOT_PATIENCE);

		LinkedList<IMove> possibleMoves = new LinkedList<>();

		for (CardStackType row : rowList) {
			for (CardStackType other : stacks) {
				GameCard upperCard = gameState().getUpperCard(row);
				if (upperCard == null)
					continue;

				GameMove move = new GameMove(new SingleCardGameMoveParameter(row, other, upperCard), null, false, (int) System.currentTimeMillis());
				if (GameLogicController.isValid(gameState(), move)) {
					possibleMoves.add(new IMove(move));
				}

			}
		}
		//
		// if (gameState().getStack(talon).size() > 0) {
		// IMove talonMove = new IMove();
		// int index = gameState().getStack(talon).size() - 1;
		// for (CardStackType row : rowList) {
		// Card card = gameState().getStack(talon).toList().get(index);
		//
		// talonMove.getMoves().add(new Move(talon, row, card, null, false,
		// (int) System.currentTimeMillis()));
		// index--;
		// }
		// possibleMoves.add(talonMove);
		// }
		// System.out.println("validMoves: " + possibleMoves.size());

		return possibleMoves;
	}

	@Override
	public boolean execute(IMove moves) {
		try {
			gameState().executeMove(moves);
			setLastMove(moves);
		} catch (InconsistentMoveException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	@Override
	public boolean undo(IMove imove) {
		try {
			gameState().executeMove(reverse(imove));
		} catch (InconsistentMoveException e) {
			e.printStackTrace();
			System.out.println("###########################");
			return false;
		}

		return true;
	}

	private IMove reverse(IMove moves) {
		IMove ret = new IMove();
		for (GameMove move : moves) {
			ret.getMoves()
					.add(new GameMove(new GameMoveParameter(move.getTo(), move.getFrom(), move.getCards()), move.getPlayer(), move.getFlip()));
		}
		return ret;
	}

}
