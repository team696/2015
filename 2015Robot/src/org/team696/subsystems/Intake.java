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
	public Intake(int ejectorSolenoid, int grabberSolenoid, int rightIntake, int leftIntake) {
		ejector = new Solenoid(ejectorSolenoid);
		grabberOfBin = new Solenoid(grabberSolenoid);
		
		rightIn = new VictorSP(rightIntake);
		leftIn = new VictorSP(leftIntake);
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
	public void setIntake(boolean eject, boolean intake, boolean _grabBin) {
		if (eject == intake) {
			intake = false;
			eject = false;
		}
		grabBin = _grabBin;
	}
	
	public void setSpeed(double _speed) {
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
