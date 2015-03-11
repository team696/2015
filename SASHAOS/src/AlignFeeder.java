
public class AlignFeeder extends Command {

int counter = 0;
	
	public AlignFeeder(){
		System.out.println("aligning totes");
		counter = 16;
	}

	@Override
	public void update(){
		counter--;
		if(counter< 0) isFinished = true;
		System.out.println("aligning:   " + counter);
	}
	
}
