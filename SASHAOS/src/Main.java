
public class Main {

	public static void main(String[] args) {
		Scheduler scheduler = new Scheduler();
		
		scheduler.start(100);
		scheduler.addCommand(new Command(50, false));
		
	}

}
