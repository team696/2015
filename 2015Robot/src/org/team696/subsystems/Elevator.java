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
	boolean decelMoveUp = false;
	
	boolean lastMoveUp = false;
	boolean lastMoveDown = false;
	boolean accelMoveDown = false;
	
	boolean zoneLatch = false;
	double lastDistance;
	
	
	boolean override = true;
	boolean startBraking;
	double clicksPerTote = 0.00146484375;
	double goalPos;
	double error;
	public boolean firstTime = true;

	Timer timer = new Timer();
	
	/*
	 * @param config - encoderSlotA, encoderSlotB, limitSwitchBot, limitSwitchTop, BreakerChannel
	 */
	public Elevator(int[] config) {
		encoder = new Encoder(0, 1);
		encoder.setSamplesToAverage(10);
		
		limitSwitchBot = new DigitalInput(10);
		limitSwitchTop = new DigitalInput(11);
		
		brake = new Solenoid(4);
		intake = new Intake(6,5, 4, 1);
		elevMotor1 = new VictorSP(2);
		elevMotor2 = new VictorSP(3);
		encoder.setDistancePerPulse(clicksPerTote);
		lastDistance = encoder.getDistance();
	}
	
	@Override
	public void start(int periodMS){
		timer.start();
		intake.start(20);
		super.start(periodMS);
	}
	
	public void reset(){
		encoder.reset();
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

	public void setIntakeMotors(double _speed){
		intake.setMotors(_speed);
	}
	
	public void setIntakeOpen(boolean _open){
		intake.setOpen(_open);
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
		//brake.set(startBraking);
		boolean tempBottomSwitch = limitSwitchBot.get();
		boolean tempTopSwitch = limitSwitchTop.get();
		double tempDistance = encoder.getDistance();
		
		if (moveUp && tempDistance<5 && tempTopSwitch){
			startBraking=false;
			brake.set(false);
			if(firstTime){
				try{
					Thread.sleep(100);
				}	catch(InterruptedException e){}
				firstTime=false;
			}
			elevMotor1.set(-1);// + ((encoder.getRate()-4)/20));
			elevMotor2.set(1);// + ((encoder.getRate()-4)/20));
		}
		else if (moveDown && tempDistance>0.5 && tempBottomSwitch){
			if(moveDown && !lastMoveDown)
			startBraking=false;
			brake.set(false);
			if(firstTime){
				try{
					Thread.sleep(100);
				}	catch(InterruptedException e){}
				firstTime=false;
			}
			elevMotor1.set(0.6);//+ ((encoder.getRate()+4)/20));
			elevMotor2.set(-0.6);// - ((encoder.getRate()+4)/20));
		}else if(moveDown && !moveUp && tempDistance<0.5 && tempBottomSwitch){
			startBraking = false;
			brake.set(false);
			elevMotor1.set(0.4);
			elevMotor2.set(-0.4);
		}
		else {
			if(lastMoveUp && !moveUp){
				timer.reset();
				decelMoveUp = true;
			}
			
			if(!lastMoveDown && moveDown){
				timer.reset();
				accelMoveDown = true;
			}
			
			if(timer.get()<0.5 && decelMoveUp){
//				System.out.println("decelMovingUp" + timer.get());
				elevMotor1.set(-1+ timer.get()/0.5);
				elevMotor2.set(1-timer.get()/0.5);
				startBraking = false;
				brake.set(true);
			}else if(timer.get()<0.3 && accelMoveDown){
//				System.out.println("accelMoving down:    " + (-0.4+(timer.get()*0.4/0.3))+ "   "+ (0.4-(timer.get()*0.4/0.3)));
				brake.set(true);
				elevMotor1.set(timer.get()*2);
				elevMotor2.set(-timer.get()*2);
			}else {
				decelMoveUp = false;
				accelMoveDown = false;
				startBraking = true;
				brake.set(true);
//				System.out.println("Stop");
				accelMoveDown = false;
			}
		}
//		System.out.println(moveDown+ "  " + moveUp+ "  "+ tempBottomSwitch+ "   " + tempTopSwitch + "   " + tempDistance + "  " + elevMotor1.get() + "   " + elevMotor2.get());
//		System.out.print(brake.get());
		if(!limitSwitchBot.get()) reset();
		goalPos = encoder.getDistance();
	}
	
	public void toggleIntake(){
		intake.toggle();
	}
	
	@Override
	public void update() {
		double tempDistance = encoder.getDistance();
		error = goalPos-tempDistance;
		error = Util.deadZone(error, 0, 0.1, 0);
		
		if(tempDistance<3 && tempDistance >0.1 && !intake.isOpen()){
			intake.setOpen(true);	
		}
		
		if(!override)move();
		else override();
		lastMoveDown = moveDown;
		lastMoveUp = moveUp;
		
	}
	
	@Override
	public void stop(){
		super.stop();
		intake.stop();
	}
	
}
