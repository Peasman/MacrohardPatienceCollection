package functionals;

import model.Battlefield;
import model.GameMove;

/**
 * Ein interface um Regeln zu generalisieren.
 * @author Simon Kurz
 *
 */
public interface GameRule {
	/**
	 * Überprüft, ob ein Move gültig ist.
	 * @param battlefield Das Battlefield auf dem der Move ausgeführt werden soll.
	 * @param move Der Move der überpüft werden soll
	 * @return  <i> true </i> wenn valider move  </br>
	 *          <i> false </i> wenn move ungültig
	 */
	public abstract boolean isValid(Battlefield battlefield, GameMove move);

}
