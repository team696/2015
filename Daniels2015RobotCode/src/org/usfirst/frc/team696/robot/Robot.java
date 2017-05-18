
package org.usfirst.frc.team696.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.VictorSP;
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
	Elevator elevator = new Elevator();
	Joystick control = new Joystick(1);
	Encoder elevEnc = new Encoder(1,2);
	AnalogInput encOne = new AnalogInput(0);
	AnalogInput encTwo = new AnalogInput(1);
	AnalogInput encThree = new AnalogInput(2);
	AnalogInput encFour = new AnalogInput(3);
	double goal;
	DigitalInput atBot = new DigitalInput(0);
	VictorSP elevMotor = new VictorSP(0);
	int[] count = new int[4];
	SwerveTracking one = new SwerveTracking(0);
	SwerveTracking two = new SwerveTracking(0);
	SwerveTracking three = new SwerveTracking(0);
	SwerveTracking four = new SwerveTracking(0);
	double[] wheelEnc = new double[4];
	double[] oldWheelEnc = new double[4];
	int[] forceCounter = new int[4];

    public void robotInit() {
    	one.forceCount(forceCounter[0]);
    	two.forceCount(forceCounter[1]);
    	three.forceCount(forceCounter[2]);
    	four.forceCount(forceCounter[3]);
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
    	//elevator code
    	
//        goal+= (control.getRawAxis(4)/4);
//        elevator.set(elevEnc.get(), goal, atBot.get());
//        elevMotor.set(elevator.move());
//        if (elevator.shouldReset()) elevEnc.reset();
         
        //SwerveTracking code        
    	for(int fish = 0; fish < 4;fish++){
			oldWheelEnc[fish] = wheelEnc[fish];
		}
    	
    	wheelEnc[0] = encOne.getVoltage();
    	wheelEnc[1] = encTwo.getVoltage();
    	wheelEnc[2] = encThree.getVoltage();
    	wheelEnc[3] = encFour.getVoltage();
    	
    	one.set(oldWheelEnc[0], wheelEnc[0]);
    	two.set(oldWheelEnc[1], wheelEnc[1]);
    	three.set(oldWheelEnc[2], wheelEnc[2]);
    	four.set(oldWheelEnc[3], wheelEnc[3]);
        
    	count[0]=one.countNum();
    	count[1]=two.countNum();
    	count[2]=three.countNum();
    	count[3]=four.countNum();
    	
    	//writing to and getting from smart dash  board    	
    	String writeToFile = count[0] + " " + count[1] + " " + count[2]+ " " + count[3] + "";
    	SmartDashboard.putString("appendToFile",writeToFile);
    	String readFromFile = SmartDashboard.getString("readFromFile", "7 7 7 7");
    	
    	
    	//force counter set
    	
    	forceCounter[0] = Integer.parseInt(readFromFile.substring(0,1));
    	forceCounter[1] = Integer.parseInt(readFromFile.substring(2,3));
    	forceCounter[2] = Integer.parseInt(readFromFile.substring(4,5));
    	forceCounter[3] = Integer.parseInt(readFromFile.substring(6));
    	
    	
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}
 