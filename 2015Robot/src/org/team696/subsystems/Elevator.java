package org.team696.subsystems;

import org.team696.baseClasses.CustomPID;
import org.team696.baseClasses.Runnable;
import org.team696.baseClasses.Util;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class Elevator extends Runnable {
	Encoder encoder;
	DigitalInput limitSwitchBot;
	DigitalInput limitSwitchTop;
	VictorSP elevMotor1;
	VictorSP elevMotor2;
	Solenoid brake;
	CustomPID PID = new CustomPID(1, 1, 0);
	Intake intake;
	
	boolean moveUp = false;
	boolean moveDown = false;
	boolean override = false;
	boolean startBraking;
	double clicksPerTote = 360;
	double goalPos;	
	double distPerTote = 1.0;
	double error;

	/*
	 * @param config - encoderSlotA, encoderSlotB, limitSwitchBot, limitSwitchTop, BreakerChannel
	 */
	public Elevator(int[] config) {
		encoder = new Encoder(config[0], config[1]);
		
//		limitSwitchBot = new DigitalInput(config[2]);
//		limitSwitchTop = new DigitalInput(config[3]);
		
		brake = new Solenoid(config[4]);
		
		intake = new Intake(config[5],config[6], config[7], config[8]);
		
		elevMotor1 = new VictorSP(config[9]);
		elevMotor2 = new VictorSP(config[10]);
		encoder.setDistancePerPulse((1/256)*distPerTote);
	}
	
	@Override
	public void start(int periodMS){
		intake.start(20);
		super.start(periodMS);
	}
	
	public void setIntake(boolean eject,boolean open, boolean _intake){
		if(encoder.get() < 2.25){
			intake.set(eject,false,_intake);
		} else {
			intake.set(eject,open, _intake);
		}
	}
	
	public void setGoalPos(double _totesPos){	
		goalPos = _totesPos;
	}
	
	public void setMotion(boolean _moveUp,boolean _moveDown){
		moveUp = _moveUp;
		moveDown = _moveDown;
	}
	
	private void elevPID(){
		 PID.update(error);
	}
	
	public void move(){
		if (PID.getOutput()>0 && !limitSwitchTop.get()){
			elevMotor1.set(PID.getOutput());
			startBraking = false;
		} else if (PID.getOutput()<0 && !limitSwitchBot.get()){
			elevMotor1.set(PID.getOutput());
			startBraking =false;
		} else if (Util.deadZone(PID.getOutput(), 0, 0.1, 0) == 0){
			elevMotor1.set(0);
			startBraking = true;
		} else {
			elevMotor1.set(0);
			startBraking = true;
		}
	}
	
	public void setHeight(){
		brakeSys();	
		move();
	}
	
//	public void setHeight() {
//		brakeSys();
//		if(!override){
//			if (limitSwitchTop.get() && goalPos>clicksPerTote*6.5) elevMotor.set(0);
//			else if (limitSwitchBot.get()) encoder.reset();
//			else if (limitSwitchBot.get() && goalPos<0) elevMotor.set(0);
//			if(goalPos/clicksPerTote>encoder.get()/clicksPerTote)goalTotesHigh++;
//			if(goalPos/clicksPerTote<encoder.get()/clicksPerTote)goalTotesHigh--;
//			movePerTote();
//		} else {
//			if (limitSwitchTop.get() && goalPos>clicksPerTote*6.5) elevMotor.set(0);
//			else if (limitSwitchBot.get()) encoder.reset();
//			else if (limitSwitchBot.get() && goalPos<0) elevMotor.set(0);
//			else elevMotor.set(goalPos - encoder.get());
//		}
//	}
	
	public boolean atLocation(){
		if(error == 0)return true;
		else return false;
	}
	
	private void override(){
		if (moveUp && !moveDown)elevMotor1.set(0.75);
		else if (moveDown && !moveUp)elevMotor1.set(-0.75);
		else elevMotor1.set(0);
	}
	
	public void override(boolean _override){
		override = _override;
	}
	
	public void brakeSys() {
		if (startBraking) brake.set(true);
		
		brake.set(false);
	}
	
	@Override
	public void update() {
		elevPID();
		error = goalPos-encoder.get();
		error = Util.deadZone(error, 0, 0.1, 0);
		if (!override)setHeight();
		else override();
	}
	
	@Override
	public void stop(){
		super.stop();
		intake.stop();
	}
	
}
