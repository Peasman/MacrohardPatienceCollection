package model;

import java.io.Serializable;

/**
 * Aufzählung aller CardStackTypes
 * @author Moritz Ludolph, Simon Kurz
 */
public enum CardStackType  implements Serializable{
	TALON(-1, CardStackSuperType.TALON, ""), 
	ZANK_LEFT_1(1, CardStackSuperType.ZANK_LEFT, "B6"), 
	ZANK_LEFT_2(2, CardStackSuperType.ZANK_LEFT, "C1"), 
	ZANK_RIGHT_1(1, CardStackSuperType.ZANK_RIGHT, "D6"), 
	ZANK_RIGHT_2(2, CardStackSuperType.ZANK_RIGHT, "A1"), 
	ZANK_MIDDLE_1(1, CardStackSuperType.ZANK_MIDDLE, "C6"), 
	ZANK_MIDDLE_2(2, CardStackSuperType.ZANK_MIDDLE, "B1"), 
	FREECELL_1(1, CardStackSuperType.FREECELL, ""), 
	FREECELL_2(2, CardStackSuperType.FREECELL,""), 
	FREECELL_3(3, CardStackSuperType.FREECELL,""), 
	FREECELL_4(4, CardStackSuperType.FREECELL,""), 
	ROW_1(1, CardStackSuperType.ROW, "A2"), 
	ROW_2(2, CardStackSuperType.ROW, "A3"), 
	ROW_3(3, CardStackSuperType.ROW, "A4"), 
	ROW_4(4, CardStackSuperType.ROW,"A5"), 
	ROW_5(5, CardStackSuperType.ROW, "D2"), 
	ROW_6(6, CardStackSuperType.ROW, "D3"), 
	ROW_7(7, CardStackSuperType.ROW, "D4"), 
	ROW_8(8, CardStackSuperType.ROW, "D5"), 
	STACKER_1(1, CardStackSuperType.STACKER, "B2"), 
	STACKER_2(2, CardStackSuperType.STACKER, "B3"), 
	STACKER_3(3, CardStackSuperType.STACKER, "B4"), 
	STACKER_4(4, CardStackSuperType.STACKER, "B5"), 
	STACKER_5(5, CardStackSuperType.STACKER, "C2"), 
	STACKER_6(6, CardStackSuperType.STACKER, "C3"), 
	STACKER_7(7, CardStackSuperType.STACKER, "C4"), 
	STACKER_8(8, CardStackSuperType.STACKER, "C5");
	
	private int num = -1;
	private CardStackSuperType superType;
	private String protocolName;
	private CardStackType(int num, CardStackSuperType superType, String protocolName){
		this.num = num;
		this.superType = superType;
		this.protocolName = protocolName;
	}
	/**
	 * Gibt für einen Stapeltyp den zugehörigen Protokolnamen
	 * @return Der Protokolname des Stapels
	 */
	public String toProtocol()
	{
		return protocolName;
	}
	
	/**
	 * Gibt für einen übergeben Protokollnamen den Stapeltyp zurück
	 * @param string Der Protokollname
	 * @return Der zugehörige CardStackType, null für ungültige Protokollnamen
	 */
	public static CardStackType fromProtocol(String string){
		for (CardStackType type : CardStackType.values()){
			if (type.toProtocol().equals(string)) {
				return type;
			}
		}
		return null;
	}
	
	/**
	 * Gibt die zugeordnete Zahl zurück
	 * @return Die zugeordnete Zahl, -1 falls keine zugeordnet wurde
	 */
	public int getNum(){
		return num;
	}
	
	/**
	 * Die Aufgabe des CardSTacks
	 * @return Gibt die Aufgabe des Stacks als CardStackSuperType zurück.
	 * @see CardStackSuperType
	 */
	public CardStackSuperType getSuperType() {
		return superType;
	}
}
