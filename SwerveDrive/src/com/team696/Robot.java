
package com.team696;

import edu.wpi.first.wpilibj.IterativeRobot;
import com.team696.SwerveDrive;

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
	SwerveDrive drive = new SwerveDrive(1,2,3,4,5,6,7,8);
	
    public void robotInit() {
    	drive.setSteerPID(0, 0, 0);
    	drive.setDrivePID(0, 0, 0);
    	drive.
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
        
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
