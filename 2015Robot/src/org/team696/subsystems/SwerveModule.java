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
import org.team696.baseClasses.ModuleConfigs;
import org.team696.baseClasses.Util;

public class SwerveModule extends Runnable{

	//Encoder driveEncoder;
	public SteeringEncoder steerEncoder;
	ModuleConfigs configs;
	double[] odometryVector = {0.0,0.0};
	
	double lastEncoderCount = 0;
	
	boolean override = false;
	double overrideSteer = 0;
	double overrideSpeed = 0;
	double setAngle;
	double angle;
	
	double setSpeed;
	double speed;
	
	Victor steerMotor;
	Victor driveMotor;
	
	//PIDController driveController;
	CustomPID steerController;
	
	
	public SwerveModule(ModuleConfigs _configs)throws FileNotFoundException, UnsupportedEncodingException,IOException{
		configs = _configs;
		steerMotor = new Victor(configs.kSteerMotor);
		driveMotor = new Victor(configs.kDriveMotor);
		steerEncoder = new SteeringEncoder(configs.kSteerEncoder,configs.kWheelNumber);
//		steerController = new CustomPID(0.05,0, 0.3);
		steerController = new CustomPID(0.03,0, 0.2);
//		driveEncoder = new Encoder(configs.kDriveEncoderA, configs.kDriveEncoderB);
		}

	@Override
	public void start(int periodMS){
		override = false;
		super.start(periodMS);
		steerEncoder.start(10);
		//driveEncoder.reset();
	}
	
	@Override
	public void stop(){
		steerEncoder.stop();
		super.stop();
	}
	
	@Override
	public void update(){
		super.update();
		
		//System.out.println(configs.kWheelNumber + "  " + steerEncoder.getAngleDegrees() + "  "+steerMotor.get());

		//odometryVector[0] += (driveEncoder.getDistance()-lastEncoderCount)*Math.sin(Math.toRadians(angle));
		//odometryVector[1] += (driveEncoder.getDistance()-lastEncoderCount)*Math.cos(Math.toRadians(angle));
		//lastEncoderCount = driveEncoder.getDistance();
		odometryVector[0] = 0.0;
		odometryVector[1] = 0.0;
		
		
		angle = steerEncoder.getAngleDegrees();
		if(angle<0) angle = 360+angle;
		double error = 0.0;
		error = setAngle - angle;
		//org.team696.robot.Robot.logger.set(error, 2);
		//System.out.print(setAngle+ "   ");
		//System.out.print(angle+ "   ");
		//System.out.print(error+ "   ");
		boolean reverseMotor =  false;
		if(error>180) error = -(360-error);  //check if over the
		else if(error<-180) error = (360+error);//zero line to flip error 
		//System.out.print(error+ "   ");
		//org.team696.robot.Robot.logger.set(error, 3);
		
		if(error > 90){
			error = -(180-error);
			reverseMotor = true;
		}else if(error<-90){
			error = -(180+error);
			reverseMotor = true;
		}
		//System.out.print(error+ "   ");
		//System.out.println();
		//org.team696.robot.Robot.logger.set(error, 4);
		//org.team696.robot.Robot.logger.set(angle, 0);
		//org.team696.robot.Robot.logger.set(setAngle, 1);
		
		steerController.update(-error);
		if(override) steerMotor.set(overrideSteer);
		else if(Math.abs(steerController.getOutput())>0.1) {
			double tmp = steerController.getOutput();
			tmp =Util.constrain(tmp, -0.3, 0.3);
			steerMotor.set(tmp);
		}
		else steerMotor.set(0);
		if(override) driveMotor.set(overrideSpeed);
		else if(reverseMotor) driveMotor.set(-setSpeed);
		else driveMotor.set(setSpeed);
	}
	
	public void setValues(double _setSpeed, double _setAngleDegrees){
		setSpeed = _setSpeed;
		if(_setAngleDegrees <0) _setAngleDegrees +=360;
		setAngle = _setAngleDegrees;
	}
	
	public void setSteerPID(double P, double I, double D){
		steerController.setConstants(P, I, D);
	}
	
	public void override(boolean _override, double _overrideSteer, double _overrideSpeed){
		override = _override;
		overrideSteer = _overrideSteer;
		overrideSpeed = _overrideSpeed;
	}
	
	public double[] getCumVector(){
		double[] vector = odometryVector;
		odometryVector[0] = 0;
		odometryVector[1] = 0;
		return vector;
	}
}
