
package org.team696.robot;

import edu.wpi.first.wpilibj.IterativeRobot;

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
	boolean firstRun = true;
	int[][] moduleValues;
	int[] testValues = {1,2,1,1,2};
	//SwerveDrive drive = new SwerveDrive(moduleValues);
	SwerveModule testModule = new SwerveModule(testValues);
	//SteeringEncoder encoderTest = new SteeringEncoder(1);
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
    		firstRun = false;
    	}	
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
