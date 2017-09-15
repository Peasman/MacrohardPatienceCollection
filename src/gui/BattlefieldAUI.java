package gui;

import model.Player;

public interface BattlefieldAUI {
	public void onTurnBegin(Player playerOfTurn);
	public void onTurnEnd(Player playerOfTurn);
	public void onGameStart();
	public void onGameFinished();
	public void onAIMove(boolean end);
	
}
