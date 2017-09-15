package controller;
import model.GameStatistics;

/**
 * Klasse, welche die Statistiken verwaltet
 * @author Olga Scheftelowitsch, Alexader Herlez
 */
public class StatisticsController extends SuperStatisticsController {
	private MPCController mpcController;
	/**
	 * Erzeugt den StatisticsController, welcher die Statistiken verwaltet
	 * @param mpcController der Hauptcontroller
	 */
	public StatisticsController(MPCController mpcController) {
		this.mpcController = mpcController;
		this.gameStatistics = new GameStatistics();
	}
	/**
	 * gibt die Spielstatistiken zurück
	 * @return	 das Statistiken Modell um die Werte zu erhalten
	 */
	public GameStatistics getStatistics() {
		return gameStatistics;
	}
	
	/**
	 * setzt neue Gamestatistiken
	 * @param newGameStatistics die neuen Statistiken
	 */
	public void setGameStatistics(GameStatistics newGameStatistics) {
		gameStatistics = newGameStatistics; 
	}
	/**
	 * resettet die Statistiken
	 */
	public void resetStatistics() {
		gameStatistics = new GameStatistics();
	}
	/** Muss aufgerufen wenn ein Freecell Spiel beendet wurde, updated alle Werte im Model
	 * @param won		boolean ob das Spiel gewonnen wurde
	 * @param duration		Zeitspanne wie lange das Spiel gedauert hat
	 * @param cardsLeft		Anzahl an Karten, die übrig geblieben sind
	 * @param moves		Anzahl an Zügen in denen das Spiel beendet wurde
	 */
	protected void updateGamesFreecell(boolean won, Long duration, Double cardsLeft, Double moves) {
		gameStatistics.setFreecellGames(gameStatistics.getFreecellGames()+1);
		if(won){
			gameStatistics.setFreecellWins(gameStatistics.getFreecellWins()+1);
		}
		if((gameStatistics.getFreecellFastestWin()==null || gameStatistics.getFreecellFastestWin().compareTo(duration)>0)&&won){
			gameStatistics.setFreecellFastestWin(duration);
		}
		if((gameStatistics.getFreecellMinTurns()==null || moves<gameStatistics.getFreecellMinTurns()) && won){
			gameStatistics.setFreecellMinTurns(moves);
		}
		double avrg;
		if(gameStatistics.getFreecellCardsLeftAvrg() == null) {
			avrg = cardsLeft;
		} else {
			double cardsLeftOld = gameStatistics.getFreecellCardsLeftAvrg();
			double gamesPlayed = gameStatistics.getFreecellGames();
			avrg = ((cardsLeftOld*(gamesPlayed-1))+cardsLeft)/gamesPlayed;
		}
		gameStatistics.setFreecellCardsLeftAvrg(avrg);
	}
	/**
	 * aktualisiert die Statistiken am Ende eines Spiels, benutzt Parameter eines fertigen Spiels 
	 * @param won ob der Spieler gewonnen hat
	 * @param duration Dauer des Spiels
	 * @param cardsLeft Karten übrig am Ende des Spiels
	 * @param moves die Anzahl an Kartenbewegungen
	 */
	public void updateStats(boolean won) {
		Long duration = mpcController.getBattlefield().getGameTime();
		Double cardsLeft = mpcController.getBattlefield().getCardsLeft();
		Double moves = mpcController.getBattlefield().getNumberOfTurns();
		
		switch (mpcController.getBattlefield().getGameType()) {
		case IDIOT_PATIENCE: updateGamesIdiotPatience(won, duration, cardsLeft); break;
		case FREECELL: updateGamesFreecell(won, duration, cardsLeft, moves); break; 
		case AGGRO_PATIENCE:
			switch(mpcController.getBattlefield().getPlayerTwo().getType()) {
			case HUMAN : updateGamesAPHuman(won, duration, cardsLeft, moves); break;
			case HUMAN_ONLINE : updateGamesAPHuman(won, duration, cardsLeft, moves); break;
			case AI1:  updateGamesAPKI1(won, duration, cardsLeft, moves); break;
			case AI2:  updateGamesAPKI2(won, duration, cardsLeft, moves); break;
			case AI3:  updateGamesAPKI3(won, duration, cardsLeft, moves); break;
			}
		}
	}
}