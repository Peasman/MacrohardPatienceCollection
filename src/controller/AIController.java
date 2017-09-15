package controller;

import java.util.ArrayList;
import java.util.Stack;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ai.freecell.FreecellAI;
import ai.general.IMove;
import ai.general.PatienceAI;
import ai.general.Result;
import ai.idiotpatience.IdiotPatienceAI;
import exceptions.InconsistentMoveException;
import gui.sprites.Textures;
import javafx.application.Platform;
import model.Battlefield;
import model.GameCard;
import model.CardStackType;
import model.GameType;
import model.Player;
import model.PlayerType;
import model.GameMove;
import model.GameMove.GameMoveParameter;
import model.GameMove.SingleCardGameMoveParameter;
import model.SuperBattlefield;

/**
 * Diese Klasse ist die Schnittstelle der KI und des Spiels. Mit ihr werden
 * Spiele gelöst oder Tipps gegeben.
 * 
 * @author Alexander Herlez
 *
 */
public class AIController {

	/**
	 * der Hauptcontroller @MPCController
	 */
	private MPCController mpcController;

	/**
	 * die IdiotPatience AI
	 */

	/**
	 * die Freecell AI
	 */
	private PatienceAI gameAI;
	
//	private FreecellAI freecellAI;
//	private IdiotPatienceAI idiotAI;
	
	private boolean allowAutoPlay = true;
	
	private Future<Result> result = null;
	
	private CompletableFuture<Result> completableFuture;
	
	private CompletableFuture<Void> autoPlayFuture;

	/**
	 * die ZankPatience AI
	 */

	/**
	 * der Spieltyp, fuer den die AI gerade aktiv ist - null, wenn AI nicht
	 * aktiv
	 */
	// private GameType currentGameTypeForAI = null;

	/**
	 * der Timer Thread
	 */
	// private Thread timerThread = null;

	/**
	 * der Executor der AI
	 */
	private ScheduledExecutorService aiExecutor;

	/**
	 * initialisiert den AIController
	 * 
	 * @param mpcController
	 *            der Hauptcontroller
	 */
	public AIController(MPCController mpcController) {
		this.mpcController = mpcController;
	}

	/**
	 * berechnet den nächsten möglichst guten Zug und gibt ihn wieder.
	 * 
	 * @param battleField
	 *            das Spielfeld auf dem der Zug gesucht werden soll
	 * @return ein guter Spielzug
	 */
	public GameMove getNextMove(SuperBattlefield battleField) {
		return new GameMove(new GameMoveParameter(CardStackType.FREECELL_1, CardStackType.FREECELL_2, new ArrayList<>()), new Player(PlayerType.AI1, "name"), false);
	}

	/**
	 * @param gibt
	 *            alle möglichen Züge wieder. Der Spielmodi wird aus dem
	 *            Battlefield gelesen.
	 * @return alle möglichen Züge
	 */
	public static ArrayList<GameMove> getAllPossibleMoves(Battlefield battlefield) {
		switch (battlefield.getGameType()) {
		case IDIOT_PATIENCE:
			return getAllPossibleIdiotMoves(battlefield);
		case AGGRO_PATIENCE:
			return getAllPossibleZankMoves(battlefield);
		case FREECELL:
			return getAllPossibleFreecellMoves(battlefield);
		}
		return null;
	}

	/**
	 * gibt alle möglichen Idioten Patience Spielzüge wieder
	 * 
	 * @param Das
	 *            Idioten Patience Spielfeld
	 * @return alle möglichen Züge
	 */
	private static ArrayList<GameMove> getAllPossibleIdiotMoves(Battlefield battlefield) {
		CardStackType rows[] = new CardStackType[] { CardStackType.ROW_1, CardStackType.ROW_2, CardStackType.ROW_3,
				CardStackType.ROW_4 };
		ArrayList<GameMove> posMoves = new ArrayList<GameMove>();
		for (CardStackType row : rows) {
			GameCard upperCard = battlefield.getUpperCard(row);
			if (upperCard == null) {
				continue;
			}

			GameMove move = new GameMove(new SingleCardGameMoveParameter(row, CardStackType.STACKER_1, upperCard), battlefield.getCurrentPlayer(), false);
			if (GameLogicController.isValid(battlefield, move)) {
				posMoves.add(move);
			}

			for (CardStackType row2 : rows) {
				if (row2 == row)
					continue;

				move = new GameMove(new SingleCardGameMoveParameter(row, row2, upperCard), battlefield.getCurrentPlayer(), false);
				if (GameLogicController.isValid(battlefield, move)) {
					posMoves.add(move);
				}
			}
		}

		return posMoves;
	}
	private static ArrayList<GameMove> getAllPossibleZankMoves(Battlefield battlefield){
		ArrayList<CardStackType> stacks = GameController.getInstance().getCardStackTypes(GameType.AGGRO_PATIENCE);
		ArrayList<GameMove> posMoves = new ArrayList<GameMove>();
		
		for (CardStackType stack : stacks) {
			for (CardStackType otherStack : stacks) {
				if (battlefield.getStack(stack).getOwner() != null && !battlefield.getStack(stack).getOwner().equals(battlefield.getCurrentPlayer()))
					continue;
				
				GameCard upperCard = battlefield.getUpperCard(stack);
				if (upperCard == null) {
					continue;
				}
				
				GameMove move = new GameMove(new SingleCardGameMoveParameter(stack, otherStack, upperCard), battlefield.getCurrentPlayer(), false);
				if (GameLogicController.isValid(battlefield, move)) {
					posMoves.add(move);
				}
			}
		}
		return posMoves;
	}
	
	
	/**
	 * gibt alle Möglichen Kartenbewegungen wieder, die im aktuellen FreeCell
	 * Spiel möglich sind
	 * 
	 * @param battlefield
	 *            das Freecell Spielfeld
	 * @return alle möglichen Züge
	 */
	private static ArrayList<GameMove> getAllPossibleFreecellMoves(Battlefield battlefield) {
		ArrayList<CardStackType> stacks = GameController.getInstance().getCardStackTypes(GameType.FREECELL);
		ArrayList<GameMove> posMoves = new ArrayList<GameMove>();
		for (CardStackType stack : stacks) {
			for (CardStackType otherStack : stacks) {
				GameCard upperCard = battlefield.getUpperCard(stack);
				if (upperCard == null) {
					continue;

				}
				ArrayList<GameCard> cardsToMove = new ArrayList<GameCard>();

				ArrayList<GameCard> cardsInStack = battlefield.getStack(stack).toList();
				if (cardsInStack.size() == 0)
					continue;
				for (int i = cardsInStack.size() - 1; i >= 0; i--) {
					cardsToMove.add(0, cardsInStack.get(i));
					GameMove multipleMove = new GameMove(new GameMoveParameter(stack, otherStack, new ArrayList<GameCard>(cardsToMove)), battlefield.getCurrentPlayer(), false);
					if (GameLogicController.isValid(battlefield, multipleMove)) {
						posMoves.add(multipleMove);
					}
				}
			}
		}
		return posMoves;
	}

	/**
	 * gibt den MPCController wieder. Erleichtert das Testen.
	 * 
	 * @return Hauptcontroller
	 */
	public MPCController getMpcController() {
		return mpcController;
	}
	/*
	 * 1. Always play an Ace or Deuce wherever you can immediately. 2. Always
	 * make the play or transfer that frees (or allows a play that frees) a
	 * downcard, regardless of any other considerations. 3. When faced with a
	 * choice, always make the play or transfer that frees (or allows a play
	 * that frees) the downcard from the biggest pile of downcards. 4. Transfer
	 * cards from column to column only to allow a downcard to be freed or to
	 * make the columns smoother. 5. Don't clear a spot unless there's a King
	 * IMMEDIATELY waiting to occupy it. 6. Only play a King that will benefit
	 * the column(s) with the biggest pile of downcards, unless the play of
	 * another King will at least allow a transfer that frees a downcard. 7.
	 * Only build your Ace stacks (with anything other than an Ace or Deuce)
	 * when the play will: Not interfere with your Next Card Protection Allow a
	 * play or transfer that frees (or allows a play that frees) a downcard Open
	 * up a space for a same-color card pile transfer that allows a downcard to
	 * be freed Clear a spot for an IMMEDIATE waiting King (it cannot be to
	 * simply clear a spot) 8. Don't play or transfer a 5, 6, 7 or 8 anywhere
	 * unless at least one of these situations will apply after the play: It is
	 * smooth with it's next highest even/odd partner in the column It will
	 * allow a play or transfer that will IMMEDIATELY free a downcard There have
	 * not been any other cards already played to the column You have ABSOLUTELY
	 * no other choice to continue playing (this is not a good sign) 9. When you
	 * get to a point that you think all of your necessary cards are covered and
	 * you just can't get to them, IMMEDIATELY play any cards you can to their
	 * appropriate Ace stacks. You may have to rearrange existing piles to allow
	 * blocked cards freedom to be able to go to their Ace stack. Hopefully this
	 * will clear an existing pile up to the point that you can use an existing
	 * pile upcard to substitute for the necessary covered card.
	 */

	/**
	 * 
	 * @param gameType
	 *            der Spieltyp, fuer den die passende AI gestartet werden soll
	 */
	public void startAI(boolean runInBackground, boolean autoPlay) {
		shutdown();
		GameType gameType = mpcController.getBattlefield().getGameType();
		mpcController.getGameLogicController().callOnAIMove(runInBackground);
		aiExecutor = Executors.newSingleThreadScheduledExecutor();
		
		switch (gameType) {
			case AGGRO_PATIENCE:
				
				break;
			case FREECELL:
	//			startFreecellAI();
				gameAI = new FreecellAI(TimeUnit.MINUTES, 9, mpcController);
				break;
			case IDIOT_PATIENCE:
	//			startIdiotPatienceAI();
				gameAI = new IdiotPatienceAI(TimeUnit.MINUTES, 9, mpcController);
				break;
		}

		completableFuture = CompletableFuture.supplyAsync(gameAI);
		if (autoPlay) {
			autoPlayFuture = completableFuture.thenAcceptAsync(result -> {
				try {
					autoPlay(result.solution());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			});
		}
		
//		result = aiExecutor.submit(gameAI);
		aiExecutor.shutdown();
	}

//	private void startFreecellAI() {
//		// freecellAI = new FreecellAI(TimeUnit.SECONDS, 580, mpcController);
//		// aiExecutor.submit(freecellAI);
//		freecellAI = null;
//		System.gc();
//		freecellAI = new FreecellAI(TimeUnit.MINUTES, 9, mpcController);
//		aiExecutor.submit(freecellAI);
//
//		aiExecutor.shutdown();
//	}

//	private void startIdiotPatienceAI() {
//		idiotAI = new IdiotPatienceAI(TimeUnit.MINUTES, 9, mpcController);
//		aiExecutor.submit(idiotAI);
//	}

	public void autoPlay(Stack<IMove> solution) throws InterruptedException {
		if (!allowAutoPlay) return;
		
		mpcController.getGameLogicController().callOnAIMove(false);
		double animSpeed = Textures.ANIMATION_SPEED;
		Textures.ANIMATION_SPEED = 0.1;
		for (IMove iMove : solution) {
			for (GameMove move : iMove.getMoves()) {
				if (mpcController.getBattlefield().getGameType() == GameType.IDIOT_PATIENCE
						|| mpcController.getBattlefield().getGameType() == GameType.FREECELL) {
					if (move.getFrom() == CardStackType.TALON) {
						Thread.sleep(100);
					} else {
						Thread.sleep(250);
					}
					Platform.runLater(() -> {
						try {
							mpcController.getGameLogicController().executeMove(move);
						} catch (InconsistentMoveException e) {
							e.printStackTrace();
						}
					});
				} else {
					Platform.runLater(() -> {
						try {
							if (mpcController.getGameLogicController().isValid(move)) {
								mpcController.getGameLogicController().executeMove(move);
							} else {
								System.out.println("invalid move");
							}
						} catch (InconsistentMoveException e) {
							e.printStackTrace();
						}
					});
				}
				System.out.println(move.getFrom() + " -> " + move.getTo());
			}
		}
		Textures.ANIMATION_SPEED = animSpeed;
		mpcController.getGameLogicController().callOnAIMove(true);
	}
	
	public Result getHint() {
		if (gameAI != null && gameAI.isRunning()) {
			
			System.out.println("ai is already running");
			
			return gameAI.getHint();
		}
		
//		allowAutoPlay = false;
		
//		mpcController.getBattlefield().setGotHelp();
//		GameType gameType = mpcController.getBattlefield().getGameType();
//		mpcController.getGameLogicController().callOnAIMove(false);
//		aiExecutor = Executors.newSingleThreadScheduledExecutor();
//		
//		gameAI = null;
//		System.gc();
//		gameAI = new FreecellAI(TimeUnit.SECONDS, 5, mpcController);
		
		
		startAI(true, false);
		allowAutoPlay = false;
		
		
//		Future<Result> future = aiExecutor.submit(gameAI);
		
//		Result result = null;
//		try {
//			result = future.get();
//		} catch (InterruptedException | ExecutionException e) {
//			e.printStackTrace();
//		}
//
//		aiExecutor.shutdown();
//		
//		allowAutoPlay = true;
//		
		
		
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		gameAI.getHint();
		
		return gameAI.getHint();
	}
	
	public void shutdown() {
		if (gameAI != null) {
			gameAI.shutdown();
			gameAI = null;
		}
		if (aiExecutor != null) {
			aiExecutor.shutdownNow();
			aiExecutor = null;
		}
		System.gc();
	}
	
}
