public class Command extends Runnable{
	boolean parallel = false;
	boolean isFinished;
	
	public Command(){
		parallel = false;
	}
	
	@Override
	public void update(){
	
	}
	public boolean finished(){
		return isFinished;
	}
	public boolean isParallel(){
		return parallel;
	}
}
