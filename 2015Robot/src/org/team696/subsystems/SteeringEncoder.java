package org.team696.subsystems;

import org.team696.baseClasses.Runnable;
import org.team696.baseClasses.Util;

import edu.wpi.first.wpilibj.AnalogInput;


public class SteeringEncoder extends Runnable{

	AnalogInput encoder;
	double minVoltage = 0;
	double maxVoltage = 5;
	double oldVoltage;
	double voltage;
	double angle;
	
	int count = 0;
	double degreesPerRotation = 102.85714285714285714285714285714;
	public SteeringEncoder(int channel){
		encoder = new AnalogInput(channel);
	}
	
	@Override
	public void update(){
		super.update();
		voltage = encoder.getVoltage();
		boolean testClockWise = voltage<0.5 && oldVoltage>4.5;
		boolean testCounterClockWise = voltage>4.5 && oldVoltage<0.5;
		if(testClockWise) count++;
		if(testCounterClockWise) count--;
		oldVoltage = voltage;
	}
	
	@Override
	public void start(int periodMS){
		super.start(periodMS);
		oldVoltage = voltage;
		voltage = encoder.getVoltage();
	}
	public double getAngleDegrees(){
		angle = (count*degreesPerRotation + Util.map( encoder.getVoltage(), minVoltage, maxVoltage, 0, degreesPerRotation))%360;
		return angle;
	}
}
