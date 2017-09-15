package model;

import org.junit.Before;
import org.junit.Test;

/** testklasse für  die Statistics Model Klasse
 * @author Olga Scheftelowitsch
 *
 */
public class GameStatisticsTest {
	GameStatistics testStatistics;
	/**	Methode zum erstellen einer GameStatistic */
	@Before
	public void setUp() {
		testStatistics = new GameStatistics();
		Double[] wins = {3.0,5.0,2.0,0.0};
		testStatistics.setAggroPatienceWins(wins);
		testStatistics.setAggroPatienceGames(wins);
	}

	/**
	 * Testet ob die Anzahl der Spiele gegen KI 2 richtig geändert wird
	 */
	@Test
	public void testSetAggroPatienceGamesAi2() {
		testStatistics.setAggroPatienceGamesAi2(3.0);
		assert(testStatistics.getAggroPatienceGames()[2]==3.0);
	}
	/**
	 * Testet ob die Anzahl der Spiele gegen KI 3 richtig geändert wird
	 */
	@Test
	public void testSetAggroPatienceGamesAi3() {
		testStatistics.setAggroPatienceGamesAi3(9.0);
		assert(testStatistics.getAggroPatienceGames()[3]==9.0);
	}

	/**
	 * Testet ob die Anzahl der Spiele gegen einen Menschen richtig geändert wird
	 */
	@Test
	public void testSetAggroPatienceGamesHuman() {
		testStatistics.setAggroPatienceGamesHuman(9.0);
		assert(testStatistics.getAggroPatienceGames()[0]==9.0);
	}
	
	/**
	 * Testet ob die Anzahl der Spiele gegen KI 1 richtig geändert wird
	 */
	@Test
	public void testSetAggroPatienceWinsAi1() {
		testStatistics.setAggroPatienceWinsAi1(9.0);
		assert(testStatistics.getAggroPatienceWins()[1]==9.0);
	}
	/**
	 * Testet ob die Anzahl der gewonnenen Spiele gegen KI 2 richtig geändert wird
	 */
	@Test
	public void testSetAggroPatienceWinsAi2() {
		testStatistics.setAggroPatienceWinsAi2(9.0);
		assert(testStatistics.getAggroPatienceWins()[2]==9.0);
	}
	/**
	 * Testet ob die Anzahl der gewonnenen Spiele gegen KI 3 richtig geändert wird
	 */
	@Test
	public void testSetAggroPatienceWinsAi3() {
		testStatistics.setAggroPatienceWinsAi3(9.0);
		assert(testStatistics.getAggroPatienceWins()[3]==9.0);
	}
	/**
	 * Testet ob die Anzahl der gewonnenen Spiele gegen einen Menschen richtig geändert wird
	 */
	@Test
	public void testSetAggroPatienceWinsHuman() {
		testStatistics.setAggroPatienceWinsHuman(9.0);
		assert(testStatistics.getAggroPatienceWins()[0]==9.0);
	}

	/**
	 * Testet ob die größte Anzahl an Zügen in Aggro Patience gegen KI1 richtig geändert wird
	 */
	@Test
	public void testSetAggroPatienceAi1LongestTurn() {
		testStatistics.setAggroPatienceAi1LongestTurn(9.0);
		assert(testStatistics.getAggroPatienceLongestTurn()[1]==9.0);
	}
	/**
	 * Testet ob die größte Anzahl an Zügen in Aggro Patience gegen KI2 richtig geändert wird
	 */
	@Test
	public void testSetAggroPatienceAi2LongestTurn() {
		testStatistics.setAggroPatienceAi2LongestTurn(9.0);
		assert(testStatistics.getAggroPatienceLongestTurn()[2]==9.0);
	}
	/**
	 * Testet ob die größte Anzahl an Zügen in Aggro Patience gegen KI3 richtig geändert wird
	 */
	@Test
	public void testSetAggroPatienceAi3LongestTurn() {
		testStatistics.setAggroPatienceAi3LongestTurn(9.0);
		assert(testStatistics.getAggroPatienceLongestTurn()[3]==9.0);
	}
	/**
	 * Testet ob die größte Anzahl an Zügen in Aggro Patience gegen einen Menschen richtig geändert wird
	 */
	@Test
	public void testSetAggroPatienceHumanLongestTurn() {
		testStatistics.setAggroPatienceHumanLongestTurn(9.0);
		assert(testStatistics.getAggroPatienceLongestTurn()[0]==9.0);
	}

	/**
	 * Testet ob die Dauer des am schnellsten gewonnenen Spiels in Aggro Patience gegen KI2 richtig gesetzt wird
	 */
	@Test
	public void testSetAggroPatienceAi2FastestWin() {
		Long duration =  (long)345;
		testStatistics.setAggroPatienceAi2FastestWin(duration);
		assert(testStatistics.getAggroPatienceFastestWin()[2].equals(duration));
	}
	/**
	 * Testet ob die Dauer des am schnellsten gewonnenen Spiels in Aggro Patience gegen KI3 richtig gesetzt wird
	 */
	@Test
	public void testSetAggroPatienceAi3FastestWin() {
		Long duration =  (long)352;
		testStatistics.setAggroPatienceAi3FastestWin(duration);
		assert(testStatistics.getAggroPatienceFastestWin()[3].equals(duration));
	}
	/**
	 * Testet ob die Dauer des am schnellsten gewonnenen Spiels in Aggro Patience gegen einen Menschen richtig gesetzt wird
	 */
	@Test
	public void testSetAggroPatienceHumanFastestWin() {
		Long duration =  (long)432;
		testStatistics.setAggroPatienceHumanFastestWin(duration);
		assert(testStatistics.getAggroPatienceFastestWin()[0].equals(duration));
	}

	/**
	 * Testet ob die Anzahl übrig gebliebener Karten in Aggro Patience gegen KI1 richtig geändert wird 
	 */
	@Test
	public void testSetAggroPatienceAi1CardsLeftAvrg() {
		testStatistics.setAggroPatienceAi1CardsLeftAvrg(4.5);
		assert(testStatistics.getAggroPatienceCardsLeftAvrg()[1]==4.5);
	}
	/**
	 * Testet ob die Anzahl übrig gebliebener Karten in Aggro Patience gegen KI2 richtig geändert wird 
	 */
	@Test
	public void testSetAggroPatienceAi2CardsLeftAvrg() {
		testStatistics.setAggroPatienceAi2CardsLeftAvrg(4.5);
		assert(testStatistics.getAggroPatienceCardsLeftAvrg()[2]==4.5);
	}
	/**
	 * Testet ob die Anzahl übrig gebliebener Karten in Aggro Patience gegen KI3 richtig geändert wird 
	 */
	@Test
	public void testSetAggroPatienceAi3CardsLeftAvrg() {
		testStatistics.setAggroPatienceAi3CardsLeftAvrg(4.5);
		assert(testStatistics.getAggroPatienceCardsLeftAvrg()[3]==4.5);
	}
	/**
	 * Testet ob die Anzahl übrig gebliebener Karten in Aggro Patience gegen einen Menschen richtig geändert wird 
	 */
	@Test
	public void testSetAggroPatienceHumanCardsLeftAvrg() {
		testStatistics.setAggroPatienceHumanCardsLeftAvrg(4.5);
		assert(testStatistics.getAggroPatienceCardsLeftAvrg()[0]==4.5);
	}

}
