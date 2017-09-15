package gui;

import java.util.Random;

import controller.IOController;
import model.GameMove;

/**
 * Aktualisierungsmethoden für GUI bei Moves
 * @author moritz
 */
public interface MoveAUI {

	/**
	 * Wird vor der Ausfürhung eines Moves aufgerufen
	 * @param move Der auszuführende Move
	 */
	public void beforeMove(GameMove move, boolean implicit);
	/**
	 * Wird nach einem ausgeführten Move aufgerufen
	 * @param move Der ausgeführte Move
	 */
	public void afterMove(GameMove move, boolean implicit);
	
	public default void playMoveSound(int moveId) {
		if(IOController.getProperty("lastSound").compareTo(""+moveId) != 0) {
			Random rng = new Random();
			IOController.playSound("/card"+rng.nextInt(12)+".wav");
			IOController.setProperty("lastSound", ""+moveId);
		}
	}
}