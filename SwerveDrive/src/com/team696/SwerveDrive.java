package com.team696;

import com.team696.SwerveModule;

public class SwerveDrive {

	boolean fieldCentric = false;
	
	
	private double setRotation;
	private double setRotationSpeed;
	private double setRobotHeading;
	private double setSpeed;
	
	private double[] robotMotionVector = {0.0,0.0,0.0};   //x, y, and rotation speed
	
	private double[][] wheelVectors = new double[4][2];      // wheel vectors
	private double[][] setWheelVectors = new double[4][2];
	
	private double[][] wheelValues = new double[4][2];      // wheel values radians
	private double[][] setWheelValues = new double[4][2];
	
	private SwerveModule frontLeft;
	private SwerveModule frontRight;
	private SwerveModule backRight;
	private SwerveModule backLeft;
	
	public SwerveDrive(int frontLeftWheel, int frontLeftSteer, int frontRightWheel, int frontRightSteer,
			int backRightWheel, int backRightSteer, int backLeftWheel, int backLeftSteer){
		
	    frontLeft = new SwerveModule(frontLeftSteer, frontLeftWheel, 0, 0, 0);
		frontRight = new SwerveModule(frontRightSteer, frontRightWheel, 0, 0, 0);
		backRight = new SwerveModule(backRightSteer,backRightWheel, 0, 0, 0);
		backLeft = new SwerveModule(backLeftSteer, backLeftWheel, 0, 0, 0);
		
	}
	
	public void setSteerPID(int P, int I, int D){
		frontLeft.setSteerPID(P, I, D);
		frontRight.setSteerPID(P, I, D);
		backRight.setSteerPID(P, I, D);
		backLeft.setSteerPID(P, I, D);
	}
	public void setDrivePID(int P, int I, int D){
		frontLeft.setDrivePID(P, I, D);
		frontRight.setDrivePID(P, I, D);
		backRight.setDrivePID(P, I, D);
		backLeft.setDrivePID(P, I, D);
	}
	
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
			vectors[fish][0] += rotationRadians*Math.cos((Math.PI/4) + (fish*Math.PI/2));
			vectors[fish][1] += rotationRadians*Math.sin((Math.PI/4) + (fish*Math.PI/2));
		}
		
		return vectors;
	}
	
	double[][] calculateWheelValues(double[][] vectors){
		double values[][] = new double[4][2];
		
			for(int fish = 0; fish<4; fish++){
				values[fish][0] = Math.sqrt(vectors[fish][0] *vectors[fish][0]) +((vectors[fish][1] *vectors[fish][1])); //pythagorean thrm for speed
				values[fish][1] = Math.atan2(vectors[fish][1],vectors[fish][0]);                                          //asin to find angle
			}
		
		return values;
	}
	
	boolean setDriveHeading(double speed, double headingDegrees, double rotation){
		setSpeed = speed;
		setRobotHeading = headingDegrees;
		setRotation = rotation;
		
		setWheelVectors = calculateVectors(setSpeed, setRobotHeading, setRotation);
		setWheelValues = calculateWheelValues(setWheelVectors);
		
		frontLeft.setValues(wheelValues[0][0], wheelValues[0][0]);
		frontRight.setValues(wheelValues[0][0], wheelValues[0][0]);
		backRight.setValues(wheelValues[0][0], wheelValues[0][0]);
		frontLeft.setValues(wheelValues[0][0], wheelValues[0][0]);
		
		
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
	
}
