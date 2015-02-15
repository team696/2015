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
//	Logger centerLogger;
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
//		counter.write("0");
		
//		centerLogger = new Logger(new String[] {""},"/usr/local/frc/logs/zcenter"+ wheel +".txt");
//		try {
//			str = centerLogger.read(1)[0];
//			if (str == null){
//				str = "0.0";
//			}
//			System.out.println(wheel + "  " + str);
//		}
//		catch(IOException e){
//			System.out.print("reading error   ");
//			e.printStackTrace();
//		}
		
//		try {
////			str = centerLogger.read(1)[0];
//			offset = Double.parseDouble(str);
//			System.out.println("offset: "+offset);
//		}
//		catch(NullPointerException e){
//			offset = 0;
//		}
		count = 0;
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
		//System.out.println(count + "  " + voltage + "   " + angle);
		
		//if(wheel == 2)System.out.println(stopwatch.get()-lastStopWatch + "    " + voltage + "    " + oldVoltage);
		//if(Math.abs((voltage-oldVoltage))<3 && Math.abs((voltage-oldVoltage))>0.5) 
		
	}
	
	public void trimCenter(double trim){
		offset+=trim;
		}
	
	public void writeOffset(){
		double temp=offset%degreesPerRotation;
		if (temp < 0)temp+=degreesPerRotation;
//		centerLogger.write(temp+"");
		//System.out.println(temp+"");
	}
	
	@Override
	public void start(int periodMS){
//		try{
//			count = Integer.parseInt(counter.read(1)[0]);
//		}catch(IOException e){System.out.println("Found IOException on line 118\nin SteeringEncoder.java");count=0;}
		offset = 0;
		count = 0;
//		System.out.print(getAngleDegrees()+ "   ");
		offset = getAngleDegrees();
		voltage = encoder.getVoltage();
		oldVoltage = voltage;
//		System.out.println(offset + "   ");
		super.start(periodMS);
		
//		try {
//			str = centerLogger.read(1)[0];
//			if(str==null)str="0.0";
//			offset = Double.parseDouble(str);
//			
//		}
//		catch(NullPointerException e){
//			System.out.println("error reading " + e);
//			centerLogger.write(0+"");
//			offset = 0;
//			System.out.println(offset);
//		}
//		catch (IOException e){
//			System.out.println(e);
//			centerLogger.write(0+"");
//			offset = 0;
//		}
	}
	
	public double getAngleDegrees(){
		System.out.println("  "+ wheel+ "  " + voltage);
		angle = ((count*degreesPerRotation + Util.map( encoder.getVoltage(), minVoltage, maxVoltage, 0, degreesPerRotation))- offset)%360;
		if(angle<0) angle+=360;
		//System.out.println(wheel + "   " + offset + "   " + count);
		return angle;
	}
	public double getRawVoltage(){
		return voltage;
	}
}
