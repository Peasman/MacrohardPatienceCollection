package controller;

import model.GameStatistics;

public class SuperStatisticsController {
	protected GameStatistics gameStatistics;

	public SuperStatisticsController() {
		super();
	}

	/**
	 * Muss aufgerufen wenn ein Aggro Patience Spiel mit der KI Level 1 beendet wurde, updated alle Werte im Model
	 * @param won		boolean ob das Spiel gewonnen wurde
	 * @param duration		Zeitspanne wie lange das Spiel gedauert hat
	 * @param cardsLeft		Anzahl an Karten, die übrig geblieben sind
	 * @param turns			maximale Anzahl an abgelegten Karten im Zug
	 */
	protected void updateGamesAPKI1(boolean won, Long duration, Double cardsLeft, Double turns) {
		gameStatistics.setAggroPatienceGamesAi1(gameStatistics.getAggroPatienceGames()[1]+1);
		if(won){
			gameStatistics.setAggroPatienceWinsAi1(gameStatistics.getAggroPatienceWins()[1]+1);
		}
		if((gameStatistics.getAggroPatienceFastestWin()[1]==null ||gameStatistics.getAggroPatienceFastestWin()[1].compareTo(duration)>0)&&won){
			gameStatistics.setAggroPatienceAi1FastestWin(duration);
		}
		double avrg;
		if(gameStatistics.getAggroPatienceCardsLeftAvrg()[1] == null) {
			avrg = cardsLeft;
		} else {
			double cardsLeftOld = gameStatistics.getAggroPatienceCardsLeftAvrg()[1];
			double gamesPlayed = gameStatistics.getAggroPatienceGames()[1];
			avrg = ((cardsLeftOld*(gamesPlayed-1))+cardsLeft)/gamesPlayed;
		}
		gameStatistics.setAggroPatienceAi1CardsLeftAvrg(avrg);
		
		if(gameStatistics.getAggroPatienceLongestTurn()[1] == null || turns > gameStatistics.getAggroPatienceLongestTurn()[1]){
			gameStatistics.setAggroPatienceAi1LongestTurn(turns);
		}
	}

	/**
	 * Muss aufgerufen wenn ein Aggro Patience Spiel mit der KI Level 2 beendet wurde, updated alle Werte im Model
	 * @param won		boolean ob das Spiel gewonnen wurde
	 * @param duration		Zeitspanne wie lange das Spiel gedauert hat
	 * @param cardsLeft		Anzahl an Karten, die übrig geblieben sind
	 * @param turns			maximale Anzahl an abgelegten Karten im Zug
	 */
	protected void updateGamesAPKI2(boolean won, Long duration, Double cardsLeft, Double turns) {
		gameStatistics.setAggroPatienceGamesAi2(gameStatistics.getAggroPatienceGames()[2]+1);
		if(won){
			gameStatistics.setAggroPatienceWinsAi2(gameStatistics.getAggroPatienceWins()[2]+1);
		}
		if((gameStatistics.getAggroPatienceFastestWin()[2]==null ||gameStatistics.getAggroPatienceFastestWin()[2].compareTo(duration)>0)&&won){
			gameStatistics.setAggroPatienceAi2FastestWin(duration);
		}
		double avrg;
		if(gameStatistics.getAggroPatienceCardsLeftAvrg()[2] == null) {
			avrg = cardsLeft;
		} else {
			double cardsLeftOld = gameStatistics.getAggroPatienceCardsLeftAvrg()[2];
			double gamesPlayed = gameStatistics.getAggroPatienceGames()[2];
			avrg = ((cardsLeftOld*(gamesPlayed-1))+cardsLeft)/gamesPlayed;
		}
		gameStatistics.setAggroPatienceAi2CardsLeftAvrg(avrg);
		
		if(gameStatistics.getAggroPatienceLongestTurn()[2] == null || turns > gameStatistics.getAggroPatienceLongestTurn()[2]){
			gameStatistics.setAggroPatienceAi2LongestTurn(turns);
		}
	}

	/**
	 * Muss aufgerufen wenn ein Aggro Patience Spiel mit der KI Level 3 beendet wurde, updated alle Werte im Model
	 * @param won		boolean ob das Spiel gewonnen wurde
	 * @param duration		Zeitspanne wie lange das Spiel gedauert hat
	 * @param cardsLeft		Anzahl an Karten, die übrig geblieben sind
	 * @param turns			maximale Anzahl an abgelegten Karten im Zug
	 */
	protected void updateGamesAPKI3(boolean won, Long duration, Double cardsLeft, Double turns) {
		gameStatistics.setAggroPatienceGamesAi3(gameStatistics.getAggroPatienceGames()[3]+1);
		if(won){
			gameStatistics.setAggroPatienceWinsAi3(gameStatistics.getAggroPatienceWins()[3]+1);
		}
		if((gameStatistics.getAggroPatienceFastestWin()[3]==null ||gameStatistics.getAggroPatienceFastestWin()[3].compareTo(duration)>0)&&won){
			gameStatistics.setAggroPatienceAi3FastestWin(duration);
		}
		
		double avrg;
		if(gameStatistics.getAggroPatienceCardsLeftAvrg()[3] == null) {
			avrg = cardsLeft;
		} else {
			double cardsLeftOld = gameStatistics.getAggroPatienceCardsLeftAvrg()[3];
			double gamesPlayed = gameStatistics.getAggroPatienceGames()[3];
			avrg = ((cardsLeftOld*(gamesPlayed-1))+cardsLeft)/gamesPlayed;
		}
		gameStatistics.setAggroPatienceAi3CardsLeftAvrg(avrg);
		
		if(gameStatistics.getAggroPatienceLongestTurn()[3] == null || turns > gameStatistics.getAggroPatienceLongestTurn()[3]){
			gameStatistics.setAggroPatienceAi3LongestTurn(turns);
		}
	}

	/**
	 * Muss aufgerufen wenn ein Aggro Patience Spiel mit einem Menschen beendet wurde, updated alle Werte im Model
	 * @param won		boolean ob das Spiel gewonnen wurde
	 * @param duration		Zeitspanne wie lange das Spiel gedauert hat
	 * @param cardsLeft		Anzahl an Karten, die übrig geblieben sind
	 * @param turns			maximale Anzahl an abgelegten Karten im Zug
	 */
	protected void updateGamesAPHuman(boolean won, Long duration, Double cardsLeft, Double turns) {
		gameStatistics.setAggroPatienceGamesHuman(gameStatistics.getAggroPatienceGames()[0]+1);
		if(won){
			gameStatistics.setAggroPatienceWinsHuman(gameStatistics.getAggroPatienceWins()[0]+1);
		}
		if((gameStatistics.getAggroPatienceFastestWin()[0]==null || gameStatistics.getAggroPatienceFastestWin()[0].compareTo(duration)>0)&&won){
			gameStatistics.setAggroPatienceHumanFastestWin(duration);
		}
		
		double avrg;
		if(gameStatistics.getAggroPatienceCardsLeftAvrg()[0] == null) {
			avrg = cardsLeft;
		} else {
			double cardsLeftOld = gameStatistics.getAggroPatienceCardsLeftAvrg()[0];
			double gamesPlayed = gameStatistics.getAggroPatienceGames()[0];
			avrg = ((cardsLeftOld*(gamesPlayed-1))+cardsLeft)/gamesPlayed;
		}
		gameStatistics.setAggroPatienceHumanCardsLeftAvrg(avrg);
	
		if(gameStatistics.getAggroPatienceLongestTurn()[0] == null || turns > gameStatistics.getAggroPatienceLongestTurn()[0]){
			gameStatistics.setAggroPatienceHumanLongestTurn(turns);
		}
	}

	/** Muss aufgerufen wenn ein Idioten Patience Spiel beendet wurde, updated alle Werte im Model
	 * @param won		boolean ob das Spiel gewonnen wurde
	 * @param duration		Zeitspanne wie lange das Spiel gedauert hat
	 * @param cardsLeft		Anzahl an Karten, die übrig geblieben sind
	 */
	protected void updateGamesIdiotPatience(boolean won, Long duration, Double cardsLeft) {
		gameStatistics.setIdiotPatienceGames(gameStatistics.getIdiotPatienceGames()+1);
		if(won){
			gameStatistics.setIdiotPatienceWins(gameStatistics.getIdiotPatienceWins()+1);
		}
		if((gameStatistics.getIdiotPatienceFastestWin()==null ||(gameStatistics.getIdiotPatienceFastestWin().compareTo(duration)>0 )) && won){
			gameStatistics.setIdiotPatienceFastestWin(duration);
		}
		if(gameStatistics.getIdiotPatienceMaxCards() == null || (52-cardsLeft)>gameStatistics.getIdiotPatienceMaxCards()){
			gameStatistics.setIdiotPatienceMaxCards(52-cardsLeft);
		}
		double avrg;
		if(gameStatistics.getIdiotPatienceCardsLeftAvrg() == null) {
			avrg = cardsLeft;
		} else {
			double cardsLeftOld = gameStatistics.getIdiotPatienceCardsLeftAvrg();
			double gamesPlayed = gameStatistics.getIdiotPatienceGames();
			avrg = ((cardsLeftOld*(gamesPlayed-1))+cardsLeft)/gamesPlayed;
		}
		gameStatistics.setIdiotPatienceCardsLeftAvrg(avrg);
	}



}