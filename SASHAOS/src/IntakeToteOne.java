
public class IntakeToteOne extends Command {
	
	private double counter;
	
	public IntakeToteOne(){
		System.out.println("intaking first tote");
		counter = 20;
		parallel = false;
	}

	@Override
	public void update(){
		counter--;
		if(counter< 0) isFinished = true;
		System.out.println("firstTote:   " + counter);
	}
}
