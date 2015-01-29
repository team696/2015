package org.team696.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.team696.baseClasses.CustomPID;
import org.team696.baseClasses.Runnable;

public class SwerveModule extends Runnable{

	Encoder driveEncoder;
	SteeringEncoder steerEncoder;
	
	double setAngle;
	double angle;
	
	double setSpeed;
	double speed;
	
	Talon steerMotor;
	Talon driveMotor;
	
	PIDController driveController;
	CustomPID steerController;
	
	
	public SwerveModule(int[] configs){
		steerMotor = new Talon(configs[0]);
		driveMotor = new Talon(configs[1]);
		steerEncoder = new SteeringEncoder(configs[2]);
		steerController = new CustomPID(0.05,0, 0.3);
		driveEncoder = new Encoder(configs[3], configs[4]);
		driveController = new PIDController(0, 0, 0, 0, driveEncoder, driveMotor);
	}
	
//	public void enablePID(boolean drive){
//		
//		if(drive) driveController.enable();
//		else driveController.disable();
//	}
	@Override
	public void start(int periodMS){
		super.start(periodMS);
		steerEncoder.start(1000/200);
	}
	@Override
	public void update(){
		super.update();
		angle = steerEncoder.getAngleDegrees();
		if(angle<0) angle = 360+angle;
		double error = 0.0;
		error = setAngle - angle;
		boolean reverseMotor =  false;
		if(error>180) error = -(360-error);    //check if over the
		else if(error<-180) error = (360+error);//zero line to flip error 
		
		if(error > 90){
			error = -(180-error);
			reverseMotor = true;
		}else if(error<-90){
			error = -(180+error);
			reverseMotor = true;
		}
		steerController.update(-error);
		if(steerController.getOutput()>0.1) steerMotor.set(steerController.getOutput());
		else steerMotor.set(0);
		if(reverseMotor) driveMotor.set(-setSpeed);
		else driveMotor.set(setSpeed);
	}
	
	public void setValues(double _setSpeed, double _setAngleDegrees){
		setSpeed = _setSpeed;
		setAngle = _setAngleDegrees;
	}
	
	public void setSteerPID(double P, double I, double D){
		steerController.setConstants(P, I, D);
	}
	
//	public void setDrivePID(double P, double I, double D, double F){
//		driveController.setPID(P, I, D, F);
//	}
	
	public double[] getVelocity(){
		
		double[] vector = {0.0,0.0};
		return vector;
	}
	
	public double[] getPosition(){
		double[] x = {0,0};
		return x;
	}
}
