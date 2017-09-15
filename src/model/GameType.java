package model;

import java.io.Serializable;

/**
 * Aufzählung aller realisierten Patience-Spielarten
 * @author Moritz Ludolph
 */
public enum GameType  implements Serializable{
	AGGRO_PATIENCE, IDIOT_PATIENCE, FREECELL;

	/**
	 * Deutscher Name der Spielart 
	 * @return Gibt den Name als String zurück
	 */
	public String toLocale() {
		switch (this) {
		case AGGRO_PATIENCE:
			return "Zank-Patience";
		case IDIOT_PATIENCE:
			return "Idioten-Patience";
		case FREECELL:
			return "FreeCell";
		default:
			return "Game";
		}
	}
}
