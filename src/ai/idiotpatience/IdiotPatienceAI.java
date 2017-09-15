package ai.idiotpatience;

import java.util.Stack;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ai.general.IGameState;
import ai.general.IMove;
import ai.general.IVisitor;
import ai.general.PatienceAI;
import ai.general.Result;
import ai.general.StagedDeepening;
import controller.MPCController;
import model.Battlefield;
import model.GameCard;
import model.CardStackType;
import model.GameMove;
import model.GameCard.Rank;
import model.GameMove.SingleCardGameMoveParameter;

public class IdiotPatienceAI extends StagedDeepening<byte[], IdiotPatienceGameState> implements PatienceAI {

	private static final IVisitor VISITOR = new IVisitor() {

		@Override
		public void visitNode(IGameState gameState, int identfier) {
			// TODO Auto-generated method stub

		}

		@Override
		public void visitEdge(int parent, int child) {
			// TODO Auto-generated method stub

		}
	};
	
	private boolean isRunning = false;
	
	private CardStackType talon = CardStackType.TALON;
	private CardStackType[] rowList = new CardStackType[] { CardStackType.ROW_1, CardStackType.ROW_2, CardStackType.ROW_3,
			CardStackType.ROW_4};

	private static final IdiotPatienceEvaluator EVALUATOR = new IdiotPatienceEvaluator();

	private static final IdiotPatienceNodeArchiveComparator ARCHIVE_COMPARATOR = new IdiotPatienceNodeArchiveComparator();

	private TimeUnit timeUnit;

	private int duration;

	private static final ScheduledExecutorService TIMER = Executors.newSingleThreadScheduledExecutor();

	private MPCController mpcController;

	private boolean earlyFinish = false;

	public IdiotPatienceAI(TimeUnit timeUnit, int duration, MPCController mpcController) {
		super(VISITOR, EVALUATOR, ARCHIVE_COMPARATOR);
		this.timeUnit = timeUnit;
		this.duration = duration;
		this.mpcController = mpcController;
	}

	@Override
	public boolean searchComplete(IdiotPatienceGameState gameState) {
		// return (evaluator.evaluate((FreecellGameState) gameState)== 0);

		Battlefield battlefield = gameState.gameState();

		if (hasWon(battlefield)) {
			System.out.println(battlefield.getStack(CardStackType.STACKER_1).size());
			return true;
		}

		return false;
	}

	private boolean hasWon(Battlefield battlefield) {
		return battlefield.getStack(CardStackType.STACKER_1).size() == 48 && numAcesOnBottom(battlefield) == 4;
	}
	private int numAcesOnBottom(Battlefield battlefield) {
		int count = 0;
		for (CardStackType row : rowList) {
			if (battlefield.getStack(row).size() > 0) {
				GameCard lowest = battlefield.getStack(row).toList().get(0);
				if (lowest.getRank() == Rank.ACE)
					count++;
			}
		}
		return count;
	}
	@Override
	public Result get() {
		IdiotPatienceGameState gameState = (new IdiotPatienceGameState(mpcController.getBattlefield())).copy();
		searchDepth = 0;
		
		// muss VOR 'fullSearch(gameState)' aufgerufen werden
		startTimer();
		// muss NACH 'startTimer()' aufgerufen werden

		Stack<IMove> solution = new Stack<>();

		
		solution.push(dealTalon(gameState));
		int movesDone = 1;
		
		while (movesDone > 0) {
			movesDone = 0;
			Result result = fullSearch(gameState);
			System.out.println("optimized turn, playing turns: " + result.solution().size());
			
			for (IMove move : result.solution()) {
				solution.push(move);
				gameState.execute(move);
				movesDone++;
			}
			
			try {
				gameState = gameState.copy().clone();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
			
			if (gameState.gameState().getStack(CardStackType.TALON).size() > 0){
				solution.push(dealTalon(gameState));
				movesDone++;
			}
			
		}
		//System.out.println("computing finished");
		try {
			mpcController.getAIController().autoPlay(solution);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}

	private IMove dealTalon(IdiotPatienceGameState gameState){
		int index = gameState.gameState().getStack(talon).size() - 1;
		IMove talonMove = new IMove();
		for (CardStackType row : rowList) {
			GameCard card = gameState.gameState().getStack(talon).toList().get(index);
			talonMove.getMoves().add(new GameMove(new SingleCardGameMoveParameter(talon, row, card), null, true, (int) System.currentTimeMillis()));
			index--;
		}
		gameState.execute(talonMove);
		return talonMove;
	}
	
	
	private void startTimer() {
		TIMER.schedule(new Runnable() {
			@Override
			public void run() {
				earlyFinish = true;
			}
		}, duration, timeUnit);
	}

	@Override
	protected boolean earlyFinish(IdiotPatienceGameState gameState) {
		return earlyFinish;
	}
	
	@Override
	public boolean isRunning() {
		return isRunning;
	}
	
	@Override
	public void shutdown() {
		if (TIMER != null) {
			earlyFinish = true;
			TIMER.shutdownNow();
		}
	}
	
	@Override
	public Result getHint() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
