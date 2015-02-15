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
//	Logger counter;
	public double offset;
	int wheel;
	String str;
	
	public Counter steerCounter;
	public AnalogInput encoder;
	
	public AnalogTrigger turnTrigger;
	
	double minVoltage = 0;
	double maxVoltage = 5;
	double oldVoltage;
	public double voltage;
	double angle;
	double lastStopWatch = 0;
	public int count;
	
	
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
		
//		counter = new Logger(new String[] {""},"/usr/local/frc/logs/zcounter"+ wheel +".txt");
		
		centerLogger = new Logger(new String[] {""},"/usr/local/frc/logs/zcenter"+ wheel +".txt");
		
		String[] strAry = centerLogger.read(50);
		boolean lastLine = false;
		int x = 0;
		while (!lastLine){
			if(strAry[x].equals(null)){
				lastLine = true;
				if(x==0)str = "0";
				else str = strAry[x-1];
			}
			x++;
		}
		offset = Integer.parseInt(str);
		count = 0;
		makeReader();
	}
	
	public void makeWriter(){
		centerLogger.makeWriter();
	}
	
	public void makeReader(){
		centerLogger.makeReader();
	}
	
	@Override
	public void update(){
		super.update();
//		counter.writerRefresh();
//		counter.write(count+"");
		voltage = encoder.getVoltage();
		//boolean testClockWise = voltage-oldVoltage<-3;
		//boolean testCounterClockWise = voltage-oldVoltage>3;
		//boolean testClockWise = upTick.get();
		//boolean testCounterClockWise = downTick.get();
		
		//if(testClockWise) count++;
		//if(testCounterClockWise) count--;
		count = steerCounter.get();
		
		//if(wheel == 2)System.out.println(stopwatch.get()-lastStopWatch + "    " + voltage + "    " + oldVoltage);
		//if(Math.abs((voltage-oldVoltage))<3 && Math.abs((voltage-oldVoltage))>0.5) 
		
	}
	
	public void trimCenter(double trim){
		offset+=trim;
		}
	
	public void writeOffset(){
		makeWriter();
		double temp=offset%degreesPerRotation;
		if (temp < 0)temp+=degreesPerRotation;
		centerLogger.write(temp+"");
	}
	
	@Override
	public void start(int periodMS){
		count = 0;
//		offset = getAngleDegrees();
		voltage = encoder.getVoltage();
		oldVoltage = voltage;
		super.start(periodMS);
	}
	
	public double getAngleDegrees(){
		angle = ((count*degreesPerRotation + Util.map( encoder.getVoltage(), minVoltage, maxVoltage, 0, degreesPerRotation))- offset)%360;
		if(angle<0) angle+=360;
		//System.out.println(wheel + "   " + offset + "   " + count);
		return angle;
	}
	public double getRawVoltage(){
		return voltage;
	}
}
