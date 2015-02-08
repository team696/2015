
package org.team696.robot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.team696.baseClasses.*;
import org.team696.subsystems.*;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */			
	
	Joystick        controlBoard;
	public static SwerveDrive     drive;
	//public static Intake          intake;
	//public static AutoCanner      canner;
	//public static Elevator        elevator;	
	
	static Logger          logger;
	
	double          speed;
	int             goalTotes = 0;
	int             temp = 0;
	boolean         intakeWheelsIn  = controlBoard.getRawButton(0);
	boolean         intakeWheelsOut = controlBoard.getRawButton(1);
	boolean         ejectMech       = controlBoard.getRawButton(2);
	boolean         intakeMech      = controlBoard.getRawButton(3);
	boolean         grabBin         = controlBoard.getRawButton(4);
	boolean         leftOut         = controlBoard.getRawButton(5);
	boolean         rightOut        = controlBoard.getRawButton(6);
	boolean         override        = controlBoard.getRawButton(7);
	boolean         moveUp          = controlBoard.getRawButton(8);
	boolean         moveDown        = controlBoard.getRawButton(9);
	boolean         moveUpOld;
	boolean         moveDownOld;
	double          rotation        = 0;
	double          yAxis           = controlBoard.getY();
	double          xAxis           = controlBoard.getX();
	double          trim            = 0;
	ModuleConfigs[] configs         = new ModuleConfigs[4];
	
	public void setConfig(){
		configs[0].kSteerMotor     = 0;
		configs[0].kDriveMotor     = 0;
		configs[0].kSteerEncoder   = 0;
		configs[0].kDriveEncoderA  = 0;
		configs[0].kDriveEncoderB  = 0;
		configs[0].kWheelNumber    = 0;
		configs[0].kReverseEncoder = false;
		configs[0].kReverseMotor   = false;
		
		configs[1].kSteerMotor     = 0;
		configs[1].kDriveMotor     = 0;
		configs[1].kSteerEncoder   = 0;
		configs[1].kDriveEncoderA  = 0;
		configs[1].kDriveEncoderB  = 0;
		configs[1].kWheelNumber    = 0;
		configs[1].kReverseEncoder = false;
		configs[1].kReverseMotor   = false;
		
		configs[2].kSteerMotor     = 0;
		configs[2].kDriveMotor     = 0;
		configs[2].kSteerEncoder   = 0;
		configs[2].kDriveEncoderA  = 0;
		configs[2].kDriveEncoderB  = 0;
		configs[2].kWheelNumber    = 0;
		configs[2].kReverseEncoder = false;
		configs[2].kReverseMotor   = false;
		
		configs[3].kSteerMotor     = 0;
		configs[3].kDriveMotor     = 0;
		configs[3].kSteerEncoder   = 0;
		configs[3].kDriveEncoderA  = 0;
		configs[3].kDriveEncoderB  = 0;
		configs[3].kWheelNumber    = 0;
		configs[3].kReverseEncoder = false;
		configs[3].kReverseMotor   = false;
	}
	
	public void initiate(){
		setConfig();
		controlBoard = new Joystick(0);
		//intake = new Intake(0, 1, 2, 3);
		//canner = new AutoCanner(4, 5);
		//elevator = new Elevator(new int[] {6,7,8,9,10});
		try {
			drive = new SwerveDrive(configs);
			logger = new Logger(new String[] {
					"null"
			});
		} 
		catch(FileNotFoundException fnfE){}
		catch(IOException ioE){}
	}
	
	public void robotInit(){
		//intake.start(20);
		//canner.start(20);
		//elevator.start(20);
		drive.start(10);
		logger.init();
    }

    /**
     * This function is called periodically during autonomous
     */
	@Override
	public void autonomousInit(){
		logger.stop();
		logger.start(20);
	}
	
	
	
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopInit() {   	
//    	testModule.start(10);
    	
    	logger.stop();
    	logger.start(20);
    }
    
    public void teleopPeriodic() {
    	
//    	logger.update();
//    	kP = SmartDashboard.getNumber("kP", 0.04);
//    	kI = SmartDashboard.getNumber("kI", 0.0);
//    	kD = SmartDashboard.getNumber("kD", 0.3);
//    	testModule.setSteerPID(kP, kI, kD);
//    	speed = Math.sqrt(Math.pow(stick.getRawAxis(0),2) + Math.pow(stick.getRawAxis(1),2));
//    	if(Math.abs(stick.getRawAxis(0))>0.05 || Math.abs(stick.getRawAxis(1))>0.05) angle = -Math.toDegrees(Math.atan2(-stick.getRawAxis(0),-stick.getRawAxis(1)));
//    	if(angle<0) angle += 360;
//    	
//    	testModule.setSteerPID(kP, kI, kD);
//    	testModule.setValues(speed/2,angle);
    	
    	intakeWheelsIn  = controlBoard.getRawButton(12);
    	intakeWheelsOut = controlBoard.getRawButton(12);
    	ejectMech       = controlBoard.getRawButton(12);
    	intakeMech      = controlBoard.getRawButton(12);
    	grabBin         = controlBoard.getRawButton(12);
    	leftOut         = controlBoard.getRawButton(12);
    	rightOut        = controlBoard.getRawButton(12);
    	override        = controlBoard.getRawButton(12);
    	moveUpOld 		= moveUp;
    	moveDownOld     = moveDown;
    	moveUp          = controlBoard.getRawButton(12);
    	moveDown        = controlBoard.getRawButton(12);
    	rotation        = Util.deadZone(controlBoard.getZ(), -0.1, 1, 0);
    	yAxis           = controlBoard.getY();
    	xAxis           = controlBoard.getX();
    	
    	//if(intakeWheelsIn)speed=0.75;
    	//if(intakeWheelsOut)speed= -0.75;
    	//intake.setIntake(ejectMech, intakeMech,grabBin, speed);
    	//canner.set(leftOut, rightOut);
    	//elevator.override(override);
    	//elevator.setMotion(moveUp,moveDown);
//    	if (!override){
//    		if(moveUp && !moveDown && !moveUpOld)goalTotes++;
//    		if(!moveUp && moveDown && !moveDownOld)goalTotes--;
//    		elevator.setGoalPos(goalTotes);
//    	}    	
    	
    	if(controlBoard.getRawButton(6)) trim=0.001;
    	else if(controlBoard.getRawButton(8)) trim=-0.001;
    	else trim=0;
    	
    	if(controlBoard.getRawButton(0)) drive.frontLeft.steerEncoder.trimCenter(trim);
    	else if (controlBoard.getRawButton(1))drive.frontRight.steerEncoder.trimCenter(trim);
    	else if (controlBoard.getRawButton(2))drive.backRight.steerEncoder.trimCenter(trim);
    	else if (controlBoard.getRawButton(3))drive.backLeft.steerEncoder.trimCenter(trim);
    	
    	drive.setDriveValues(Math.sqrt((yAxis*yAxis)+(xAxis*xAxis)), Math.atan2(yAxis, xAxis), rotation, false);
    	
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
    @Override
    public void disabledInit() {
    	logger.stop();
//    	testModule.stop();
    }
}
