package org.team696.subsystems;

import org.team696.baseClasses.CustomPID;
import org.team696.baseClasses.Runnable;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.VictorSP;

public class Elevator extends Runnable {
	Encoder encoder;
	DigitalInput limitSwitchBot;
	DigitalInput limitSwitchTop;
	VictorSP elevMotor;
	Solenoid brake;
	
	boolean[] move = new boolean[2];
	boolean override = false;
	boolean startBraking;
	double clicksPerTote = 360;
	double goalPos;	
	int loc = 1;
	int goalTotesHigh = loc;

	/*
	 * @param config - encoderSlotA, encoderSlotB, limitSwitchBot, limitSwitchTop, Kp, Ki, Kd, BreakerChannel
	 */
	public Elevator(int[] config) {
		encoder = new Encoder(config[0], config[1]);
		
		limitSwitchBot = new DigitalInput(config[2]);
		limitSwitchTop = new DigitalInput(config[3]);
		
		brake = new Solenoid(config[4]);
	}
	
	public void setGoalPos(double _goalPos) {
		goalPos = _goalPos;
	}
	
	public void setBreaking(boolean _startBreaking) {
		startBraking = _startBreaking;
	}
	
	public void movePerTote(){
		if (goalTotesHigh>loc)elevMotor.set(1);
		if (goalTotesHigh<loc)elevMotor.set(-1);
		if (goalTotesHigh==loc)elevMotor.set(0);
	}
	
	public void setHeight() {
		brakeSys();
		if(!override){
			if (limitSwitchTop.get() && goalPos>clicksPerTote*6.5) elevMotor.set(0);
			else if (limitSwitchBot.get()) encoder.reset();
			else if (limitSwitchBot.get() && goalPos<0) elevMotor.set(0);
			if(goalPos/clicksPerTote>encoder.get()/clicksPerTote)goalTotesHigh++;
			if(goalPos/clicksPerTote<encoder.get()/clicksPerTote)goalTotesHigh--;
			movePerTote();
		} else {
			if (limitSwitchTop.get() && goalPos>clicksPerTote*6.5) elevMotor.set(0);
			else if (limitSwitchBot.get()) encoder.reset();
			else if (limitSwitchBot.get() && goalPos<0) elevMotor.set(0);
			else elevMotor.set(goalPos - encoder.get());
		}
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
		setHeight();
	}
	
}
