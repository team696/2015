
public class navigate extends Command{
	
	int counter = 0;
	
	public navigate(Double arg1,Double arg2,Double arg3, Boolean _parallel) {

		
		System.out.println("navigating to: " + arg1+ ",   " + arg2 + ",   " + arg3);
		counter+= arg1;
		counter+= arg2;
		counter+= arg3;
		parallel = _parallel;
	}
	
	public navigate(){
		System.out.println("navigating to: 0, 0, 0");
		
		counter = 0;
		parallel = false;
	}

	@Override
	public void update(){
		counter--;
		if(counter< 0) isFinished = true;
		System.out.println("running:   " + counter);
	}
}
