package org.team696;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.AnalogPotentiometer;

public class SwerveModule {

	Encoder driveEncoder;
	AnalogPotentiometer steeringPot;
	
	double setAngle;
	double angle;
	
	double setSpeed;
	double speed;
	
	VictorSP steerMotor;
	VictorSP driveMotor;
	
	PIDController driveController;
	CustomPID steerController;
	public SwerveModule(int[] configs){
		steerMotor = new VictorSP(configs[0]);
		driveMotor = new VictorSP(configs[1]);
		steeringPot = new AnalogPotentiometer(configs[2]);
		driveEncoder = new Encoder(configs[3], configs[4]);
		driveController = new PIDController(0, 0, 0, 0, driveEncoder, driveMotor);
	}
	
	public void enablePID(boolean drive){
		
		if(drive) driveController.enable();
		else driveController.disable();
	}
	
	public void setValues(double speed, double angleDegrees){
		driveController.setSetpoint(speed);
	}
	
	public void setSteerPID(double P, double I, double D){
		//steerController.setPID(P, I, D);
	}
	
	public void setDrivePID(double P, double I, double D, double F){
		driveController.setPID(P, I, D, F);
	}
	
	public boolean updateOdometry(){
		
		return true;
	}
	
	public double[] getPosition(){
		double[] x = {0,0};
		return x;
	}
}
