
public class Main {

	public static void main(String[] args) {
		Scheduler scheduler = new Scheduler();
		
		scheduler.start(100);
		try{
		Thread.sleep(15000);
		}catch(InterruptedException e){}
		System.out.println("stopping");
		scheduler.stop();
	}

}
