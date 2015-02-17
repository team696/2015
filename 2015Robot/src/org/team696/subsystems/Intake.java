package org.team696.subsystems;

import org.team696.Commands.openintake;
import org.team696.baseClasses.Runnable;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;

public class Intake extends Runnable{
	VictorSP rightIn;
	VictorSP leftIn;
	Solenoid ejector;
	Solenoid open;
	
	Timer timer = new Timer();
	
	boolean eject = false;
	boolean intake = false;
	double speed = 0;
	boolean intakeOpen = true;
	boolean isOpen = false;
	boolean lastIntakeOpen = true;
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
		timer.start();
		super.start(periodMS);
		
	}
	
	@Override
	public void update() {
		rightIn.set(speed);
		leftIn.set(-speed);
		open.set(!intakeOpen);
		ejector.set(eject);
		if(intakeOpen && !lastIntakeOpen) timer.reset();
		
		isOpen = timer.get()>0.6 && intakeOpen;
		
		lastIntakeOpen = intakeOpen;
	}
	
	@Override
	public void stop(){
		super.stop();
	}
	
	public void setMotors(double _speed) {
		speed = _speed;
	}
	public void setOpen(boolean _open){
		intakeOpen = _open;
	}
	public void setEjector(boolean _eject){
		eject = _eject;
		ejector.set(eject);
	}
	public void toggle(){
		intakeOpen=!intakeOpen;
	}
	public boolean isOpen(){
		return isOpen;
	}
}
