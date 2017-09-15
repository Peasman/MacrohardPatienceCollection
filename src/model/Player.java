package model;

import java.io.Serializable;

/**
 * 
 * Der Spieler (Mensch oder KI) mit seinem Namen
 * @author Alexander Herlez, Olga Scheftelowitsch
 */
public class Player implements Serializable {
	private static final long serialVersionUID = -6380627361534891862L;
	private String name;
	private long identifier;
	private PlayerType type;
	
	
	/**
	 * der Spieler wird instanziert
	 * @param type der Typ des Spielers @link PlayerType
	 * @param name der Name des Spielers
	 */
	public Player(PlayerType type, String name) {
		this.type = type;
		this.name = name;
		this.identifier = (long)(((double) Long.MAX_VALUE) * Math.random());
	}
	
	public void setName(String newName) {
		name = newName;
	}
	
	/**
	 * gibt den Namen des Spielers zurück
	 * @return der Name
	 */
	public String getName() {
		return name;
	}

	/**
	 * gibt den Typen des Spielers zurück @link PlayerType
	 * @return Typ des Spielers
	 */
	public PlayerType getType() {
		return type;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (identifier ^ (identifier >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		if (identifier != other.identifier)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
