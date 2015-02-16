
package org.team696.robot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.team696.baseClasses.*;
import org.team696.subsystems.*;

import com.kauailabs.nav6.frc.*;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.SerialPort.Port;

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
	boolean 		calibrate 		= false;
	
	
	Joystick        controlBoard = new Joystick(0);
	Joystick 		joyStick   = new Joystick(1);
	PowerDistributionPanel pdp = new PowerDistributionPanel();
//	public static SwerveModule testModule;
	public static SwerveDrive     drive;
//	public static Intake          intake;
	//public static AutoCanner      canner;
	public static Elevator        elevator;	
	
//	static Logger          logger;
	
	double          speed;
	int             goalTotes = 0;
	int             temp = 0;
	boolean         intakeWheelsIn  = controlBoard.getRawAxis(3)<-0.5;
	boolean			intakeWheelsOut = controlBoard.getRawAxis(3)>0.5;
//	boolean         ejectMech       = controlBoard.getRawButton(3);
//	boolean         intakeMech      = controlBoard.getRawButton(4);
	boolean         moveUp          = controlBoard.getRawButton(9);
	boolean         moveDown        = controlBoard.getRawButton(10);
	boolean 		moveRight 		= false;
	boolean 		moveLeft  		= false;
	boolean 		openIntakeButton		=false;
	boolean 		oldOpenIntakeButton	=openIntakeButton;
	boolean			upOneTote= false;
	boolean			oldUpOneTote = upOneTote;
	boolean 		downOneTote = false;
	boolean			oldDownOneTote = downOneTote;
	
	boolean 		openIntake		= true;
	boolean			eject			= false;
	boolean			fieldCentric	= true;
	boolean			fieldCentricButton = false;
	boolean 		oldFieldCentricButton = false;
	boolean         moveUpOld;
	boolean         moveDownOld;
	double          rotation        = 0;
	double          yAxis           = controlBoard.getY();
	double          xAxis           = controlBoard.getX();
	double          trim            = 0;
	boolean			write			= false;
	boolean			oldWrite		= write;
	ModuleConfigs[] configs;

	
	public void setConfig(){
		configs         = new ModuleConfigs[4];
		for(int fish=0;fish<4;fish++){
			configs[fish] = new ModuleConfigs();
		}
		configs[0].kSteerMotor     = 16;
		configs[0].kDriveMotor     = 0;
		configs[0].kSteerEncoder   = 2;
		configs[0].kDriveEncoderA  = 7;
		configs[0].kDriveEncoderB  = 6;
		configs[0].kWheelNumber    = 1;
		configs[0].kReverseEncoder = false;
		configs[0].kReverseMotor   = false;
//		configs[0].kCenter         = 47.85;
		
		configs[1].kSteerMotor     = 6;
		configs[1].kDriveMotor     = 5;
		configs[1].kSteerEncoder   = 1;
		configs[1].kDriveEncoderA  = 5;
		configs[1].kDriveEncoderB  = 4;
		configs[1].kWheelNumber    = 2;
		configs[1].kReverseEncoder = false;
		configs[1].kReverseMotor   = false;
//		configs[1].kCenter         = 69.14;
		
		configs[2].kSteerMotor     = 7;
		configs[2].kDriveMotor     = 8;
		configs[2].kSteerEncoder   = 0;
		configs[2].kDriveEncoderA  = 3;
		configs[2].kDriveEncoderB  = 2;
		configs[2].kWheelNumber    = 3;
		configs[2].kReverseEncoder = false;
		configs[2].kReverseMotor   = false;
//		configs[2].kCenter         = 79.3;
		
		configs[3].kSteerMotor     = 17;
		configs[3].kDriveMotor     = 18;
		configs[3].kSteerEncoder   = 3;
		configs[3].kDriveEncoderA  = 9;
		configs[3].kDriveEncoderB  = 8;
		configs[3].kWheelNumber    = 4;
		configs[3].kReverseEncoder = false;
		configs[3].kReverseMotor   = false;
//		configs[3].kCenter         = 29.3;
		
	}
	
	public String getDate(){
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		return df.format(date);
	}
	
	public void robotInit(){
		elevator = new Elevator(new int[] {1,0,2,3,4,5,3,4,1,2,3});
//		intake = new Intake(69, 1, 4);
		setConfig();
		try {
			drive = new SwerveDrive(configs);
//			testModule = new SwerveModule(configs[2]);
//			logger = new Logger(new String[] {
//					"",
//					""
//			},"/usr/local/frc/logs/"+getDate()+".txt");
		} 
		catch(FileNotFoundException fnfE){}
		catch(IOException ioE){}
//		logger.init();
    }

    /**
     * This function is called periodically during autonomous
     */
	@Override
	public void autonomousInit(){
//		logger.stop();
//		logger.start(20);
	}
	
	
	
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    @Override
    public void teleopInit() {
    	drive.start(10);
//    	testModule.start(10);
//    	intake.start(20);
    	elevator.start(10);
    }
    
    public void teleopPeriodic() {
    	
    	
    	
    	calibrate = joyStick.getRawButton(3);
    	if(calibrate) calibrate();
    	else robotCode();
    }
    
    public void calibrate(){
//    	moveLeft = controlBoard.getRawButton(8);
//    	moveRight = controlBoard.getRawButton(6);
//    	
//    	if(moveRight)trim = 0.5;
//    	else if(moveLeft)trim = -0.5;
//    	else trim = 0;
    	
    	trim = joyStick.getRawAxis(0)*2;
    	if(joyStick.getRawButton(6))drive.frontLeft.steerEncoder.trimCenter(trim);
    	else drive.frontLeft.steerEncoder.trimCenter(0);
    	if(joyStick.getRawButton(11))drive.frontRight.steerEncoder.trimCenter(trim);
    	else drive.frontRight.steerEncoder.trimCenter(0);
    	if(joyStick.getRawButton(7))drive.backRight.steerEncoder.trimCenter(trim);
    	else drive.backRight.steerEncoder.trimCenter(0);
    	if(joyStick.getRawButton(10))drive.backLeft.steerEncoder.trimCenter(trim);
    	else drive.backLeft.steerEncoder.trimCenter(0);
    	System.out.println("In Calibrate    "+trim);

//    	if(controlBoard.getRawButton(1))drive.frontLeft.steerEncoder.trimCenter(trim);
//    	else drive.frontLeft.steerEncoder.trimCenter(0);
//    	if(controlBoard.getRawButton(4))testModule.override(true,trim/2, 0.2);
//    	else testModule.override(false, 0,0);
    	
//    	System.out.println(drive.frontLeft.steerEncoder.getAngleDegrees() + "   " +
//    				drive.frontRight.steerEncoder.getAngleDegrees() + "   " +
//    				drive.backRight.steerEncoder.getAngleDegrees() + "   " +
//    				drive.backLeft.steerEncoder.getAngleDegrees() + "   ");
    	
//    	if(controlBoard.getRawButton(2))drive.frontRight.override(true, trim, 0.2);
//    	else drive.frontRight.override(true, 0.0, 0);
//    	if(controlBoard.getRawButton(3))drive.backRight.override(true, trim, 0.2);
//    	else drive.backRight.override(true, 0.0, 0);
//    	if(controlBoard.getRawButton(4))drive.backLeft.override(true, trim, 0.2);
//    	else drive.backLeft.override(true, 0.0, 0);
    	oldWrite = write;
    	write = joyStick.getRawButton(1);
    	if(write && !oldWrite){
    		drive.frontLeft.steerEncoder.writeOffset();
    		drive.frontRight.steerEncoder.writeOffset();
    		drive.backLeft.steerEncoder.writeOffset();
    		drive.backRight.steerEncoder.writeOffset();
//    		testModule.steerEncoder.writeOffset();
    	}
    	
    	
    	
    }
    public void robotCode(){
    	intakeWheelsIn  = controlBoard.getRawAxis(3)<-0.5;
    	intakeWheelsOut = controlBoard.getRawAxis(3)>0.5;
//    	ejectMech       = controlBoard.getRawButton(12);
//    	intakeMech      = controlBoard.getRawButton(12);
    	moveUp          = controlBoard.getRawButton(1);
    	moveDown        = controlBoard.getRawButton(3);
    	
    	oldFieldCentricButton = fieldCentric;
    	fieldCentricButton = controlBoard.getRawButton(10);
    	if(fieldCentric&& !oldFieldCentricButton) fieldCentric = !fieldCentric;
    	oldOpenIntakeButton = openIntakeButton;
    	openIntakeButton = controlBoard.getRawButton(5);
    	oldUpOneTote = upOneTote;
    	upOneTote = controlBoard.getRawButton(6);
    	oldDownOneTote = downOneTote;
    	downOneTote = controlBoard.getRawButton(7);
    	
    	rotation        = Util.deadZone(controlBoard.getRawAxis(0), -0.1, 0.1, 0)/2;
    	yAxis           = -Util.deadZone(Util.map(joyStick.getRawAxis(1), -1, 1, 1.5, -1.5),-0.1,0.1,0);
    	xAxis           = Util.deadZone(Util.map(joyStick.getRawAxis(0), -1, 1, 1.5, -1.5),-0.1,0.1,0);
    	
    	double angle;
    	if(Math.abs(xAxis)<0.1 && Math.abs(yAxis)<0.1) angle = 0;
    	else  angle = Math.toDegrees(-Math.atan2(xAxis, -yAxis));
    	if(angle<0) angle+=360;
    	
    	drive.setDriveValues(Math.sqrt((yAxis*yAxis)+(xAxis*xAxis))/2, angle, rotation, fieldCentric);
//    	testModule.setValues(Math.sqrt((yAxis*yAxis)+(xAxis*xAxis))/2, angle);
    	

    	if(openIntakeButton && !oldOpenIntakeButton) openIntake = true;
    	else openIntake = false;
    	
    	elevator.setIntake(intakeWheelsOut, openIntake, intakeWheelsIn);
    	
    	if(controlBoard.getRawButton(2)) elevator.reset();
    	if(joyStick.getRawButton(1)) drive.zeroNavX();
//    	testModule.override(controlBoard.getRawButton(2), controlBoard.getRawAxis(2));
//    	System.out.println(controlBoard.getRawButton(1)+"    " + xAxis);
    	
		if(moveUp){
			elevator.setMotion(true,false);
			elevator.overrideMotion();
		}
		else if(moveDown){
			elevator.setMotion(false, true);
			elevator.overrideMotion();
		} else if (upOneTote && !oldUpOneTote) {
			goalTotes++;
			elevator.regularMotion();
		}else if (downOneTote && !oldDownOneTote) {
			goalTotes--;
			elevator.regularMotion();
		}else {
			elevator.overrideMotion();
			elevator.setMotion(false, false);
			elevator.firstTime();
		}
		
//		else if(upOneTote && !oldUpOneTote){
//			goalTotes++;
//			elevator.regularMotion();
//		}
//		else if(downOneTote && !oldDownOneTote){
//			goalTotes--;
//			elevator.regularMotion();
//		else if(upOneTote && !oldUpOneTote){
//			goalTotes++;
//			elevator.regularMotion();
//		}
//		else if(downOneTote && !oldDownOneTote){
//			goalTotes--;
//			elevator.regularMotion();
//		}
//		if(elevator.atLocation()){
//			elevator.overrideMotion();
//			elevator.setMotion(false,false);
//		} else elevator.setGoalPos(goalTotes);
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
    @Override
    public void disabledInit() {
//    	logger.stop();
//    	testModule.stop();
    	drive.stop();
//    	elevator.stop();
    }
}
