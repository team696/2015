
package org.team696.robot;

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
	static String[] configName = new String[] {"1","2","3","4","5"};
	public static Logger logger = new Logger(configName);
	double x = 0;
	double y = 0;
	boolean firstRun = true;
	int[][] moduleValues;
	static int[] testValues = {0,1,0,0,1};
	Joystick stick = new Joystick(0);
	double kP=0;
	double kI=0;
	double kD=0;
	double angle=0;
	double speed=0;
	//SwerveDrive drive = new SwerveDrive(moduleValues);
	public static SwerveModule testModule = new SwerveModule(testValues);
	//SteeringEncoder encoderTest = new SteeringEncoder(0);
    public void robotInit() {
    	//drive.start(100);
    	
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
    	if(firstRun){
    		testModule.start(10);
    		//encoderTest.start(5);
    		firstRun = false;
    	}
    	
    	kP = SmartDashboard.getNumber("kP", 0.07);
    	kI = SmartDashboard.getNumber("kI", 0.0);
    	kD = SmartDashboard.getNumber("kD", 0.5);
    	testModule.setSteerPID(kP, kI, kD);
    	speed = Math.sqrt(Math.pow(stick.getRawAxis(0),2) + Math.pow(stick.getRawAxis(1),2));
    	if(Math.abs(stick.getRawAxis(0))>0.05 || Math.abs(stick.getRawAxis(1))>0.05) angle = Math.toDegrees(Math.atan2(-stick.getRawAxis(0),-stick.getRawAxis(1)));
    	if(angle<0) angle += 360;
    	
    	testModule.setSteerPID(kP, kI, kD);
    	testModule.setValues(speed,angle);
    	
    	
    	
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
