package org.team696.robot;

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
	CustomPID heightController;
	Solenoid breaker;
	
	boolean startBreaking;
	double cliksPerTote = 360;
	double goalPos;	

	/*
	 * @param config - encoderSlotA, encoderSlotB, limitSwitchBot, limitSwitchTop, Kp, Ki, Kd, BreakerChannel
	 */
	public Elevator(int[] config) {
		encoder = new Encoder(config[0], config[1]);
		
		limitSwitchBot = new DigitalInput(config[2]);
		limitSwitchTop = new DigitalInput(config[3]);
		
		heightController = new CustomPID(config[4], config[5], config[6]);
		
		breaker = new Solenoid(config[7]);
	}

	@Override
	public void start(int frequency) {
		super.start(frequency);
	}
	
	public void setGoalPos(double _goalPos) {
		goalPos = _goalPos;
	}
	
	public void setBreaking(boolean _startBreaking) {
		startBreaking = _startBreaking;
	}
	
	public void setHeight() {
		breakSys();
		if (limitSwitchTop.get() && goalPos>cliksPerTote*6.5) elevMotor.set(0);
		else if (limitSwitchBot.get()) encoder.reset();
		else if (limitSwitchBot.get() && goalPos<0) elevMotor.set(0);
		else elevMotor.set(goalPos - encoder.get());
	}
	
	public void breakSys() {
		if (startBreaking) breaker.set(true);
		
		breaker.set(false);
	}
	
	@Override
	public void update() {
		setHeight();
		heightController.update(goalPos - encoder.get());
	}
	
}
