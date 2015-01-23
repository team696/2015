package org.team696.baseClasses;

import java.util.Timer;
import java.util.TimerTask;

public class Runnable {
	Timer updater = new Timer();
	TimerTask updateTask = new TimerTask() {
		@Override
		public void run() {
			update();
		}
	};
	public void update(){
		System.out.print("running super: ");
	}
	public void start(){
		updater.schedule(updateTask, 1, 1000);
		//schedule the update function
	}
	
	
}
