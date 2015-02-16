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
	double clicksPerTote = 0.017578125;
	double goalPos;	
	double distPerTote = 1.0;
	double error;
	public boolean firstTime = true;

	/*
	 * @param config - encoderSlotA, encoderSlotB, limitSwitchBot, limitSwitchTop, BreakerChannel
	 */
	public Elevator(int[] config) {
		encoder = new Encoder(config[1], config[0]);
		encoder.setSamplesToAverage(10);
//		limitSwitchBot = new DigitalInput(config[2]);
//		limitSwitchTop = new DigitalInput(config[3]);
		
		brake = new Solenoid(config[4]);
		intake = new Intake(config[5],config[6], config[7], config[8]);
		elevMotor1 = new VictorSP(config[9]);
		elevMotor2 = new VictorSP(config[10]);
		encoder.setDistancePerPulse(0.017578125);
	}
	
	public void overrideMotion(){
		override = true;
	}
	
	public void regularMotion(){
		override = false;
	}
	
	public void firstTime(){
		firstTime = true;
	}
	
	
	
	@Override
	public void start(int periodMS){
		intake.start(20);
		super.start(periodMS);
	}
	
	public void setIntakeOpen(boolean _open){
		System.out.println("setintakeOpen _open:   " + _open+ "   " +encoder.getDistance()+ encoder.getRate());
		if(encoder.getDistance() < 2.25){
			System.out.print("intakeSetTrue");
			intake.setOpen(true);
		} else {
			intake.setOpen(_open);
		}
	}
	
	public void setIntakeMotors(double _speed){
		intake.setMotors(_speed);
	}
	public void setEjector(){
		
	}
	
	public void setGoalPos(double _totesPos){	
		goalPos = _totesPos;
	}
	
	public void increment(boolean up){
		if(up) goalPos = (int)goalPos+1;
		else goalPos = (int) goalPos;
	}
	
	public void setMotion(boolean _moveUp,boolean _moveDown){
		moveUp = _moveUp;
		moveDown = _moveDown;
		override();
	}
	
	public void reset(){
		encoder.reset();
	}
	
	public void move(){
		System.out.println("In Move");
		goalPos = Math.round(goalPos);
		brake.set(startBraking);
		double error =Util.deadZone(goalPos-encoder.getDistance(), 0, 0.1, 0);
		if (error>0/* && !limitSwitchTop.get()*/){
			if(firstTime){
				startBraking = false;
				brake.set(startBraking);
				try{
					Thread.sleep(100);
				}catch(InterruptedException e){e.printStackTrace();}
				elevMotor1.set(-1);
				elevMotor2.set(1);
				firstTime = false;
			} else {
				elevMotor1.set(-1);
				elevMotor2.set(1);
				startBraking = false;
			}
		} else if (error<0 /*&& !limitSwitchBot.get()*/){
			if(firstTime){
				startBraking = false;
				brake.set(startBraking);
				try{
					Thread.sleep(100);
				}catch(InterruptedException e){e.printStackTrace();}
				elevMotor1.set(0.5);
				elevMotor2.set(-0.5);
				firstTime = false;
			} else {
				elevMotor1.set(0.5);
				elevMotor2.set(-0.5);
				startBraking = false;
			}
		} else if (Util.deadZone(error, 0, 0.1, 0) == 0){
			elevMotor1.set(0);
			elevMotor2.set(0);
			startBraking = true;
		} else {
			elevMotor1.set(0);
			elevMotor2.set(0);
			startBraking = true;
		}
	}
		
	public boolean atLocation(){
		if(error == 0)return true;
		else return false;
	}
	
	private void override(){
		brake.set(startBraking);
		System.out.println("override encoder rate:   " + encoder.getRate());
		if (moveUp && !moveDown && encoder.getDistance()<5){
			startBraking=false;
			brake.set(startBraking);
			if(firstTime){
				try{
					Thread.sleep(100);
				}	catch(InterruptedException e){}
				firstTime=false;
			}
			elevMotor1.set(-1);// + ((encoder.getRate()-4)/20));
			elevMotor2.set(1);// + ((encoder.getRate()-4)/20));
		}
		else if (moveDown && !moveUp && encoder.getDistance()>0.5){
			startBraking=false;
			brake.set(startBraking);
			if(firstTime){
				try{
					Thread.sleep(100);
				}	catch(InterruptedException e){}
				firstTime=false;
			}
			elevMotor1.set(0.2);//+ ((encoder.getRate()+4)/20));
			elevMotor2.set(-0.2);// - ((encoder.getRate()+4)/20));
		}else if(moveDown && !moveUp && encoder.getDistance()<0.5 && encoder.getDistance()>-0.5){
			elevMotor1.set(0.1);
			elevMotor1.set(-0.1);
			if(encoder.getDistance()<0) reset();
		}
		
		else {
			startBraking=true;
			elevMotor1.set(0);
			elevMotor2.set(0);
		}
		goalPos = encoder.getDistance();
	}
	
	@Override
	public void update() {
		error = goalPos-encoder.getDistance();
		error = Util.deadZone(error, 0, 0.1, 0);
		if(!override)move();
		else override();
	}
	
	@Override
	public void stop(){
		super.stop();
		intake.stop();
	}
	
}
