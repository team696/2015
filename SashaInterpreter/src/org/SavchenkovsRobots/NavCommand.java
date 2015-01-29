package org.SavchenkovsRobots;

public class NavCommand {
	static int counter;
	static boolean finished;
	public NavCommand(){
		
	}
	public static boolean nav(){
		counter++;
		System.out.println(counter);
		if(counter>696) finished = true;
		return finished;
	}
}
