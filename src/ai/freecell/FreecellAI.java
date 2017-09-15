package ai.freecell;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ai.general.IGameState;
import ai.general.IVisitor;
import ai.general.PatienceAI;
import ai.general.Result;
import ai.general.StagedDeepening;
import controller.MPCController;
import model.Battlefield;
import model.CardStackType;

public class FreecellAI extends StagedDeepening<byte[], FreecellGameState> implements PatienceAI {

	private static IVisitor visitor = new IVisitor() {
		
		@Override
		public void visitNode(IGameState n, int id) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void visitEdge(int parent, int child) {
			// TODO Auto-generated method stub
			
		}
	};
	
	private boolean isRunning = false;
	
	private static FreecellEvaluator evaluator = new FreecellEvaluator();
	
	private static FreecellNodeArchiveComparator archievComparator = new FreecellNodeArchiveComparator();
	
	private TimeUnit timeUnit;
	
	private int duration;
	
	private ScheduledExecutorService timer;
	
	private MPCController mpcController;
	
	private boolean earlyFinish = false;
	
	public FreecellAI(TimeUnit timeUnit, int duration, MPCController mpcController) {
		super(visitor, evaluator, archievComparator);
		this.timeUnit = timeUnit;
		this.duration = duration;
		this.mpcController = mpcController;
		
		timer = Executors.newSingleThreadScheduledExecutor();
	}

	@Override
	public boolean searchComplete(FreecellGameState gameState) {
		// return (evaluator.evaluate((FreecellGameState) gameState)== 0);

		Battlefield battlefield = gameState.gameState();

		if (hasWon(battlefield)) {
//			System.out.println(battlefield.getStack(CardStackType.STACKER_1).size() + " " + " "
//					+ battlefield.getStack(CardStackType.STACKER_2).size() + " "
//					+ battlefield.getStack(CardStackType.STACKER_3).size() + " "
//					+ battlefield.getStack(CardStackType.STACKER_4).size());
			return true;
		}

		return false;
	}

	private boolean hasWon(Battlefield battlefield) {
		return battlefield.getStack(CardStackType.STACKER_1).size() == 13
				&& battlefield.getStack(CardStackType.STACKER_2).size() == 13
				&& battlefield.getStack(CardStackType.STACKER_3).size() == 13
				&& battlefield.getStack(CardStackType.STACKER_4).size() == 13;
	}

	@Override
	public Result get() {
		FreecellGameState gameState = new FreecellGameState(mpcController.getBattlefield());
		
		isRunning = true;
		
		// muss VOR 'fullSearch(gameState)' aufgerufen werden
		startTimer();
		
		// muss NACH 'startTimer()' aufgerufen werden
		Result result = fullSearch(gameState);
		
		timer.shutdownNow();
		
//		mpcController.getAIController().autoPlay(result.solution());
		
		isRunning = false;
		
		return result;
	}
	
	private void startTimer() {
		timer.schedule(new Runnable() {
			@Override
			public void run() {
				earlyFinish = true;
				System.err.println("Time's up!");
			}
		}, duration, timeUnit);
	}
	
	@Override
	protected boolean earlyFinish(FreecellGameState gameState) {
		return earlyFinish;
	}

	@Override
	public boolean isRunning() {
		return isRunning;
	}
	
	@Override
	public void shutdown() {
		if (timer != null) {
			earlyFinish = true;
			timer.shutdownNow();
		}
	}
	
	@Override
	public Result getHint() {
		if (bestState != null) {
			return new Result(computeSolution(bestState));
		}
		return null;
	}
	
}
