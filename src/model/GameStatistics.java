package model;
/**
 * Klasse beinhaltet alle wichtigen Statistik Werte aller verschiedenen Spiel-Modi
 * @author Olga Scheftelowitsch
 */
public class GameStatistics {
	/** Anzahl der mit KI1 gespielten Aggro Patience Spiele */
	private Double[] aggroPatienceGames;
	/** Anzahl der mit KI1 gewonnenen Aggro Patience Spiele  */
	private Double[] aggroPatienceWins;
	/** Maximale Anzahl an Zügen in einem Zug gegen KI1 in Aggro Patience*/
	private Double[] aggroPatienceLongestTurn;
	/** Schnellste Runge in aggro Patience mit KI1*/
	private Long[] aggroPatienceFastestWin;
	/** Durchschnittliche Anzahl an Karten übrig in Aggr Patience gegen KI1 */
	private Double[] aggroPatienceCardsLeftAvrg;
	/** Anzahl an Spielen in Idiot Patience*/
	private Double idiotPatienceGames;
	/** Anzahl an Spielen in Freecell*/
	private Double freecellGames;
	/** Anzahl an gewonnenen Spielen in Idiot Patience*/
	private Double idiotPatienceWins;
	/** Anzahl an gewonnenen Spielen in Freecell*/
	private Double freecellWins;
	/** Maximale Anzahl an Karten weggelegt in Idioten Patience*/
	private Double idiotPatienceMaxCards;
	/** Durchschnittliche Anzahl an Karten übrig in Idioten Patience*/
	private Double idiotPatienceCardsLeftAvrg;
	/** Durchschnittliche Anzahl an Karten übrig in Freecell*/
	private Double freecellCardsLeftAvrg;
	/** Minimale Anzahl an Zügen in Freecell*/
	private Double freecellMinTurns;
	/**Dauer der schnellsten freecell Partie*/
	private Long freecellFastestWin;
	/**Dauer der schnellsten Idioten Patience Partie*/
	private Long idiotPatienceFastestWin;
	
	public GameStatistics(){
		aggroPatienceWins = new Double[4];
		aggroPatienceGames= new Double[4];
		aggroPatienceLongestTurn = new Double[4];
		aggroPatienceCardsLeftAvrg = new Double[4];
		aggroPatienceFastestWin = new Long[4];
		freecellGames = 0.0;
		freecellWins = 0.0;
		//freecellCardsLeftAvrg = 0.0;
		idiotPatienceWins = 0.0;
		idiotPatienceGames = 0.0;
		//idiotPatienceMaxCards = 0.0;
		//idiotPatienceCardsLeftAvrg = 0.0;
		for(int i = 0; i<4; ++i){
			aggroPatienceWins[i] = 0.0;
			aggroPatienceGames[i] = 0.0;
			//aggroPatienceLongestTurn[i] = 0.0;
			//aggroPatienceCardsLeftAvrg[i] = 0.0;
		}
	}
	
	/**setzt neue Anzahl an Aggro Patience Spielen mit KI2
	 * @param aggroPatienceGamesAi2			neue Anzahl an Aggro Patience Spielen mit KI2*/
	public void setAggroPatienceGamesAi2(Double aggroPatienceGamesAi2) {
		this.aggroPatienceGames[2] = aggroPatienceGamesAi2;
	}
	/**setzt neue Anzahl an Aggro Patience Spielen mit KI3
	 * @param aggroPatienceGamesAi3			neue Anzahl an Aggro Patience Spielen mit KI3*/
	public void setAggroPatienceGamesAi3(Double aggroPatienceGamesAi3) {
		this.aggroPatienceGames[3] = aggroPatienceGamesAi3;
	}
	/**setzt neue Anzahl an Aggro Patience Spielen mit einem Menschen
	 * @param aggroPatienceGamesHuman		neue Anzahl an Aggro Patience Spielen mit einem Menschen */
	public void setAggroPatienceGamesHuman(Double aggroPatienceGamesHuman) {
		this.aggroPatienceGames[0] = aggroPatienceGamesHuman;
	}
	/** setzt neue Anzahl an gewonnenen Aggro Patience Spielen mit KI Level 1
	 * @param aggroPatienceWinsAi1		neue Anzahl an Aggro Patience Spielen mit KI1 */
	public void setAggroPatienceWinsAi1(Double aggroPatienceWinsAi1) {
		this.aggroPatienceWins[1] = aggroPatienceWinsAi1;
	}
	/** setzt neue Anzahl an in Aggro Patience gewonnenen Spielen mit KI Level 2
	 * @param aggroPatienceWinsAi2	neue Anzahl an in Aggro Patience gewonnenen Spielen mit KI Level 2 */
	public void setAggroPatienceWinsAi2(Double aggroPatienceWinsAi2) {
		this.aggroPatienceWins[2] = aggroPatienceWinsAi2;
	}
	/** setzt neue Anzahl an in Aggro Patience gewonnenen Spielen mit KI Level 3
	 * @param aggroPatienceWinsAi3		neue Anzahl an in Aggro Patience gewonnenen Spielen mit KI Level 3 */
	public void setAggroPatienceWinsAi3(Double aggroPatienceWinsAi3) {
		this.aggroPatienceWins[3] = aggroPatienceWinsAi3;
	}
	/**  setzt neue Anzahl an in Aggro Patience gewonnenen Spielen mit einem Menschen
	 * @param aggroPatienceWinsHuman		neue Anzahl an in Aggro Patience gewonnenen Spielen mit einem Menschen */
	public void setAggroPatienceWinsHuman(Double aggroPatienceWinsHuman) {
		this.aggroPatienceWins[0] = aggroPatienceWinsHuman;
	}
	/** setzt für Aggro Patience gegen KI1 einen neuen Wert für die maximalen moves pro Zug
	 * @param aggroPatienceAi1LongestTurn neuer Wert für die maximalen moves pro Zug gegen KI1 */
	public void setAggroPatienceAi1LongestTurn(Double aggroPatienceAi1LongestTurn) {
		this.aggroPatienceLongestTurn[1] = aggroPatienceAi1LongestTurn;
	}
	/**	setzt für Aggro Patience gegen KI2 einen neuen Wert für die maximalen moves pro Zug
	 * @param aggroPatienceAi2LongestTurn neuer Wert für die maximalen moves pro Zug gegen KI2 */
	public void setAggroPatienceAi2LongestTurn(Double aggroPatienceAi2LongestTurn) {
		this.aggroPatienceLongestTurn[2] = aggroPatienceAi2LongestTurn;
	}
	/** setzt für Aggro Patience gegen KI3 einen neuen Wert für die maximalen moves pro Zug
	 * @param aggroPatienceAi3LongestTurn neuer Wert für die maximalen moves pro Zug gegen KI3 */
	public void setAggroPatienceAi3LongestTurn(Double aggroPatienceAi3LongestTurn) {
		this.aggroPatienceLongestTurn[3] = aggroPatienceAi3LongestTurn;
	}
	/** setzt für Aggro Patience gegen einen Menschen einen neuen Wert für die maximalen moves pro Zug
	 * @param aggroPatienceHumanLongestTurn		neuer Wert für die maximalen moves pro Zug gegen einen Menschen */
	public void setAggroPatienceHumanLongestTurn(Double aggroPatienceHumanLongestTurn) {
		this.aggroPatienceLongestTurn[0] = aggroPatienceHumanLongestTurn;
	}
	/** setzt die neue Dauer des schnellsten Sieges gegen KI1
	 * @param aggropatienceAi1FastestWin die neue Dauer des schnellsten Sieges gegen KI1 */
	public void setAggroPatienceAi1FastestWin(Long aggropatienceAi1FastestWin) {
		this.aggroPatienceFastestWin[1] = aggropatienceAi1FastestWin;
	}
	/** setzt die neue Dauer des schnellsten Sieges gegen KI2
	 * @param aggroPatienceAi2FastestWin   die neue Dauer des schnellsten Sieges gegen KI2 */
	public void setAggroPatienceAi2FastestWin(Long aggroPatienceAi2FastestWin) {
		this.aggroPatienceFastestWin[2] = aggroPatienceAi2FastestWin;
	}
	/** setzt die neue Dauer des schnellsten Sieges gegen KI3
	 * @param aggroPatienceAi3FastestWin	die neue Dauer des schnellsten Sieges gegen KI3 */
	public void setAggroPatienceAi3FastestWin(Long aggroPatienceAi3FastestWin) {
		this.aggroPatienceFastestWin[3] = aggroPatienceAi3FastestWin;
	}
	/**	setzt die neue Dauer des schnellsten Sieges gegen einen Menschen
	 * @param aggroPatienceHumanFastestWin	die neue Dauer des schnellsten Sieges gegen einen Menschen */
	public void setAggroPatienceHumanFastestWin(Long aggroPatienceHumanFastestWin) {
		this.aggroPatienceFastestWin[0] = aggroPatienceHumanFastestWin;
	}
	/** Setzt neuen Durchschnitt der übrig gebliebenen Karten gegen KI1
	 * @param aggroPatienceAi1CardsLeftAvrg	 neuer Durchschnitt der übrig gebliebenen Karten gegen KI1*/
	public void setAggroPatienceAi1CardsLeftAvrg(Double aggroPatienceAi1CardsLeftAvrg) {
		this.aggroPatienceCardsLeftAvrg[1] = aggroPatienceAi1CardsLeftAvrg;
	}
	/** Setzt neuen Durchschnitt der übrig gebliebenen Karten gegen KI2
	 * @param aggroPatienceAi2CardsLeftAvrg		neuer Durchschnitt der übrig gebliebenen Karten gegen KI2 */
	public void setAggroPatienceAi2CardsLeftAvrg(Double aggroPatienceAi2CardsLeftAvrg) {
		this.aggroPatienceCardsLeftAvrg[2] = aggroPatienceAi2CardsLeftAvrg;
	}
	/** Setzt neuen Durchschnitt der übrig gebliebenen Karten gegen KI3
	 * @param aggroPatienceAi3CardsLeftAvrg neuer Durchschnitt der übrig gebliebenen Karten gegen KI3 */
	public void setAggroPatienceAi3CardsLeftAvrg(Double aggroPatienceAi3CardsLeftAvrg) {
		this.aggroPatienceCardsLeftAvrg[3] = aggroPatienceAi3CardsLeftAvrg;
	}
	/**	Setzt neuen Durchschnitt der übrig gebliebenen Karten gegen einen Menschen
	 * @param aggroPatienceAi4CardsLeftAvrg		neuer Durchschnitt der übrig gebliebenen Karten gegen einen Menschen */
	public void setAggroPatienceHumanCardsLeftAvrg(Double aggroPatienceAi4CardsLeftAvrg) {
		this.aggroPatienceCardsLeftAvrg[0] = aggroPatienceAi4CardsLeftAvrg;
	}
	/** Gibt die aktuelle Anzahl an gespielten Idioten Patience Spielen zurück
	 * @return	aktuelle Anzahl an gespielten Idioten Patience Spielen */
	public Double getIdiotPatienceGames() {
		return idiotPatienceGames;
	}
	/**	Setzt eine neue Anzahl an gespielten Idioten Paience Spielen
	 * @param idiotPatienceGames	neue Anzahl an gespielten Idioten Paience Spielen */
	public void setIdiotPatienceGames(Double idiotPatienceGames) {
		this.idiotPatienceGames = idiotPatienceGames;
	}
	/**	Gibt die aktuelle Anzahl an gespielten Freecell Spielen zurück
	 * @return		aktuelle Anzahl an gespielten Freecell Spielen */
	public Double getFreecellGames() {
		return freecellGames;
	}
	/**	Setzt eine neue Anzahl an gespielten Freecell Spielen
	 * @param freecellGames neue Anzahl an gespielten Freecell Spielen */
	public void setFreecellGames(Double freecellGames) {
		this.freecellGames = freecellGames;
	}
	/**	Gibt die aktuelle Anzahl an gewonnenen Idioten Patience Spielen zurück
	 * @return	 aktuelle Anzahl an gewonnenen Idioten Patience Spielen */
	public Double getIdiotPatienceWins() {
		return idiotPatienceWins;
	}
	/**	Setzt eine neue Anzahl an gewonnenen Idioten Patience Spielen
	 * @param idiotPatienceWins		neue Anzahl an gewonnenen Idioten Patience Spielen */
	public void setIdiotPatienceWins(Double idiotPatienceWins) {
		this.idiotPatienceWins = idiotPatienceWins;
	}
	/**	Gibt die aktuelle Anzahl an gewonnenen Freecell Spielen zurück
	 * @return	aktuelle Anzahl an gewonnenen Freecell Spielen */
	public Double getFreecellWins() {
		return freecellWins;
	}
	/**	Setzt eine neue Anzahl an gewonnenen Freecell Spielen
	 * @param freecellWins		neue Anzahl an gewonnenen Freecell Spielen */
	public void setFreecellWins(Double freecellWins) {
		this.freecellWins = freecellWins;
	}
	/** Gibt die aktuelle maximale Anzahl an abgelegten Karten in Idioten Patience zurück
	 * @return		aktuelle maximale Anzahl an abgelegten Karten in Idioten Patience */
	public Double getIdiotPatienceMaxCards() {
		return idiotPatienceMaxCards;
	}
	/** setzt eine neue maximale Anzahl an abgelegten Karten in Idioten Patience
	 * @param idiotPatienceMaxCards		neue maximale Anzahl an abgelegten Karten in Idioten Patience */
	public void setIdiotPatienceMaxCards(Double idiotPatienceMaxCards) {
		this.idiotPatienceMaxCards = idiotPatienceMaxCards;
	}
	/** Gibt die aktuelle durchschnittliche Anzahl an abgelegten Karten in Idioten Patience zurück
	 * @return		aktuelle durchschnittliche Anzahl an abgelegten Karten in Idioten Patience */
	public Double getIdiotPatienceCardsLeftAvrg() {
		return idiotPatienceCardsLeftAvrg;
	}
	/**	setzt eine neue durchschnittliche Anzahl an abgelegten Karten in Idioten Patience
	 * @param idiotPatienceCardsLeftAvrg	neue durchschnittliche Anzahl an abgelegten Karten in Idioten Patience */
	public void setIdiotPatienceCardsLeftAvrg(Double idiotPatienceCardsLeftAvrg) {
		this.idiotPatienceCardsLeftAvrg = idiotPatienceCardsLeftAvrg;
	}
	/**	Gibt die aktuelle durchschnittliche Anzahl an abgelegten Karten in Freecell zurück
	 * @return		aktuelle durchschnittliche Anzahl an abgelegten Karten in Freecell */
	public Double getFreecellCardsLeftAvrg() {
		return freecellCardsLeftAvrg;
	}
	/**	setzt eine neue durchschnittliche Anzahl an abgelegten Karten in Freecell
	 * @param freecellCardsLeftAvrg		neue durchschnittliche Anzahl an abgelegten Karten in Freecell */
	public void setFreecellCardsLeftAvrg(Double freecellCardsLeftAvrg) {
		this.freecellCardsLeftAvrg = freecellCardsLeftAvrg;
	}
	/** gibt die aktuelle minimale Anzahl an Zügen in Freecell zurück
	 * @return	aktuelle minimale Anzahl an Zügen in Freecell */
	public Double getFreecellMinTurns() {
		return freecellMinTurns;
	}
	/** setzt eine neue minimale Anzahl an Zügen in Freecell
	 * @param freecellMinTurns	neue minimale Anzahl an Zügen in Freecell */
	public void setFreecellMinTurns(Double freecellMinTurns) {
		this.freecellMinTurns = freecellMinTurns;
	}
	/** Gibt die Dauer des aktuell schnellsten gewonnenen Spiels in Freecell zurück
	 * @return	Dauer des aktuell schnellsten gewonnenen Spiels in Freecell */
	public Long getFreecellFastestWin() {
		return freecellFastestWin;
	}
	/** setzt eine neue Dauer des schnellsten gewonnenen Spiels in Freecell
	 * @param freecellFastestWin	neue Dauer des schnellsten gewonnenen Spiels in Freecell  */
	public void setFreecellFastestWin(Long freecellFastestWin) {
		this.freecellFastestWin = freecellFastestWin;
	}
	/**	Gibt die Dauer des aktuell schnellsten gewonnenen Spiels in Idioten Patience zurück
	 * @return	Dauer des aktuell schnellsten gewonnenen Spiels in Idioten Patience */
	public Long getIdiotPatienceFastestWin() {
		return idiotPatienceFastestWin;
	}
	/**	setzt eine neue Dauer des schnellsten gewonnenen Spiels in Idioten Patience
	 * @param idiotPatienceFastestWin	neue Dauer des schnellsten gewonnenen Spiels in Idioten Patience */
	public void setIdiotPatienceFastestWin(Long idiotPatienceFastestWin) {
		this.idiotPatienceFastestWin = idiotPatienceFastestWin;
	}
	/**setzt neue Anzahl an Aggro Patience Spielen mit KI1
	 * @param aggroPatienceGamesAi1		neue Anzahl an Aggro Patience Spielen mit KI1 */
	public void setAggroPatienceGamesAi1(Double aggroPatienceGamesAi1) {
		this.aggroPatienceGames[1] = aggroPatienceGamesAi1;
	}
	/** gibt ein Array mit der aktuellen Anzahl aller Aggro Patience Spiele pro Modi zurück
	 * @return	Array mit der aktuellen Anzahl aller Aggro Patience Spiele pro Modi	 */
	public Double[] getAggroPatienceGames() {
		return aggroPatienceGames;
	}
	/** setzt ein neues	Array mit der Anzahl aller Aggro Patience Spiele pro Modi
	 * @param aggroPatienceGames	neues	Array mit der Anzahl aller Aggro Patience Spiele pro Modi	 */
	public void setAggroPatienceGames(Double[] aggroPatienceGames) {
		this.aggroPatienceGames = aggroPatienceGames;
	}
	/**	gibt ein Array mit der aktuellen Anzahl aller gewonnenen Aggro Patience Spiele pro Modi zurück
	 * @return	Array mit der aktuellen Anzahl aller gewonnenen Aggro Patience Spiele	 */
	public Double[] getAggroPatienceWins() {
		return aggroPatienceWins;
	}
	/**	setzt ein neues	Array mit der Anzahl aller gewonnenn Aggro Patience Spiele pro Modi
	 * @param aggroPatienceWins	neues	Array mit der Anzahl aller gewonnenn Aggro Patience Spiele pro Modi	 */
	public void setAggroPatienceWins(Double[] aggroPatienceWins) {
		this.aggroPatienceWins = aggroPatienceWins;
	}
	/**	gibt ein Array mit den meisten Kartenzügen in einer Runde in allen Aggro Patience Spiele zurück
	 * @return		Array mit den meisten Kartenzügen in einer Runde in allen Aggro Patience Spiele 	 */
	public Double[] getAggroPatienceLongestTurn() {
		return aggroPatienceLongestTurn;
	}
	/** setzt ein Array mit den meisten Kartenzügen in einer Runde in allen Aggro Patience Modi
	 * @param aggroPatienceLongestTurn 	 neues Array mit den meisten Kartenzügen in einer Runde in allen Aggro Patience Modi */
	public void setAggroPatienceLongestTurn(Double[] aggroPatienceLongestTurn) {
		this.aggroPatienceLongestTurn = aggroPatienceLongestTurn;
	}
	/** gibt ein Array aller am schnellsten gewonnenen Spiele in Aggro Patience zurückt
	 * @return	Array aller am schnellsten gewonnenen Spiele in Aggro Patience	 */
	public Long[] getAggroPatienceFastestWin() {
		return aggroPatienceFastestWin;
	}
	/**	setzt ein Array aller am schnellsten gewonnenen Spiele in Aggro Patience
	 * @param aggroPatienceFastestWin	neues Array aller am schnellsten gewonnenen Spiele in Aggro Patience
	 */
	public void setAggroPatienceFastestWin(Long[] aggroPatienceFastestWin) {
		this.aggroPatienceFastestWin = aggroPatienceFastestWin;
	}
	/**	gibt ein Array aller durchschnittlich übergebliebenen Karten in Aggro Patience zurückt
	 * @return	Array aller durchschnittlich übergebliebenen Karten in Aggro Patience	 */
	public Double[] getAggroPatienceCardsLeftAvrg() {
		return aggroPatienceCardsLeftAvrg;
	}
	/** setzt ein Array aller durchschnittlich übergebliebenen Karten in Aggro Patience
	 * @param aggroPatienceCardsLeftAvrg	neues Array aller durchschnittlich übergebliebenen Karten in Aggro Patience	*/
	public void setAggroPatienceCardsLeftAvrg(Double[] aggroPatienceCardsLeftAvrg) {
		this.aggroPatienceCardsLeftAvrg = aggroPatienceCardsLeftAvrg;
	}
}