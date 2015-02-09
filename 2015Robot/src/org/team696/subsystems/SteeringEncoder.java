package org.team696.subsystems;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.team696.baseClasses.Logger;
import org.team696.baseClasses.Runnable;
import org.team696.baseClasses.Util;

import edu.wpi.first.wpilibj.AnalogInput;


public class SteeringEncoder extends Runnable {
	//Logger centerLogger;
	public double offset;
	int wheel;
	String str;
	
	AnalogInput encoder;
	double minVoltage = 0;
	double maxVoltage = 5;
	double oldVoltage;
	public double voltage;
	double angle;
	
	public int count;
	double degreesPerRotation = 102.85714285714285714285714285714;
	
	public SteeringEncoder(int channel, int _wheel, double center) throws FileNotFoundException, UnsupportedEncodingException,IOException{
		encoder = new AnalogInput(channel);
		wheel = _wheel;
		while(center>degreesPerRotation) center-=degreesPerRotation;
		while(center<0) center+=degreesPerRotation;
		offset = center;
		offset = 0;
//		centerLogger = new Logger(new String[] {""},"/usr/local/frc/logs/zcenter"+ wheel +".txt");
//		
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
    	System.out.println("angle:   " + getAngleDegrees()+ "   voltage:   " + voltage+ "    count:   " + count);
		voltage = encoder.getVoltage();
		boolean testClockWise = voltage<0.5 && oldVoltage>4.5;
		boolean testCounterClockWise = voltage>4.5 && oldVoltage<0.5;
		if(testClockWise) count++;
		if(testCounterClockWise) count--;
		oldVoltage = voltage;
	}
	
	public void trimCenter(double trim){
		offset+=trim;
	}
	
//	public void writeOffset(){
//		double temp=offset%degreesPerRotation;
//		if (temp < 0)temp+=degreesPerRotation;
//		centerLogger.write(temp+"");
//	}
	
	@Override
	public void start(int periodMS){
		count = 0;
		offset = 0;
		offset = getAngleDegrees();
		super.start(periodMS);
		oldVoltage = voltage;
		voltage = encoder.getVoltage();
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
		//System.out.println(wheel + "   " + offset + "   " + count);
		return angle;
	}
	public double getRawVoltage(){
		return voltage;
	}
}
