package com.team696;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;

public class SwerveModule {

	Encoder driveEncoder;
	Encoder steerEncoder;
	
	double setAngle;
	double angle;
	
	double setSpeed;
	double speed;
	
	VictorSP steerMotor;
	VictorSP driveMotor;
	
	PIDController steerController;
	PIDController driveController;
	
	public SwerveModule(int[] configs){
		steerMotor = new VictorSP(configs[0]);
		driveMotor = new VictorSP(configs[1]);
		steerEncoder = new Encoder(configs[2], configs[3]);
		driveEncoder = new Encoder(configs[4], configs[5]);
		steerController = new PIDController(0, 0, 0, steerEncoder, steerMotor);
		driveController = new PIDController(0, 0, 0, 0, driveEncoder, driveMotor);
	}
	
	public void enablePID(boolean steer, boolean drive){
		if(steer) steerController.enable();
		else steerController.disable();
		
		if(drive) driveController.enable();
		else driveController.disable();
	}
	
	public void setValues(double speed, double angleDegrees){
		steerController.setSetpoint(angleDegrees);
		driveController.setSetpoint(speed);
	}
	
	public void setSteerPID(double P, double I, double D){
		steerController.setPID(P, I, D);
	}
	
	public void setDrivePID(double P, double I, double D, double F){
		driveController.setPID(P, I, D, F);
	}
	
	public boolean updateOdometry(){
		
		return true;
	}
	
	public double[] getOdometry(){
		double[] x = {0,0};
		return x;
	}
}
