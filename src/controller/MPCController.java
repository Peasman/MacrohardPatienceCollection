
package controller;

import java.io.IOException;

import model.Battlefield;
import model.GameCard;

/**
 * Der MPCController ist der zentrale Controller im Programm. Er initialisiert
 * und besitzt alle anderen Controller
 * 
 * @see GameController, IOController, GameLogicController, StatisticsController,
 *      AIController
 * @author Friedemann Runte
 *
 */
public class MPCController {

	/**
	 * Der GameLogicController
	 * 
	 * @see gameLogicController
	 */
	private GameLogicController gameLogicController;

	/**
	 * Das Spielfeld vom derzeit gespielten Spiel. Wird geändert wenn ein anderer
	 * Spielmodus oder ein neues Spiel gestartet wird.
	 * 
	 * @see Battlefield
	 */
	private Battlefield battlefield;

	/**
	 * Der AIController der die KISpielzüge kontrolliert.
	 * 
	 * @see AIController
	 */
	private AIController aiController;

	/**
	 * Der StatisticsController für alle Statistiken
	 * @see StatisticsController
	 */
	private StatisticsController statisticsController;

	/**
	 * Der GameController zum initialisieren aller Spieltypen
	 * 
	 * @see GameController
	 */
	private GameController gameController;
	
	private ServerController serverController;
//	private StackerGroupController stackerGroupController;

	/**
	 * Im Konstruktor werden alle Controller initialisiert und das Battlefield vorerst auf null gesetzt.
	 */
	public MPCController() {
		gameController = GameController.getInstance();
		statisticsController = new StatisticsController(this);
		gameLogicController = new GameLogicController(this);
		aiController = new AIController(this);
		serverController = new ServerController(this);
		battlefield = null;
	}

	/**
	 * Gibt den derzeitigen GameLogicController zurück
	 * @return gameLogicController 
	 */
	public GameLogicController getGameLogicController() {
		return gameLogicController;
	}

	/**
	 * Gibt den derzeitigen AIController zurück.
	 * @return aiController Der derzeitige AIController
	 */
	public AIController getAIController() {
		return aiController;
	}

	/**
	 * Setzt das battlefield auf ein neues Battlefield;
	 * @param battlefield Das neue Battlefield
	 */
	public void setBattlefield(Battlefield battlefield) {
		this.battlefield = battlefield;
//		aiController.startAI(true, false);
	}
	
	public void loadBattlefield(String path) {
		try {
			this.battlefield = IOController.loadGame(path);
			switch(battlefield.getGameType()) {
			case AGGRO_PATIENCE:
				GameCard.Rank.ACE.setRankValue(1);
				break;
			case FREECELL:
				GameCard.Rank.ACE.setRankValue(1);
				break;
			case IDIOT_PATIENCE:
				GameCard.Rank.ACE.setRankValue(14);
				break;
			default:
				break;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Gibt das momentante Battlefield zurück
	 * @return battlefield Das aktuelle Spielfeld
	 */
	public Battlefield getBattlefield() {
		return battlefield;
	}
	
	/**
	 * Gibt den derzeitigen StatisticsController zurück.
	 * @return statisticsController der derzeitige StatistikController
	 */
	public StatisticsController getStatisticsController() {
		return statisticsController;
	}

	/**
	 * Gibt den GameController zurück
	 * @return gameController Der aktuelle GameController
	 */
	public GameController getGameController() {
		return gameController;
	}

	/**
	 * Gibt den aktuellen ServerController zurück.
	 * @return Der aktuelle ServerController
	 */
	public ServerController getServerController() {
		return serverController;
	}
	
	/**
	 * Setzt den ServerController auf den übergebenenen ServerController.
	 * @param serverController Der zu setzende ServerController
	 */
	public void setServerController(ServerController serverController) {
		this.serverController = serverController;
	}

}
