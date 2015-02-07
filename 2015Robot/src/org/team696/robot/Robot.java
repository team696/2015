
package org.team696.robot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.team696.baseClasses.Logger;
import org.team696.subsystems.SwerveModule;

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
	public static Logger logger;
	//0   			1				2			3		4					5					6					7				8				9				10				11				13				14
	//static String[] configName = new String[] {"AutoCannerLeft","AutoCannerRight","Elevator","Intake","SteeringEncoder1","SteeringEncoder2","SteeringEncoder3","SteeringEncoder4","SwerveModule1","SwerveModule2","SwerveModule2","SwerveModule3","SwerveModule4","SwerveDrive"};
	//public static Logger logger = new Logger(configName);
	public Robot() throws FileNotFoundException, UnsupportedEncodingException,IOException{	
		logger = new Logger(new String[] {"Empty"});
		testModule = new SwerveModule(testValues);
	}
	
	double x = 0;
	double y = 0;
	boolean firstRun = true;
	int[][] moduleValues;
	static int[] testValues = {0,1,0,0,1,1};
	Joystick stick = new Joystick(0);
	double kP=0;
	double kI=0;
	double kD=0;
	double angle=0;
	double speed=0;
	//SwerveDrive drive = new SwerveDrive(moduleValues);
	public static SwerveModule testModule;
	public void robotInit() {
    	//drive.start(100);
    	logger.init();
    }

    /**
     * This function is called periodically during autonomous
     */
	@Override
	public void autonomousInit(){
	}
	
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopInit() {
    	logger.start(20);
    	testModule.start(10);
    }
    
    public void teleopPeriodic() {
    	
    	logger.update();
    	kP = SmartDashboard.getNumber("kP", 0.04);
    	kI = SmartDashboard.getNumber("kI", 0.0);
    	kD = SmartDashboard.getNumber("kD", 0.3);
    	testModule.setSteerPID(kP, kI, kD);
    	speed = Math.sqrt(Math.pow(stick.getRawAxis(0),2) + Math.pow(stick.getRawAxis(1),2));
    	if(Math.abs(stick.getRawAxis(0))>0.05 || Math.abs(stick.getRawAxis(1))>0.05) angle = -Math.toDegrees(Math.atan2(-stick.getRawAxis(0),-stick.getRawAxis(1)));
    	if(angle<0) angle += 360;
    	
    	testModule.setSteerPID(kP, kI, kD);
    	testModule.setValues(speed/2,angle);
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
    @Override
    public void disabledInit() {
    	logger.stop();
    	testModule.stop();
    }
}
