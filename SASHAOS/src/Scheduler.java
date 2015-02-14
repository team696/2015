

import java.util.Enumeration;
import java.util.Vector;

import javax.print.attribute.standard.Finishings;


public class Scheduler extends Runnable{
	
	Vector<Command> curCommands = new Vector<Command>();
	Interpreter interpreter = new Interpreter("navigate: 3 , 4, 30, sequential \n navigate: 2 , 2, 0, parallel");
	public Scheduler(){
		
	}
	
	public void addCommand(Command command,int periodMS){
		curCommands.addElement(command);
		curCommands.lastElement().start(periodMS);
		
	}
	
	@Override 
	public void start(int periodMS){
		super.start(periodMS);
	}
	@Override
	public void update(){
		boolean threadFree = true;
		int fish=0;
		while(fish<curCommands.size()){
			if(curCommands.get(fish).finished()){
				curCommands.get(fish).stop();
				curCommands.remove(fish);
			}else{
				if(!curCommands.get(fish).isParallel()) threadFree = false;
				fish++;
			}
		}
		
		if(threadFree && interpreter.hasNextLine()){
			curCommands.add(interpreter.nextLine());
			curCommands.lastElement().start(100);
			
		}
		
		if(curCommands.size()<1 && !interpreter.hasNextLine()){
			System.out.println("stopping");
			stop();
		}
	}
}
