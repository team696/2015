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
//	DigitalInput limitSwitchBot;
//	DigitalInput limitSwitchTop;
	VictorSP elevMotor1;
	VictorSP elevMotor2;
	Solenoid brake;
	CustomPID PID = new CustomPID(1, 1, 0);
	Intake intake;
	
	boolean moveUp = false;
	boolean moveDown = false;
	boolean override = true;
	boolean startBraking;
	double clicksPerTote = 360;
	double goalPos;	
	double distPerTote = 1.0;
	double error;
	public boolean firstTime = true;

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
	
	public void overrideMotion(){
		override = true;
	}
	
	public void firstTime(){
		firstTime = true;
	}
	
	public void regularMotion(){
		override = false;
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
		goalPos = encoder.get();
		override();
	}
	
	public void reset(){
		encoder.reset();
	}
	
	public void move(){
		double error =goalPos-encoder.get();
		if (error>0/* && !limitSwitchTop.get()*/){
			elevMotor1.set(-1);
			elevMotor2.set(1);
			startBraking = false;
		} else if (error<0 /*&& !limitSwitchBot.get()*/){
			elevMotor1.set(-0.5);
			elevMotor2.set(0.5);
			startBraking =false;
		} else if (Util.deadZone(error, 0, 0.1, 0) == 0){
			elevMotor1.set(0);
			elevMotor2.set(0);
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
		
	public boolean atLocation(){
		if(error == 0)return true;
		else return false;
	}
	
	private void override(){
		brakeSys();
		if (moveUp && !moveDown){
			startBraking=false;
			if(firstTime){
				try{
					Thread.sleep(50);
				}	catch(InterruptedException e){}
				firstTime=false;
			}
			elevMotor1.set(1);
			elevMotor2.set(-1);
		}
		else if (moveDown && !moveUp){
			startBraking=false;
			try{
				Thread.sleep(50);
			}catch(InterruptedException e){}
			elevMotor1.set(-0.7);
			elevMotor2.set(0.7);
		}
		else {
			startBraking=true;
			elevMotor1.set(0);
			elevMotor2.set(0);
		}
	}
	
	public void brakeSys() {
		brake.set(startBraking);
	}
	
	@Override
	public void update() {
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
