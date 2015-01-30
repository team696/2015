import java.util.Enumeration;
import java.util.Vector;


public class Scheduler extends Runnable{
	
	Vector<Command> curCommands = new Vector<Command>();
	
	public Scheduler(){
		
	}
	
	public void addCommand(Command command){
		curCommands.addElement(command);
	}
	
	@Override 
	public void start(int periodMS){
		super.start(periodMS);
	}
	@Override
	public void update(){
		if(curCommands.isEmpty()) return;
		for(int fish = curCommands.size(); fish>0; fish--){
			if(curCommands.get(fish).finished()){
				curCommands.remove(fish);
				fish++;
			}
		}			
		
		
		
	}
}
