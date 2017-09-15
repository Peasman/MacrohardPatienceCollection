package ai.zankpatience;

import java.util.HashMap;
import java.util.Random;

import controller.GameLogicController;
import controller.MPCController;
import exceptions.InconsistentMoveException;
import javafx.application.Platform;
import model.Battlefield;
import model.CardStack;
import model.CardStackSuperType;
import model.GameCard;
import model.GameMove;
import model.GameMove.SingleCardGameMoveParameter;
import model.Player;

public class ZankPatienceAI {

	private MPCController mpcController;
	private GameLogicController gameLogic;
	private Battlefield selectedBattlefield;
	private Player aiPlayer;
	private int difficulty;
	private Random rnJesus;
	private ZankGameState instance;
	private int maxFieldMoves;
	private int fieldMoves = 0;
	private GameMove lastPlayedMove;
	private int lastMoveId;
	private HashMap<MoveSignature, Boolean> moveSignatures;
	
	public ZankPatienceAI(MPCController mpc, Player player) throws IllegalArgumentException {
		this.mpcController = mpc;
		this.selectedBattlefield = mpcController.getBattlefield();
		this.gameLogic = mpcController.getGameLogicController();
		this.aiPlayer = player;
		checkConsistency(selectedBattlefield, aiPlayer);
		difficulty = this.getDifficulty();
		rnJesus = new Random();
		instance = new ZankGameState(selectedBattlefield, aiPlayer);
		this.maxFieldMoves = 4 + 5*difficulty*difficulty;
		moveSignatures = new HashMap<MoveSignature, Boolean>();
	}
	
	
	
	public void runLoop() {
		lastPlayedMove = null;
		fieldMoves = 0;
		lastMoveId = selectedBattlefield.getMoveHistory().get(selectedBattlefield.getMoveHistory().size()-1).getTurn();
		do {
			//System.out.println("findng move...");
			findMove();
		} while (hasTurn() && doActionRoll());
		if(hasTurn())
			endTurn();
	}
	
	private void findMove() {
		for (CardStack fromStack : instance.getMyStacks()) {
			CardStack newFromstack = instance.getMyStacks()[0];
			for(CardStack frStk : instance.getMyStacks()) {
				if(newFromstack.size() == 0 || (frStk.size() > 0 && frStk.peek().getRank().getRankValue() > newFromstack.peek().getRank().getRankValue())) {
					newFromstack = frStk;
				}
			}
			for (CardStack toStack : instance.getEnemyPunishStacks()) {
				GameMove generatedMove = new GameMove(new SingleCardGameMoveParameter(newFromstack.getType(), toStack.getType(), newFromstack.peek()), aiPlayer,
						false, lastMoveId);
				if (newFromstack.peek() != null && gameLogic.isValid(generatedMove)) {
					playMove(generatedMove);
					return;
				}
			}
			for (CardStack toStack : instance.getFieldStacks()) {
				GameMove generatedMove = new GameMove(new SingleCardGameMoveParameter(newFromstack.getType(), toStack.getType(), newFromstack.peek()), aiPlayer,
						false, lastMoveId);
				if (newFromstack.peek() != null && gameLogic.isValid(generatedMove)) {
					playMove(generatedMove);
					return;
				}
			}
			for (CardStack toStack : instance.getFieldStacks()) {
				GameMove generatedMove = new GameMove(new SingleCardGameMoveParameter(newFromstack.getType(), toStack.getType(), newFromstack.peek()), aiPlayer,
						false, lastMoveId);
				if (newFromstack.peek() != null && gameLogic.isValid(generatedMove)) {
					playMove(generatedMove);
					return;
				}
			}
		}
		for (CardStack fromStack : instance.getFieldStacks()) {
			for (CardStack toStack : instance.getEnemyPunishStacks()) {
				GameMove generatedMove = new GameMove(new SingleCardGameMoveParameter(fromStack.getType(), toStack.getType(), fromStack.peek()), aiPlayer,
						false, lastMoveId);
				if (fromStack.peek() != null && gameLogic.isValid(generatedMove)) {
					playMove(generatedMove);
					return;
				}
			}
			if(fieldMoves > maxFieldMoves) {
				endTurn();
				return;
			}
			for (CardStack toStack : instance.getFieldStacks()) {
				GameMove generatedMove = new GameMove(new SingleCardGameMoveParameter(fromStack.getType(), toStack.getType(), fromStack.peek()), aiPlayer,
						false, lastMoveId);
				if (fromStack.peek() != null && gameLogic.isValid(generatedMove) && !isMoveStupid(generatedMove, lastPlayedMove)) {
					playMove(generatedMove);
					return;
				}
				fieldMoves++;
			}
		}
		//if(lastPlayedMove == null)
		endTurn();
	}
	
	private void generateMove(CardStack fromStack, CardStack toStack) {
		GameMove generatedMove = new GameMove(new SingleCardGameMoveParameter(fromStack.getType(), toStack.getType(), fromStack.peek()), aiPlayer,
				false, lastMoveId);
		if (fromStack.peek() != null && gameLogic.isValid(generatedMove)) {
			playMove(generatedMove);
			return;
		}
	}

	private void playMove(GameMove move) {
		fieldMoves++;
		if( lastPlayedMove != null && isMoveStupid(move, lastPlayedMove)) {
			return;
		} else {
			lastPlayedMove = move;
		}
		moveSignatures.put(new MoveSignature(move, selectedBattlefield), true);
		try {
			Platform.runLater(()->{ 
				try {
					gameLogic.executeMove(move);
				} catch (InconsistentMoveException e) {
					e.printStackTrace();
				} 
			});
			Thread.sleep(1000);
			return;
		} catch (InterruptedException e) {
			// e.printStackTrace();
		}
	}
	
	private boolean isMoveStupid(GameMove currentMove, GameMove lastMove) {
		if(currentMove == null || lastMove == null)
				return true;
		MoveSignature sig = new MoveSignature(currentMove, selectedBattlefield);
		if(moveSignatures.containsKey(sig) && moveSignatures.get(sig))
			return true;
		CardStack lastTo = selectedBattlefield.getStack(lastMove.getTo());
		CardStack currentTo = selectedBattlefield.getStack(currentMove.getTo());
		GameCard currentMovingCard = currentMove.getCards().get(0);
		GameCard lastMovingCard = lastMove.getCards().get(0);
		if(lastMovingCard.sameCardAs(currentMovingCard)) {
			if(lastTo.size() == 0 && currentTo.size() == 0) {
				return true;
			}
			if(lastMove.getFrom().equals(currentMove.getTo())) {
				return true;
			}
		}
		return false;
	}
	
	public void endTurn() {
		System.out.println("ENDE");
		if(instance.getMyTalon().size() == 0) {
			try {
				gameLogic.endCurrentTurn();
				return;
			} catch (InconsistentMoveException e) {
				e.printStackTrace();
			}
		} else {
			GameMove generatedMove = new GameMove(new SingleCardGameMoveParameter(instance.getMyTalon().getType(), instance.getMyRestStack().getType(), instance.getMyTalon().peek()), aiPlayer,
					false, lastMoveId);
			if (instance.getMyTalon().peek() != null && gameLogic.isValid(generatedMove)) {
				playMove(generatedMove);
				return;
			}
			//System.out.println("wat");
		}
	}

	public int getDifficulty() {
		switch (aiPlayer.getType()) {
		case AI1:
			return 0;
		case AI2:
			return 1;
		case AI3:
			return 2;
		default:
			return -1;
		}
	}

	public boolean hasTurn() {
		return (selectedBattlefield.getCurrentPlayer().equals(aiPlayer));
	}

	public boolean doActionRoll() {
		float random = rnJesus.nextFloat();
		switch (difficulty) {
		case 0:
			random += 0.4f;
			break;
		case 1:
			random += 0.2;
			break;
		case 2:
			random += 0.00f;
			break;
		default:
			random -= 1.337f;
			break;
		}
		return random <= 1.0f;
	}

	private void checkConsistency(Battlefield field, Player player) throws IllegalArgumentException {
		if (player == field.getPlayerOne() || player == field.getPlayerTwo()) {
			switch (player.getType()) {
			case AI1:
			case AI2:
			case AI3:
				return;
			default:
				break;
			}
		}
		throw new IllegalArgumentException("KI ist nicht als Spieler im Spielfeld enthalten!");
	}

	

}