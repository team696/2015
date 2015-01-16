
package org.team696;

import org.team696.SwerveDrive;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.I2C.Port;
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
	
	int[][] swerveChannels = new int[4][5];
	SwerveDrive drive;
	
    public void robotInit() {
    	drive = new SwerveDrive(swerveChannels);
    	drive.setDrivePID(0, 0, 0, 0);
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
