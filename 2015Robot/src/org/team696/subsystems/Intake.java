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
		rightIn.set(speed);
		leftIn.set(-speed);
		open.set(intakeOpen);
	}
	
	public void setMotors(double _speed) {
		speed = _speed;
	}
	public void setOpen(boolean _open){
		intakeOpen = _open;
	}
	public void setEjector(boolean _eject){
		ejector.set(_eject);
	}
}
