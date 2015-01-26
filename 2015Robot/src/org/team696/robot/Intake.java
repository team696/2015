package org.team696.robot;

import org.team696.baseClasses.Runnable;

public class Intake extends Runnable{
	
	
	boolean grabBin = false;
	
	@Override 
	public void start(int periodMS) {
		super.start(periodMS);
	}
	
	@Override
	public void update() {
		
	}
	
	public void setGrabBin(boolean _grabBin) {
		grabBin = _grabBin;
	}
	
	public void run() {
		
	}
}
