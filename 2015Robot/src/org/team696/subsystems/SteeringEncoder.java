package org.team696.subsystems;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.team696.baseClasses.Logger;
import org.team696.baseClasses.Runnable;
import org.team696.baseClasses.Util;

import edu.wpi.first.wpilibj.AnalogInput;


public class SteeringEncoder extends Runnable {
	Logger centerLogger;
	Logger counter;
	double center;
	int wheel;
	
	AnalogInput encoder;
	double minVoltage = 0;
	double maxVoltage = 5;
	double oldVoltage;
	double voltage;
	double angle;
	
	int count = 0;
	double degreesPerRotation = 102.85714285714285714285714285714;
	
	public SteeringEncoder(int channel, int _wheel) throws FileNotFoundException, UnsupportedEncodingException,IOException{
		encoder = new AnalogInput(channel);
		wheel = _wheel;
		centerLogger = new Logger(new String[] {"Center1","Center2","Center3","Center4"});
		counter = new Logger(new String[] {"CountPos1","CountPos2","CountPos3","CountPos4"});
		centerLogger.setPath("/usr/local/frc/logs/zcenter"+ wheel +".txt");
		counter.setPath("/usr/local/frc/logs/zcounter"+wheel+".txt");		
		center = Integer.parseInt(centerLogger.read(1)[0].substring(0,1));
		count = Integer.parseInt(counter.read(1)[0].substring(0, 1));
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
		centerLogger.set(center,1);
		counter.set(count,1);
		centerLogger.setString(false);
		counter.setString(false);
	}
	
	@Override
	public void start(int periodMS){
		super.start(periodMS);
		oldVoltage = voltage;
		voltage = encoder.getVoltage();
	}
	
	@Override
	public void stop(){
		centerLogger.setString(true);
		counter.setString(true);
	}
	
	public double getAngleDegrees(){
		angle = (count*degreesPerRotation + Util.map( encoder.getVoltage(), minVoltage, maxVoltage, 0, degreesPerRotation))%360;
		return angle;
	}
}
