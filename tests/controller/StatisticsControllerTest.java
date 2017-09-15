package controller;

import org.junit.Test;

import model.Battlefield;
import model.GameType;
import model.Player;
import model.PlayerType;

/**
 * Test für den Statistiken Controller
 * @author Olga Scheftelowitsch, Alexander Herlez
 *
 */
public class StatisticsControllerTest {

	private StatisticsController statisticsController;
	
	/**
	 * testet den Konstruktor
	 */
	@Test
	public void testStatisticsController() {
		MPCController mpcController = new MPCController();
		statisticsController = new StatisticsController(mpcController);
		assert(statisticsController.getStatistics()!=null);
	}

	/**
	 * testet das Zurücksetzen der Statistiken
	 */
	@Test
	public void testResetStatistics() {
		MPCController mpcController = new MPCController();
		statisticsController = new StatisticsController(mpcController);
		statisticsController.resetStatistics();
		for(int i = 0; i < 4; ++i){
			assert(statisticsController.getStatistics().getAggroPatienceFastestWin()[i] ==null);
			assert(statisticsController.getStatistics().getAggroPatienceCardsLeftAvrg()[i] == null);
			assert(statisticsController.getStatistics().getAggroPatienceGames()[i] == 0);
			assert(statisticsController.getStatistics().getAggroPatienceWins()[i] == 0);
			assert(statisticsController.getStatistics().getAggroPatienceLongestTurn()[i]==null);
		}
		assert(statisticsController.getStatistics().getIdiotPatienceFastestWin()==null);
		assert(statisticsController.getStatistics().getIdiotPatienceWins()==0);
		assert(statisticsController.getStatistics().getFreecellWins()==0);
		assert(statisticsController.getStatistics().getIdiotPatienceGames()==0);
		assert(statisticsController.getStatistics().getFreecellGames()==0);
		assert(statisticsController.getStatistics().getFreecellFastestWin()==null);
	}

	/**
	 * testet das Updaten der Statistik
	 */
	@Test
	public void testUpdateStats() {
		MPCController mpcController = new MPCController();
		Player fred = new Player(PlayerType.AI1,"Fred");
		Player rudi = new Player(PlayerType.AI1,"Fred2");	
		
		Battlefield battlefield= GameController.getInstance().initBattlefield(GameType.IDIOT_PATIENCE, fred, rudi);
		mpcController.setBattlefield(battlefield);
		battlefield.setGameTime(467);
		statisticsController = new StatisticsController(mpcController);
		statisticsController.updateStats(true);
		statisticsController.updateStats(false);
		assert(statisticsController.getStatistics().getIdiotPatienceFastestWin()==(long)467);
		assert(statisticsController.getStatistics().getIdiotPatienceGames()==2);
		assert(statisticsController.getStatistics().getIdiotPatienceWins()==1);
		
		Battlefield battlefield2= GameController.getInstance().initBattlefield(GameType.FREECELL, fred, rudi);
		mpcController.setBattlefield(battlefield2);
		battlefield2.setGameTime(467);
		statisticsController = new StatisticsController(mpcController);
		statisticsController.updateStats(true);
		statisticsController.updateStats(false);
		assert(statisticsController.getStatistics().getFreecellFastestWin()==(long)467);
		assert(statisticsController.getStatistics().getFreecellGames()==2);
		assert(statisticsController.getStatistics().getFreecellWins()==1);
		
		Player fred2 = new Player(PlayerType.HUMAN,"Fred");
		Battlefield battlefield3= GameController.getInstance().initBattlefield(GameType.AGGRO_PATIENCE, fred, fred2);
		mpcController.setBattlefield(battlefield3);
		battlefield3.setGameTime(467);
		statisticsController = new StatisticsController(mpcController);
		statisticsController.updateStats(true);
		statisticsController.updateStats(false);
		assert(statisticsController.getStatistics().getAggroPatienceFastestWin()[0]==467);
		assert(statisticsController.getStatistics().getAggroPatienceGames()[0]==2);
		assert(statisticsController.getStatistics().getAggroPatienceWins()[0]==1);	
	}
	/**
	 * testet das Updaten der Statistik
	 */
	@Test
	public void testUpdateStats2() {
		MPCController mpcController = new MPCController();
		Player fred = new Player(PlayerType.AI1,"Fred");
		Player rudi = new Player(PlayerType.AI1,"Fred2");
		Battlefield battlefield2= GameController.getInstance().initBattlefield(GameType.FREECELL, fred, rudi);
		mpcController.setBattlefield(battlefield2);
		Player fred2 = new Player(PlayerType.HUMAN,"Fred");
		Battlefield battlefield3= GameController.getInstance().initBattlefield(GameType.AGGRO_PATIENCE, fred, fred2);
		fred2 = new Player(PlayerType.AI1,"Fred");
		battlefield3= GameController.getInstance().initBattlefield(GameType.AGGRO_PATIENCE, fred, fred2);
		mpcController.setBattlefield(battlefield3);
		battlefield3.setGameTime(467);
		statisticsController = new StatisticsController(mpcController);
		statisticsController.updateStats(true);
		statisticsController.updateStats(false);
		assert(statisticsController.getStatistics().getAggroPatienceFastestWin()[1]==467);
		assert(statisticsController.getStatistics().getAggroPatienceGames()[1]==2);
		assert(statisticsController.getStatistics().getAggroPatienceWins()[1]==1);	
		fred2 = new Player(PlayerType.AI2,"Fred");
		battlefield3= GameController.getInstance().initBattlefield(GameType.AGGRO_PATIENCE, fred, fred2);
		mpcController.setBattlefield(battlefield3);
		battlefield3.setGameTime(467);
		statisticsController = new StatisticsController(mpcController);
		statisticsController.updateStats(true);
		statisticsController.updateStats(false);
		assert(statisticsController.getStatistics().getAggroPatienceFastestWin()[2]==467);
		assert(statisticsController.getStatistics().getAggroPatienceGames()[2]==2);
		assert(statisticsController.getStatistics().getAggroPatienceWins()[2]==1);
		fred2 = new Player(PlayerType.AI3,"Fred");
		battlefield3= GameController.getInstance().initBattlefield(GameType.AGGRO_PATIENCE, fred, fred2);
		mpcController.setBattlefield(battlefield3);
		battlefield3.setGameTime(467);
		statisticsController = new StatisticsController(mpcController);
		statisticsController.updateStats(true);
		statisticsController.updateStats(false);
		assert(statisticsController.getStatistics().getAggroPatienceFastestWin()[3]==467);
		assert(statisticsController.getStatistics().getAggroPatienceGames()[3]==2);
		assert(statisticsController.getStatistics().getAggroPatienceWins()[3]==1);
	}
}
