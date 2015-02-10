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


public class SteeringEncoder extends Runnable {
	Logger centerLogger;
	public double offset;
	int wheel;
	String str;
	
	AnalogInput encoder;
	AnalogTriggerOutput upTick = new AnalogTriggerOutput(new AnalogTrigger(encoder), AnalogTriggerType.kRisingPulse);
	AnalogTriggerOutput downTick = new AnalogTriggerOutput(new AnalogTrigger(encoder), AnalogTriggerType.kFallingPulse);
	
	double minVoltage = 0;
	double maxVoltage = 5;
	double oldVoltage;
	public double voltage;
	double angle;
	double lastStopWatch = 0;
	public int count;
	double degreesPerRotation = 102.85714285714285714285714285714;
	edu.wpi.first.wpilibj.Timer stopwatch = new edu.wpi.first.wpilibj.Timer();
	
	public SteeringEncoder(int channel, int _wheel, double center) throws FileNotFoundException, UnsupportedEncodingException,IOException{
		encoder = new AnalogInput(channel);
		
		
		wheel = _wheel;
		while(center>degreesPerRotation) center-=degreesPerRotation;
		while(center<0) center+=degreesPerRotation;
		offset = center;
		offset = 0;
		centerLogger = new Logger(new String[] {""},"/usr/local/frc/logs/zcenter"+ wheel +".txt");
		stopwatch.reset();
		stopwatch.start();
		try {
			String str = centerLogger.read(1)[0];
			if (str == null){
				str = "0.0";
			}
			System.out.println(wheel + "  " + str);
		}
		catch(IOException e){
			System.out.print("reading error   ");
			e.printStackTrace();
		}
		
//		try {
//			str = centerLogger.read(1)[0];
//			str = str.split(" ")[1];
//			offset = Double.parseDouble(str);
//		}
//		catch(NullPointerException e){
//			offset = 0;
//		}
		count = 0;
	}
	
	@Override
	public void update(){
		super.update();
		voltage = encoder.getVoltage();
		//boolean testClockWise = voltage-oldVoltage<-3;
		//boolean testCounterClockWise = voltage-oldVoltage>3;
		boolean testClockWise = upTick.get();
		boolean testCounterClockWise = downTick.get();
				
		
		if(testClockWise) count++;
		if(testCounterClockWise) count--;
		//if(wheel == 2)System.out.println(stopwatch.get()-lastStopWatch + "    " + voltage + "    " + oldVoltage);
		//if(Math.abs((voltage-oldVoltage))<3 && Math.abs((voltage-oldVoltage))>0.5) 
		if(stopwatch.get()-lastStopWatch>0.05)	
			System.out.println(wheel+ ":   "+(stopwatch.get()-lastStopWatch) + "   " +voltage + "    " + oldVoltage);
		oldVoltage = voltage;
		lastStopWatch = stopwatch.get();
	}
	
	public void trimCenter(double trim){
		offset+=trim;
	}
	
	public void writeOffset(){
		double temp=offset%degreesPerRotation;
		if (temp < 0)temp+=degreesPerRotation;
		centerLogger.write(temp+"");
	}
	
	@Override
	public void start(int periodMS){
		count = 0;
		offset = 0;
		offset = getAngleDegrees();
		super.start(periodMS);
		voltage = encoder.getVoltage();
		oldVoltage = voltage;
//		try {
//			str = centerLogger.read(1)[0];
//			if (str.charAt(0)==':'){
//				str = str.split(" ")[1];
//				offset = Double.parseDouble(str);
//			}
//		}
//		catch(NullPointerException e){
//			System.out.println(e);
//			centerLogger.write(0+"");
//			offset = 0;
//		}
//		catch (IOException e){
//			System.out.println(e);
//			centerLogger.write(0+"");
//			offset = 0;
//		}
	}
	
	public double getAngleDegrees(){
		angle = ((count*degreesPerRotation + Util.map( encoder.getVoltage(), minVoltage, maxVoltage, 0, degreesPerRotation))-offset)%360;
		if(angle<0) angle+=360;
		//System.out.println(wheel + "   " + offset + "   " + count);
		return angle;
	}
	public double getRawVoltage(){
		return voltage;
	}
}
