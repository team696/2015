package org.team696.robot;

import org.team696.baseClasses.Runnable;
import org.team696.baseClasses.Util;

import edu.wpi.first.wpilibj.AnalogInput;


public class SteeringEncoder extends Runnable{

	AnalogInput encoder;
	double minVoltage = 0.1867675632238388;
	double maxVoltage = 1.6003416776657104;
	double oldVoltage;
	double voltage;
	double angle;
	
	int count = 0;
	double degreesPerRotation = 102.85714285714285714285714285714;
	public SteeringEncoder(int channel){
		encoder = new AnalogInput(1);
	}
	
	@Override
	public void update(){
		super.update();
		voltage = encoder.getVoltage();
		boolean testClockWise = voltage<0.4 && oldVoltage>1.3;
		boolean testCounterClockWise = voltage>1.3 && oldVoltage<0.4;
		if(testClockWise) count++;
		if(testCounterClockWise) count--;
		oldVoltage = voltage;
		
	}
	
	@Override
	public void start(int periodMS){
		super.start(periodMS);
		voltage = encoder.getVoltage();
		oldVoltage = voltage;
	}
	public double getAngleDegrees(){
		angle = (count*degreesPerRotation + Util.map( encoder.getVoltage(), minVoltage, maxVoltage, 0, degreesPerRotation))%360;
		return angle;
	}
}
