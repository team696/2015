package org.team696.subsystems;

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
	public Intake(int ejectorSolenoid, int grabberSolenoid, int rightIntakeVictor, int leftIntakeVictor) {
		ejector = new Solenoid(ejectorSolenoid);
		grabberOfBin = new Solenoid(grabberSolenoid);
		
		rightIn = new VictorSP(rightIntakeVictor);
		leftIn = new VictorSP(leftIntakeVictor);
	}
	
	@Override 
	public void start(int periodMS) {
		super.start(periodMS);
	}
	
	@Override
	public void update() {
		run();
	}
	
	public void setIntake(boolean eject, boolean intake, boolean _grabBin, double _speed) {
		if (eject == intake) {
			intake = false;
			eject = false;
		}
		grabBin = _grabBin;
		speed = _speed;
	}
	
	public void motors() {
		rightIn.set(speed);
		leftIn.set(speed);
	}
	
	public void run() {
		grabberOfBin.set(grabBin);
		
		if (eject) {
			ejector.set(true); 
			speed = -0.75;
		} else  if (intake) {
			ejector.set(false);
			speed = 0.75;
		} else{
			ejector.set(false);
			speed = 0;
		}
		motors();		
	}
}
