package org.usfirst.frc.team696.robot;

import java.util.Timer;
import java.util.TimerTask;

public class JavaReminder {
	Timer timer;
	
	public JavaReminder(int seconds) {
		timer = new Timer();
		timer.schedule(new RemindTask(), seconds * 1000);
	}
	
	class RemindTask extends TimerTask {
		
		@Override
		public void run() {
			
		}
	}
}