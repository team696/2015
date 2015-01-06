package com.team696;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.interfaces.Potentiometer;

public class SwerveModule {

	Encoder enc;
	Potentiometer pot;
	
	double setAngle;
	double angle;
	
	double setSpeed;
	double speed;
	
	VictorSP steerMotor;
	VictorSP driveMotor;
	
	PIDController steerController;
	PIDController driveController;
	public SwerveModule(){
		steerController = new PIDController(0, 0, 0, pot, steerMotor);
		driveController = new PIDController(0, 0, 0, 0, enc, driveMotor);
	}
	public SwerveModule(int steerChannel, int driveChannel, int P, int I, int D){
		steerMotor = new VictorSP(steerChannel);
		driveMotor = new VictorSP(driveChannel);
		steerController = new PIDController(0, 0, 0, pot, steerMotor);
		driveController = new PIDController(0, 0, 0, 0, enc, driveMotor);
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
	public void setDrivePID(double P, double I, double D){
		driveController.setPID(P, I, D);
	}
	
}
