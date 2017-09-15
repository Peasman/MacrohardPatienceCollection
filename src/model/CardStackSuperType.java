package model;

import java.io.Serializable;

/**
 * Die Funktionsweise eines Kartenstapels
 */
public enum CardStackSuperType  implements Serializable{
	TALON, ROW, STACKER, ZANK_LEFT, ZANK_MIDDLE, ZANK_RIGHT, FREECELL;
}
