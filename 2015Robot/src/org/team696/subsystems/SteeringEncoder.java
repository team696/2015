package org.team696.subsystems;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.team696.baseClasses.Logger;
import org.team696.baseClasses.Runnable;
import org.team696.baseClasses.Util;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogTrigger;
import edu.wpi.first.wpilibj.AnalogTriggerOutput;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.AnalogTriggerOutput.AnalogTriggerType;
import edu.wpi.first.wpilibj.Encoder;


public class SteeringEncoder extends Runnable {
	Logger centerLogger;
	Logger counter;
	public Counter steerCounter;
	public AnalogInput encoder;
	public AnalogTrigger turnTrigger;
	public double offset;
	private double voltage;
	public int countOffset;
	double minVoltage = 0;
	double maxVoltage = 5;
	double oldVoltage;
	double angle;
	double lastStopWatch = 0;
	double degreesPerRotation = 102.85714285714285714285714285714;
	int wheel;
	
	public SteeringEncoder(int channel, int _wheel) throws FileNotFoundException, UnsupportedEncodingException,IOException{
		encoder = new AnalogInput(channel);
		
		turnTrigger = new AnalogTrigger(encoder);
		turnTrigger.setLimitsVoltage(1.0, 4.0);
		turnTrigger.setFiltered(true);
		
		steerCounter = new Counter();
		steerCounter.setUpDownCounterMode();
		steerCounter.setUpSource(turnTrigger, AnalogTriggerType.kRisingPulse);
		steerCounter.setDownSource(turnTrigger, AnalogTriggerType.kFallingPulse);
		
		wheel = _wheel;
		
		offset = 0;
		
		counter = new Logger(new String[] {""},"/usr/local/frc/logs/zcounter"+ wheel +".txt");
		centerLogger = new Logger(new String[] {""},"/usr/local/frc/logs/zcenter"+ wheel +".txt");
		
		String str = "0";
		str = centerLogger.read(1)[0];
		centerLogger.closeReader();
		offset = Double.parseDouble(str);
		steerCounter.reset();
		
		String s = "0";
		counter.makeReader();
		try{
			 s = counter.read(1)[0];
			 counter.closeReader();
		}catch(IOException e){e.printStackTrace();}
		if (s == null)countOffset = 0;
		else countOffset = Integer.parseInt(s);
		steerCounter.reset();
	}
	
	@Override
	public void start(int periodMS){
		voltage = encoder.getVoltage();
		oldVoltage = voltage;
		super.start(periodMS);
	}
	
	@Override
	public void update(){
		super.update();
		
		counter.makeWriter();
		int temp = countOffset+steerCounter.get();
		counter.write(temp);
		counter.closeWriter();
		
		voltage = encoder.getVoltage();
	}
	
	@Override
	public void stop(){
		super.stop();
	}
	
	public void trimCenter(double trim){
		offset+=trim;
	}
	

	public void writeOffset(){
		System.out.print(wheel + "  writing");
		centerLogger.makeWriter();
		//offset=offset%degreesPerRotation; //THIS IS OUR OFFFSET WHICH WE ARE CHANGING
		offset = Util.map( encoder.getVoltage(), minVoltage, maxVoltage, 0, degreesPerRotation);
		
		if (offset < 0)offset+=degreesPerRotation;
		centerLogger.write(offset);
		centerLogger.closeWriter();
		System.out.println(offset);
		countOffset=0;
		counter.makeWriter();
		counter.write(countOffset);
		counter.closeWriter();
		steerCounter.reset();
	}
	
	public double getAngleDegrees(){
		angle = (((countOffset+ steerCounter.get())*degreesPerRotation + Util.map( encoder.getVoltage(), minVoltage, maxVoltage, 0, degreesPerRotation))- offset)%360;
		if(angle<0) angle+=360;
		return angle;
	}
	public double getRawVoltage(){
		return voltage;
	}
}
