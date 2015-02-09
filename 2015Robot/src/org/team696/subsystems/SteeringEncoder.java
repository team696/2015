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
	double offset;
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
		centerLogger = new Logger(new String[] {""},"/usr/local/frc/logs/zcenter"+ wheel +".txt");
		counter = new Logger(new String[] {""},"/usr/local/frc/logs/zcounter"+wheel+".txt");
//		centerLogger.setPath("/usr/local/frc/logs/zcenter"+ wheel +".txt");
//		counter.setPath("/usr/local/frc/logs/zcounter"+wheel+".txt");

			centerLogger.write("0");
			counter.write("0");
			
			offset = 0;
			count = 0;
//		try {
//		offset = Double.parseDouble(centerLogger.read(1)[0]);
//		}
//		catch(FileNotFoundException e) {
//			centerLogger.write("0");
//			offset = 123456789;
//		}
//		try {
//			count = Integer.parseInt(counter.read(1)[0]);
//		}
//		catch(FileNotFoundException e) {
//			counter.write("0");
//			count = 0;
//		}
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
		centerLogger.set(offset,0);
		counter.set(count,0);
		centerLogger.setString(true);
		counter.setString(true);
		
		//System.out.println(wheel + "   " + offset);
		
//		try{
//			System.out.println("centerLogger: "+centerLogger.read(1)[0]+"  |  "+"counter: "+counter.read(1)[0]);
//		}
//		catch (IOException e){};
	}
	
	public void trimCenter(double trim){
		offset+=trim;
//		if(offset>degreesPerRotation){
//			offset-=degreesPerRotation;
//			count--;
//		}else if(offset<0){
//			offset+=degreesPerRotation;
//			count++;
//		}
	}
	
	@Override
	public void start(int periodMS){
		super.start(periodMS);
		oldVoltage = voltage;
		voltage = encoder.getVoltage();
	}
	
	@Override
	public void stop(){
		centerLogger.setString(false);
		counter.setString(false);
		
	}
	
	public double getAngleDegrees(){
		angle = ((count*degreesPerRotation + Util.map( encoder.getVoltage(), minVoltage, maxVoltage, 0, degreesPerRotation))+offset)%360;
		//System.out.println(wheel + "   " + offset + "   " + count);
		return angle;
	}
}
