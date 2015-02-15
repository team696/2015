
public class navigate extends Command{
	
	int counter = 0;
	
	public navigate(Double arg1,Double arg2,Double arg3, Boolean _parallel) {

		counter+= arg1;
		counter+= arg2;
		counter+= arg3;
		parallel = _parallel;
	}
	
	public navigate(){
		counter = 20;
		parallel = false;
	}

	@Override
	public void update(){
		counter--;
		if(counter< 0) isFinished = true;
		System.out.println("running:   " + counter);
	}
}
