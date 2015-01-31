import javax.swing.GroupLayout.ParallelGroup;


public class Command extends Runnable{
	
	int counter =0;
	boolean parallel = false;
	boolean isFinished;
	
	public Command(){
		counter = 0;
		parallel = false;
	}
	
	public Command(int arg1, int arg2, int arg3, boolean _parallel){
		counter = arg1;
		counter += arg2;
		counter += arg3;
		parallel = _parallel;
	}
	
	@Override
	public void update(){
		counter--;
		System.out.println(counter);
		if(counter <0) isFinished = true;
	}
	public boolean finished(){
		return isFinished;
	}
	public boolean isParallel(){
		return parallel;
	}
}
