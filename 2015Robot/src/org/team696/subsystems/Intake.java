package org.team696.subsystems;

import org.team696.baseClasses.Runnable;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class Intake extends Runnable{
	VictorSP rightIn;
	VictorSP leftIn;
	//Solenoid ejector;
	//Solenoid grabberOfBin;
	
	boolean grabBin = false;
	boolean eject = false;
	boolean intake = false;
	double speed;
	
	/*
	 * @param config - ejectorChan, grabBinChan, rightInChan, leftInChan
	 */
	public Intake(int ejectorSolenoid, int grabberSolenoid, int rightIntakeVictor, int leftIntakeVictor) {
//		ejector = new Solenoid(ejectorSolenoid);
//		grabberOfBin = new Solenoid(grabberSolenoid);
		
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
	
	public void setIntake(boolean _eject, boolean _intake, boolean _grabBin, double _speed) {
		eject = _eject;
		intake = _intake;
		speed = _speed;
		
		if (eject == intake) {
			intake = false;
			eject = false;
		}
		
		if (eject) {
//			ejector.set(true); 
			speed = speed;
		} else  if (intake) {
//			ejector.set(false);
			speed = -speed;
		} else{
//			ejector.set(false);
			speed = 0;
		}
		
		grabBin = _grabBin;
		}
	
	public void motors() {
		System.out.println(speed);
		rightIn.set(-speed);
		leftIn.set(speed);
	}
	
	public void run() {
//		grabberOfBin.set(grabBin);
		
//		if (eject) {
////			ejector.set(true); 
//			speed = speed;
//		} else  if (intake) {
////			ejector.set(false);
//			speed = -speed;
//		} else{
////			ejector.set(false);
//			speed = 0;
//		}
		motors();
	}
}
