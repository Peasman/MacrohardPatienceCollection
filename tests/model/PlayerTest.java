package model;

import org.junit.Before;
import org.junit.Test;

/**	Diese Klasse testet ob der Player korrekt erstellt wird
 * 	
 * 	@author Christian Walczuch, Olga Scheftelowitsch
 */


public class PlayerTest {
	/**
	 * name, vom player der beim setUp erstellt wird
	 */
	private String name;
	/**
	 * typ vom Player, der beim setUp erstellt wird
	 */
	private PlayerType type;
	/**
	 * Player, der beim setUp erstellt wird
	 */
	private Player player;
	
	/*
	 * 	initialisiert die für den Test notwendigen Parameter
	 */
	
	/**
	 * Player wird gespeichert, so wie seine Attribute
	 */
	@Before
	public void setUp(){
		name = "Peter";
		type = PlayerType.HUMAN;
		player = new Player(type, name);
	}
	
	/*
	 * 	testet ob der Player korrekt erstellt wurde
	 */
	
	/**
	 * es wird geprüft, ob die Attribute des gespeicherten Players gleich den im Konstruktor bergebenen
	 */
	@Test
	public void testPlayer(){
		assert(player.getName().equals(name));
		assert(player.getType().equals(type));
	}
}
