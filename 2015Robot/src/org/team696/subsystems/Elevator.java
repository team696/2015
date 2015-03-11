package org.team696.subsystems;

import org.team696.baseClasses.CustomPID;
import org.team696.baseClasses.Runnable;
import org.team696.baseClasses.Util;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;

public class Elevator extends Runnable {
	
	public enum Presets{
		BOTTOM,
		ONE_TOTE_HIGH,
		ABOVE_INTAKE,
		TOP
	}
	public Presets presets = Presets.BOTTOM;
	
	Encoder encoder;
	
	DigitalInput limitSwitchBot;
	DigitalInput limitSwitchTop;
	VictorSP elevMotor1;
	VictorSP elevMotor2;
	Solenoid brake;
	Intake intake;
	
	boolean intakeOpen = true;
	boolean intakeOverride = false;
	
	double goalPos = 0.0;
	boolean override = true;
	boolean totalOverride = false;
	
	double clicksPerTote = 0.00146484375;
	double curSetSpeed = 0.0;
	/*
	 * @param config - encoderSlotA, encoderSlotB, limitSwitchBot, limitSwitchTop, BreakerChannel
	 */
	public Elevator() {
		encoder = new Encoder(0, 1);
		encoder.setSamplesToAverage(10);
		
		limitSwitchBot = new DigitalInput(10);
		limitSwitchTop = new DigitalInput(11);

		brake = new Solenoid(4);
		intake = new Intake(6,5, 4, 1);
		elevMotor1 = new VictorSP(2);
		elevMotor2 = new VictorSP(3);
		encoder.setDistancePerPulse(clicksPerTote);
//		lastDistance = encoder.getDistance();
	}
	
	@Override
	public void start(int periodMS){
		intake.start(20);
		super.start(periodMS);
	}
	
	@Override
	public void update() {
		if(!override) presetMotion();
		runElevator();
	}

	
	public void overrideMotion(double _speed){
		override = true;
		if(_speed <curSetSpeed) curSetSpeed += Util.constrain(_speed, -0.01, 0.01); 
		else curSetSpeed +=Util.constrain(_speed, -0.1, 0.1);
	}
	
	public void goToPreset(Presets _preset){
		override = false;
		presets =_preset;
	}
	public void setIntakeMotors(double _speed){
		intake.setMotors(_speed);
	}
	
	public void setIntakeOpen(boolean _open){
		intakeOpen = _open;
	}
	public void setEjector(boolean _eject){
		intake.setEjector(_eject);
	}	
	
	private void presetMotion(){
		double target;
		
		switch(presets){
		case BOTTOM:		target = 0.1;
		case ONE_TOTE_HIGH:	target = 1;
		case ABOVE_INTAKE:	target = 3;
		case TOP:			target = 5;
		default:			target = 0.1; 
		}
		
		if(target-getPosition()>0.2)	overrideMotion(1);
		else if(target-getPosition()<0.2) overrideMotion(-0.5);
		else overrideMotion(0);
	}
	
	private void runElevator(){
		
		boolean tempBottomSwitch = !limitSwitchBot.get();
		boolean tempTopSwitch = !limitSwitchTop.get();
		double tempDistance = encoder.getDistance();
		
		if(curSetSpeed>0){            //if we are moving upwards
			if(tempDistance>4){
				if(tempTopSwitch) setSpeed(0);
				else{
					curSetSpeed = Util.constrain(curSetSpeed,0, 0.3);
					setSpeed(curSetSpeed);
				}
			}else if(((tempDistance>0.2 && tempDistance<3) && !intake.isOpen())) setSpeed(0);
			else setSpeed(curSetSpeed);
			
			
		}else if(curSetSpeed<0){                //if we are going downwards
			if(!intake.isOpen() && tempDistance<3 && tempDistance>0.2){
				curSetSpeed =0;
				setSpeed(curSetSpeed, false);
			}
			else if(tempDistance<0.5){
				curSetSpeed = Util.constrain(curSetSpeed, -0.3, 0);
				setSpeed(curSetSpeed);
			}
			else setSpeed(curSetSpeed);
		}else setSpeed(0);
		
		if((tempDistance<3 && tempDistance>0.2) && !intakeOverride) intakeOpen = true;
		
		intake.setOpen(intakeOpen);
		if(tempBottomSwitch) encoder.reset();
		goalPos = encoder.getDistance();
		
	}
	
	public void toggleIntake(){
		intake.toggle();
	}
	
	public double getPosition(){
		return encoder.getDistance();
	}
	
	public void setIntakeOverride(boolean _override){
		intakeOverride = _override;
	}
	
	private void setSpeed(double _setspeed){
		if(Math.abs(_setspeed)<0.1){
			
			brake.set(true);
			elevMotor1.set(0);
			elevMotor2.set(0);
		}else{
			brake.set(false);
			elevMotor1.set(-_setspeed);
			elevMotor2.set(_setspeed);
		}	
	}
	
	private void setSpeed(double _setspeed, boolean _brake){
		brake.set(_brake);
		elevMotor1.set(-_setspeed);
		elevMotor2.set(_setspeed);
	}
	
	@Override
	public void stop(){
		super.stop();
		intake.stop();
	}
	
}
