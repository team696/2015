package org.team696.robot;

import org.team696.baseClasses.Runnable;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class Intake extends Runnable{
	VictorSP rightIn;
	VictorSP leftIn;
	Solenoid ejector;
	Solenoid grabberOfBin;
	
	boolean grabBin = false;
	boolean eject = false;
	boolean intake = false;
	double speed;
	
	/*
	 * @param config - ejectorChan, grabBinChan, rightInChan, leftInChan
	 */
	public Intake(int[] config) {
		ejector = new Solenoid(config[0]);
		grabberOfBin = new Solenoid(config[1]);
		
		rightIn = new VictorSP(config[2]);
		leftIn = new VictorSP(config[3]);
	}
	
	@Override 
	public void start(int frequency) {
		super.start(frequency);
	}
	
	@Override
	public void update() {
		run();
	}
	
	/*
	 * @param set - eject, intake, grabBin
	 */
	public void setIntake(boolean[] set) {
		if (set[0] == set[1]) {
			set[0] = false;
			set[1] = false;
		}
		eject = set[0];
		intake = set[1];
		grabBin = set[2];
	}
	
	public void setSpeed(double _speed) {
		speed = _speed;
	}
	
	public void motors() {
		rightIn.set(speed);
		leftIn.set(speed);
	}
	
	public void run() {
		if (grabBin) grabberOfBin.set(true);
		else grabberOfBin.set(false);
		
		if (eject) {
			ejector.set(true); 
			speed = -1;
		} else  if (intake) {
			ejector.set(false);
			speed = 1;
		} else{
			ejector.set(false);
			speed = 0;
		}
		motors();		
	}
}
