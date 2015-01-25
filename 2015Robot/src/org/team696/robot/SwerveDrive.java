package org.team696.robot;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import org.team696.robot.SwerveModule;
import org.team696.baseClasses.Runnable;

public class SwerveDrive extends Runnable{

	private boolean fieldCentric = false;
	
	private double L2WRatio = 1; //length to width ratio
	//first dimension refers to each module from front left to back right
	//in a clockwise order
	//second dimension is each 
	private int[][] moduleChannels = new int[4][5];
	
	private double[] setRobotVector = new double[3];       //x, y, and rotation Speed (robotCentric)
	private double[] setFieldCentricVector = new double[3]; ///x, y, and rotation Speed (fieldCentric)
	
	private double[] motionVector = {0.0,0.0,0.0};   //x, y, and rotation speed
	private double[] fieldCentricMotionVector = {0.0,0.0,0.0}; //x, y, and rotation speed.
	
	private double[][] wheelVectors = new double[4][2];      // wheel vectors
	private double[][] setWheelVectors = new double[4][2];
	
	private double[][] wheelValues = new double[4][2];      // wheel values radians
	private double[][] setWheelValues = new double[4][2];
	
	private SwerveModule frontLeft;
	private SwerveModule frontRight;
	private SwerveModule backRight;
	private SwerveModule backLeft;
	
	public SwerveDrive(int[][] _moduleChannels){
		moduleChannels = _moduleChannels;
	    frontLeft = new SwerveModule(moduleChannels[0]);
		frontRight = new SwerveModule(moduleChannels[1]);
		backRight = new SwerveModule(moduleChannels[2]);
		backLeft = new SwerveModule(moduleChannels[3]);
		
	}
	@Override
	public void start(int periodMS){
		super.start(periodMS);
		
		
	}
	@Override
	public void update(){
		super.update();
		setWheelVectors = calculateVectors(setRobotVector[0], setRobotVector[1], setRobotVector[2]);
		setWheelValues = calculateWheelValues(setWheelVectors);
		frontLeft.setValues(wheelValues[0][0], wheelValues[0][1]);
		frontRight.setValues(wheelValues[1][0], wheelValues[1][1]);
		backRight.setValues(wheelValues[2][0], wheelValues[2][1]);
		frontLeft.setValues(wheelValues[3][0], wheelValues[3][1]);
				
	}
	public void setSteerPID(double P, double I, double D){
		frontLeft.setSteerPID(P, I, D);
		frontRight.setSteerPID(P, I, D);
		backRight.setSteerPID(P, I, D);
		backLeft.setSteerPID(P, I, D);
	}
//	public void setDrivePID(double P, double I, double D, double F){
//		frontLeft.setDrivePID(P, I, D, F);
//		frontRight.setDrivePID(P, I, D, F);
//		backRight.setDrivePID(P, I, D, F);
//		backLeft.setDrivePID(P, I, D, F);
//	}
	
	void setWheels(){
		frontLeft.setValues(setWheelValues[0][0], setWheelValues[0][1]);
		frontRight.setValues(setWheelValues[1][0], setWheelValues[1][1]);
		backLeft.setValues(setWheelValues[2][0], setWheelValues[2][1]);
		backRight.setValues(setWheelValues[3][0], setWheelValues[3][1]);
	}
	
	double[][] calculateVectors(double speed, double headingDegrees, double rotationDegrees){
		double[][] vectors = new double[4][2];
		double headingRadians = Math.toRadians(headingDegrees);
		double rotationRadians = Math.toRadians(rotationDegrees);
		for(int fish = 0; fish<4; fish++){
			vectors[fish][0] = speed*Math.cos(headingRadians);
			vectors[fish][1] = speed*Math.sin(headingRadians);
			//vectors[fish][0] += rotationRadians*Math.cos((Math.PI/4) + (fish*Math.PI/2));
			//vectors[fish][1] += rotationRadians*Math.sin((Math.PI/4) + (fish*Math.PI/2));
		}
		vectors[0][0] += rotationRadians*L2WRatio;
		vectors[0][1] += rotationRadians;
		vectors[1][0] += rotationRadians*L2WRatio;
		vectors[1][1] -= rotationRadians;
		vectors[2][0] -= rotationRadians*L2WRatio;
		vectors[2][1] -= rotationRadians;
		vectors[3][0] -= rotationRadians*L2WRatio;
		vectors[3][1] += rotationRadians;
		
		return vectors;
	}
	
	double[][] calculateWheelValues(double[][] vectors){
		double values[][] = new double[4][2];
		
		for(int fish = 0; fish<4; fish++){
			values[fish][0] = Math.sqrt(vectors[fish][0] *vectors[fish][0]) +((vectors[fish][1] *vectors[fish][1])); //pythagorean thrm for speed
			values[fish][1] = Math.atan2(vectors[fish][1],vectors[fish][0]);         
			//atan2 to find angle between -pi and pi
			
			}
		double maxValue=0;
		
		for (int fish = 0; fish < 4; fish++) {
			if(values[fish][0]>maxValue) maxValue = values[fish][0];
		}
		if(maxValue>1){
			for (int fish = 0; fish < 4; fish++) {
				values[fish][0] /= maxValue;
			}
		}
		return values;
	}
	
	boolean setDriveValues(double speed, double headingDegrees, double rotation){
		setRobotVector[0] = speed;
		setRobotVector[1] = headingDegrees;
		setRobotVector[2] = rotation;
		
		//setWheelVectors = calculateVectors(setSpeed, setRobotHeading, setRotation);
		//setWheelValues = calculateWheelValues(setWheelVectors);
		
		//frontLeft.setValues(wheelValues[0][0], wheelValues[0][1]);
		//frontRight.setValues(wheelValues[1][0], wheelValues[1][1]);
		//backRight.setValues(wheelValues[2][0], wheelValues[2][1]);
		//frontLeft.setValues(wheelValues[3][0], wheelValues[3][1]);
		
		return true;
	}
	
	
	boolean setWheelAngles(double[] angles){
		if(angles.length == 4){
			setWheelValues[0] = angles;
		}else return false;
		return true;
	}
	boolean setWheelSpeeds(double[] speeds){
		if(speeds.length == 4){
			setWheelValues[1] = speeds;
		}else return false;
		return true;
	}
	boolean setFieldCentric(boolean _fieldCentric){
		fieldCentric = _fieldCentric;
		return true;
	}
}
