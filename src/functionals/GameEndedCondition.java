package functionals;

import model.Battlefield;

/**
 * Stellt eine Bedingung dar, ob das Spiel gewonnen /verloren ist.
 * @author Simon Kurz
 *
 */
public interface GameEndedCondition {

	/**
	 * Gibt zurück ob das Spiel gewonnen ist.
	 * @param battlefield Das Battlefield, was überprüft werden soll
	 * @return <i> true </i> wenn gewonnen  </br>
	 * 		   <i> false </i> wenn nicht gewonnen.
	 */
	public boolean hasWon(Battlefield battlefield);
	/**
	 * Gibt zurück, ob das Spiel verloren ist.
	 * @param battlefield Das battlefield, das überprüft werden soll
	 * @return <i> true </i> wenn verloren  </br>
	 * 		   <i> false </i> wenn nicht verloren
	 */
	public boolean hasLost(Battlefield battlefield);
}
