package ai.general;

/** ein Interface mit einer Methode zum Bewerten
 * @author Olga Scheftelowitsch
 * @param <T> eine Objekt, dass von IGameState erbt
 */
public interface Evaluator<T extends IGameState> {

	/** gibt zu eimen Objekt, welches IGameState extended eine Bewertung
	 * @param toEvaluate übergebenes Objekt, welches von IGameState erbt
	 * @return eine Bewertung in Form eines ints 
	 */
	int evaluate(T toEvaluate);
	
}
