package org.team696.subsystems;

import org.team696.Commands.openintake;
import org.team696.baseClasses.Runnable;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class Intake extends Runnable{
	VictorSP rightIn;
	VictorSP leftIn;
	Solenoid ejector;
	Solenoid open;
	
	boolean eject = false;
	boolean intake = false;
	double speed;
	boolean intakeOpen;
	
	/*
	 * @param config - ejectorChan, rightInChan, leftInChan
	 */
	public Intake(int ejectorSolenoid, int openSolenoid, int rightIntakeVictor, int leftIntakeVictor) {
		ejector = new Solenoid(ejectorSolenoid);
		open = new Solenoid(openSolenoid);
		
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
	
	@Override
	public void stop(){
		super.stop();
	}
	
	public void set(boolean _eject, boolean _open, boolean _intake) {
		eject = _eject;
		intake = _intake;
		intakeOpen = _open;
		
		if (eject == intake) {
			intake = false;
			eject = false;
		}
		
		if (eject) {
//			ejector.set(true);
			speed = 1;
		} else  if (intake) {
//			ejector.set(false);
			speed = -1;
		} else{
//			ejector.set(false);
			speed = 0;
		}
		
	}
	
	private void intakeOpen(){
		open.set(intakeOpen);
	}
	
	public void motors() {
		rightIn.set(-speed);
		leftIn.set(speed);
	}
	
	public void run() {
		motors();
	}
}
