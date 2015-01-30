package org.team696.robot;

import org.team696.baseClasses.Runnable;

import edu.wpi.first.wpilibj.Solenoid;

public class AutoCanner extends Runnable {
	Solenoid leftGrab;
	Solenoid rightGrab;
	
	boolean leftOut = false;
	boolean rightOut = false;
	
	/*
	 * @param config - leftGrab, rightGrab
	 */
	public AutoCanner(int[] config) {
		leftGrab = new Solenoid(config[0]);
		rightGrab = new Solenoid(config[1]);
	}
	
	@Override
	public void start(int frequency) {
		super.start(frequency);
	}
	
	@Override
	public void update() {
		run();
	}
	
	public void set(boolean[] extend) {
		leftOut = extend[0];
		rightOut = extend[1];
	}
	
	public void run() {
		leftGrab.set(leftOut);
		rightGrab.set(rightOut);
	}
}