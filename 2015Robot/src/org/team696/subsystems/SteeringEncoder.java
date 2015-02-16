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
	public double offset;
	int wheel;
	
	public Counter steerCounter;
	public AnalogInput encoder;
	
	public AnalogTrigger turnTrigger;
	
	double minVoltage = 0;
	double maxVoltage = 5;
	double oldVoltage;
	public double voltage;
	double angle;
	double lastStopWatch = 0;
	public int countOffset;
	
	
	double degreesPerRotation = 102.85714285714285714285714285714;
	//edu.wpi.first.wpilibj.Timer stopwatch = new edu.wpi.first.wpilibj.Timer();
	
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
		
		
		boolean lastLine = false;
		int x = 0;
		String str = "0";
		str = centerLogger.read(1)[0];
		offset = Double.parseDouble(str);
		steerCounter.reset();
		centerLogger.makeReader();
		System.out.println("constructor  "+ steerCounter.get());
		
		String s = "0";
		counter.makeReader();
		try{
			 s = counter.read(1)[0];
		}catch(IOException e){e.printStackTrace();}
		if (s == null)countOffset = 0;
		else countOffset = Integer.parseInt(s);
		System.out.println("Start" + countOffset);
		
		steerCounter.reset();
	}
	
	@Override
	public void start(int periodMS){
//		String str = "0";
//		counter.makeReader();
//		try{
//			 str = counter.read(1)[0];
//		}catch(IOException e){e.printStackTrace();}
//		if (str == null)countOffset = 0;
//		else countOffset = Integer.parseInt(str);
//		System.out.println("Start" + countOffset);
//		
//		steerCounter.reset();
		
		voltage = encoder.getVoltage();
		oldVoltage = voltage;
		super.start(periodMS);
	}
	
	@Override
	public void update(){
		super.update();
		counter.makeWriter();

		counter.write(countOffset+steerCounter.get()+"");
//		countOffset = steerCounter.get();
		counter.makeReader();
		centerLogger.makeReader();
//		try{
//			if(wheel==3) System.out.println(countOffset+ "  " + counter.read(1)[0]+ "   " + offset + "  " + centerLogger.read(1)[0]);
//		}catch(IOException e){e.printStackTrace();}
		
		
		voltage = encoder.getVoltage();
	}
	
	public void trimCenter(double trim){
		offset+=trim;
		}
	
	public void writeOffset(){
		System.out.print(wheel + "  writing");
		centerLogger.makeWriter();
		counter.makeWriter();
		offset=offset%degreesPerRotation;
		if (offset < 0)offset+=degreesPerRotation;
		centerLogger.write(offset+"");
		System.out.println(offset);
		countOffset=0;
		counter.write(countOffset+"");
//		if(wheel==3)System.out.print("writeOffset  "+countOffset);
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
