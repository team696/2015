package org.team696.subsystems;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import com.kauailabs.nav6.frc.IMU;
import com.kauailabs.nav6.frc.IMUAdvanced;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.AnalogTriggerOutput.AnalogTriggerType;

import org.team696.subsystems.SwerveModule;
import org.team696.baseClasses.ModuleConfigs;
import org.team696.baseClasses.Runnable;

public class SwerveDrive extends Runnable{

	private boolean fieldCentric = false;
	
	private double length = 21/12;
	private double width = 33/12;
	private double[] centerArray = {0.0,0.0};
	//first dimension refers to each module from front left to back right
	//in a clockwise order
	//second dimension is each 
	private int[][] moduleChannels = new int[4][5]; 
	
	private double[] setRobotVector = new double[3];       //speed, heading, and rotation Speed (robotCentric)
	
	private double[][] setWheelVectors = new double[4][2];
	
	private double[][] setWheelValues = new double[4][2];
	
	private double[] robotPosition = {0.0,0.0,0.0}; //x, y, and rotation
	
	public SwerveModule frontLeft;
	public SwerveModule frontRight;
	public SwerveModule backRight;
	public SwerveModule backLeft;
		
	IMU navX;
	SerialPort port;
	boolean firstIteration;
	
	public SwerveDrive(ModuleConfigs[] _swerveConfigs)throws FileNotFoundException, UnsupportedEncodingException,IOException{

	    frontLeft = new SwerveModule(_swerveConfigs[0]);
		frontRight = new SwerveModule(_swerveConfigs[1]);
		backRight = new SwerveModule(_swerveConfigs[2]);
		backLeft = new SwerveModule(_swerveConfigs[3]);
		
	    try{
	    	byte updateRateHZ = 50;
	    	port = new SerialPort(57600, SerialPort.Port.kMXP);
	    	navX = new IMUAdvanced(port, updateRateHZ);
	    }catch(Exception ex){System.out.println("NAVX FAILURE!");}
	    
	}
	@Override
	public void start(int periodMS){
		 
		frontLeft.start(periodMS);
		frontRight.start(periodMS);
		backRight.start(periodMS);
		backLeft.start(periodMS);	
		super.start(periodMS);
	}
	@Override
	public void update(){	
		boolean is_calibrating = navX.isCalibrating();
        if ( firstIteration && !is_calibrating ) {
            Timer.delay( 0.3 );
            navX.zeroYaw();
            firstIteration = false;
        }
		setWheelVectors = calculateVectors(setRobotVector[0], setRobotVector[1], setRobotVector[2]);
		setWheelValues = calculateWheelValues(setWheelVectors);
		updateOdometry();
		frontLeft.setValues(setWheelValues[0][0], setWheelValues[0][1]);
		frontRight.setValues(setWheelValues[1][0], setWheelValues[1][1]);
		backRight.setValues(setWheelValues[2][0], setWheelValues[2][1]);
		backLeft.setValues(setWheelValues[3][0], setWheelValues[3][1]);
	}
	
	@Override
	public void stop(){
		frontLeft.stop();
		frontRight.stop();
		backRight.stop();
		backLeft.stop();	
	}
	
	private void updateOdometry(){
		double [][] wheelVectors = new double[4][2];
		
		wheelVectors[0] = frontLeft.getCumVector();
//		System.out.println(frontLeft.getCumVector()[0] + "   " + frontLeft.getCumVector()[1]);
		wheelVectors[1] = frontRight.getCumVector();
		wheelVectors[2] = backRight.getCumVector();
		wheelVectors[3] = backLeft.getCumVector();
		double[] cumVectors = {0.0,0.0};
		for(int fish = 0; fish<1; fish++){
			cumVectors[0] += wheelVectors[fish][0];
			cumVectors[1] += wheelVectors[fish][1];
		}
		double[] cumVectorsPolar = {0.0,0.0};
		cumVectorsPolar[0] = Math.sqrt(Math.pow(cumVectors[0], 2)+ Math.pow(cumVectors[1],2));
		cumVectorsPolar[1] = -Math.toDegrees(Math.atan2(-cumVectors[0],cumVectors[1]));
		cumVectorsPolar[1] = 0;
		cumVectorsPolar[1]+= navX.getYaw();
		
		if(cumVectorsPolar[1]<0) cumVectorsPolar[1]+=360;
		else if(cumVectorsPolar[1]>360) cumVectorsPolar[1]-=360;
		double[] cumVectorsAdjusted = new double[2];
		cumVectorsAdjusted[0] = cumVectorsPolar[0]*Math.sin(Math.toRadians(cumVectorsPolar[1]));
		cumVectorsAdjusted[1] = cumVectorsPolar[0]*Math.cos(Math.toRadians(cumVectorsPolar[1]));
		robotPosition[0] += cumVectorsAdjusted[0]/1000;
		robotPosition[1] += cumVectorsAdjusted[1]/1000;
		robotPosition[2] = navX.getYaw();
	}
	
	public void setSteerPID(double P, double I, double D){
		frontLeft.setSteerPID(P, I, D);
		frontRight.setSteerPID(P, I, D);
		backRight.setSteerPID(P, I, D);
		backLeft.setSteerPID(P, I, D);
	}
//	public void setDrivePID(double P, double I, double D, double F){
//		//frontleft.setDrivePID(P, I, D, F);
//		frontRight.setDrivePID(P, I, D, F);
//		backRight.setDrivePID(P, I, D, F);
//		backLeft.setDrivePID(P, I, D, F);
//	}
	
	void setWheels(){
		frontLeft.setValues(setWheelValues[0][0], setWheelValues[0][1]);
		frontRight.setValues(setWheelValues[1][0], setWheelValues[1][1]);
		backRight.setValues(setWheelValues[2][0], setWheelValues[2][1]);
		backLeft.setValues(setWheelValues[3][0], setWheelValues[3][1]);
	}
	
	double[][] calculateVectors(double speed, double headingDegrees, double rotationRadians){
		double[][] vectors = new double[4][2];
		double headingRadians = Math.toRadians(headingDegrees);
		for(int fish = 0; fish<4; fish++){
			vectors[fish][0] = speed*Math.sin(headingRadians);
			vectors[fish][1] = speed*Math.cos(headingRadians);
			
			//vectors[fish][0] += rotationRadians*Math.cos((Math.PI/4) + (fish*Math.PI/2));
			//vectors[fish][1] += rotationRadians*Math.sin((Math.PI/4) + (fish*Math.PI/2));
		}
//		vectors[0][0] += rotationRadians*length/width;
//		vectors[0][1] += rotationRadians;
//		vectors[1][0] += rotationRadians*length/width;
//		vectors[1][1] -= rotationRadians;
//		vectors[2][0] -= rotationRadians*length/width;
//		vectors[2][1] -= rotationRadians;
//		vectors[3][0] -= rotationRadians*length/width;
//		vectors[3][1] += rotationRadians;
		
		double[][] posArray = new double[4][2]; // measured in theta and R
		posArray[0][0] = Math.atan2(length/2 -centerArray[1], -width/2 -centerArray[0])-Math.toRadians(90);
		posArray[0][1] = rotationRadians*Math.sqrt(Math.pow(-width/2-centerArray[0], 2)+ Math.pow(length/2-centerArray[1], 2));
		posArray[1][0] = Math.atan2(length/2 -centerArray[1], width/2 -centerArray[0])-Math.toRadians(90);
		posArray[1][1] = rotationRadians*Math.sqrt(Math.pow(width/2-centerArray[0], 2)+ Math.pow(length/2-centerArray[1], 2));
		posArray[2][0] = Math.atan2(-length/2 -centerArray[1], width/2 -centerArray[0])-Math.toRadians(90);
		posArray[2][1] = rotationRadians*Math.sqrt(Math.pow(width/2-centerArray[0], 2)+ Math.pow(-length/2-centerArray[1], 2));
		posArray[3][0] = Math.atan2(-length/2 -centerArray[1], -width/2 -centerArray[0])-Math.toRadians(90);
		posArray[3][1] = rotationRadians*Math.sqrt(Math.pow(-width/2-centerArray[0], 2)+ Math.pow(-length/2-centerArray[1], 2));
		
		
		for(int fish = 0; fish < 4; fish++){
			vectors[fish][0] += posArray[fish][1]*Math.cos(posArray[fish][0]);
			vectors[fish][1] += posArray[fish][1]*Math.sin(posArray[fish][0]);
		}
		
		return vectors;
	}
	
	public double[] getPosition(){
		return robotPosition;
	}
	
	double[][] calculateWheelValues(double[][] vectors){
		double values[][] = new double[4][2];
		
		for(int fish = 0; fish<4; fish++){
			values[fish][0] = Math.sqrt(vectors[fish][0] *vectors[fish][0]) +((vectors[fish][1] *vectors[fish][1])); //pythagorean thrm for speed
			values[fish][1] = -Math.toDegrees(Math.atan2(-vectors[fish][0],vectors[fish][1]));
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
	
	public boolean setDriveValues(double speed, double headingDegrees, double rotation, boolean fieldCentric){
		setRobotVector[0] = speed;
		setRobotVector[1] = headingDegrees;
		if(headingDegrees<0) headingDegrees+=360;
		setRobotVector[2] = rotation;
		if(fieldCentric) setRobotVector[1]-= navX.getYaw();
		return true;
	}
	
	public void zeroNavX(){
		navX.zeroYaw();
	}
	
	
}
