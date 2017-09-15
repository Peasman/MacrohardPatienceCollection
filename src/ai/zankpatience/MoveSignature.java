package ai.zankpatience;

import model.Battlefield;
import model.CardStackType;
import model.GameMove;

public class MoveSignature {
	
	private CardStackType fromStack;
	private CardStackType toStack;
	private int fromStackSize;
	private int toStackSize;
	private boolean playedCardIsRed;
	private int playedCardValue;
	
	public MoveSignature(GameMove move, Battlefield field) {
		this.fromStack = move.getFrom();
		this.toStack = move.getTo();
		this.fromStackSize = field.getStack(move.getFrom()).size();
		this.toStackSize = field.getStack(move.getTo()).size();
		this.playedCardIsRed = move.getCards().get(0).isRed();
		this.playedCardValue = move.getCards().get(0).getRank().getRankValue();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fromStack == null) ? 0 : fromStack.hashCode());
		result = prime * result + fromStackSize;
		result = prime * result + (playedCardIsRed ? 1231 : 1237);
		result = prime * result + playedCardValue;
		result = prime * result + ((toStack == null) ? 0 : toStack.hashCode());
		result = prime * result + toStackSize;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		MoveSignature other = (MoveSignature) obj;
		if (fromStack != other.fromStack) {
			return false;
		}
		if (fromStackSize != other.fromStackSize) {
			return false;
		}
		if (playedCardIsRed != other.playedCardIsRed) {
			return false;
		}
		if (playedCardValue != other.playedCardValue) {
			return false;
		}
		if (toStack != other.toStack) {
			return false;
		}
		if (toStackSize != other.toStackSize) {
			return false;
		}
		return true;
	}
	
	
	
}
