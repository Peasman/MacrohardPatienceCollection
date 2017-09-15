package ai.general;

import java.util.function.Supplier;

public interface PatienceAI extends Supplier<Result> {

	public boolean isRunning();
	
	public void shutdown();
	
	public Result getHint();
	
}
