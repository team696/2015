
public class ExtendedCommand extends Command{
	
	int counter = 0;
	
	public ExtendedCommand(int arg1, int arg2, int arg3, boolean _parallel) {
		counter = arg1;
		counter += arg2;
		counter += arg3;
		parallel = _parallel;
	}

	@Override
	public void update(){
		counter--;
		if(counter< 0) isFinished = true;
		System.out.println("running:   " + counter);
	}
}
