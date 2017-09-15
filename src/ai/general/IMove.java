package ai.general;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import model.GameMove;

public class IMove implements Iterable<GameMove>, Serializable {
	private static final long serialVersionUID = 8208363566200267068L;
	private ArrayList<GameMove> moves;

	public IMove(GameMove... moves) {
		this.moves = new ArrayList<>();
		for (GameMove move : moves) {
			this.moves.add(move);
		}
	}

	public ArrayList<GameMove> getMoves() {
		return moves;
	}

	public void setMoves(ArrayList<GameMove> moves) {
		this.moves = moves;
	}

	public int size() {
		return moves.size();
	}

	@Override
	public Iterator<GameMove> iterator() {
		return new Iterator<GameMove>() {
			int currentIndex = size() - 1;

			@Override
			public boolean hasNext() {
				return currentIndex >= 0;
			}

			@Override
			public GameMove next() {
				if (!hasNext())
					throw new NoSuchElementException();

				GameMove move = moves.get(currentIndex);
				currentIndex--;
				return move;
			}
		};
	}
}
