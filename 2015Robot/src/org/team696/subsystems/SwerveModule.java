package org.team696.subsystems;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

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
	int wheel;
	
	Talon steerMotor;
	Talon driveMotor;
	
	//PIDController driveController;
	CustomPID steerController;
	
	
	public SwerveModule(int[] configs, int _wheel)throws FileNotFoundException, UnsupportedEncodingException,IOException{
		wheel = _wheel;
		steerMotor = new Talon(configs[0]);
		driveMotor = new Talon(configs[1]);
		steerEncoder = new SteeringEncoder(configs[2],wheel);
		steerController = new CustomPID(0.05,0, 0.3);
		driveEncoder = new Encoder(configs[3], configs[4]);
		//driveController = new PIDController(0, 0, 0, 0, driveEncoder, driveMotor);
	}
	
//	public void enablePID(boolean drive){
//		
//		if(drive) driveController.enable();
//		else driveController.disable();
//	}
	@Override
	public void start(int periodMS){
		super.start(periodMS);
		steerEncoder.start(5);
		driveEncoder.reset();
	}
	@Override
	public void update(){
		super.update();
		
		angle = steerEncoder.getAngleDegrees();
		if(angle<0) angle = 360+angle;
		double error = 0.0;
		error = setAngle - angle;
		//org.team696.robot.Robot.logger.set(error, 2);
		boolean reverseMotor =  false;
		if(error>180) error = -(360-error);  //check if over the
		else if(error<-180) error = (360+error);//zero line to flip error 
		//org.team696.robot.Robot.logger.set(error, 3);
		
		if(error > 90){
			error = -(180-error);
			reverseMotor = true;
		}else if(error<-90){
			error = -(180+error);
			reverseMotor = true;
		}
		//org.team696.robot.Robot.logger.set(error, 4);
		//org.team696.robot.Robot.logger.set(angle, 0);
		//org.team696.robot.Robot.logger.set(setAngle, 1);
		
		steerController.update(-error);
		if(Math.abs(steerController.getOutput())>0.1) steerMotor.set(steerController.getOutput());
		else steerMotor.set(0);
		if(reverseMotor) driveMotor.set(-setSpeed);
		else driveMotor.set(setSpeed);
		SmartDashboard.putNumber("angle", angle);
		SmartDashboard.putNumber("setAngle", setAngle);
		SmartDashboard.putNumber("output", steerController.getOutput());
	}
	
	public void setValues(double _setSpeed, double _setAngleDegrees){
		setSpeed = _setSpeed;
		if(_setAngleDegrees <0) _setAngleDegrees +=360;
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
		vector[0] = driveEncoder.getRate()*Math.sin(Math.toRadians(angle));
		vector[1] = driveEncoder.getRate()*Math.cos(Math.toRadians(angle));
		return vector;
	}
}
