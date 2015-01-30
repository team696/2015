
public class Command extends Runnable{
	
	int counter;
	boolean parellel;
	boolean isFinished;
	
	public Command(int arg, boolean _parallel){
		counter = arg;
		parellel = _parallel;
	}
	
	@Override
	public void update(){
		counter--;
		System.out.println(counter);
	}
	public boolean finished(){
		updater.cancel();
		return isFinished;
	}
}
