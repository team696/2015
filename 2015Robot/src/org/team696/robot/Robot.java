
package org.team696.robot;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.team696.baseClasses.*;
import org.team696.subsystems.*;
import org.team696.autonomous.Scheduler;

import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
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
	boolean 		calibrate 		= false;
	
	Joystick        controlBoard = new Joystick(0);
	Joystick 		joyStick   = new Joystick(1);
	PowerDistributionPanel pdp = new PowerDistributionPanel();
	BuiltInAccelerometer accelerometer = new BuiltInAccelerometer();
//	public static SwerveModule testModule;
	public static SwerveDrive     drive;
	public static Elevator        elevator;	
	
	public static Logger          logger;
	
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
	boolean 		closeIntakeButton		=false;
	boolean 		oldCloseIntakeButton	=closeIntakeButton;
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
	double			lastRotation 	= 0;
	double          yAxis           = controlBoard.getY();
	double          xAxis           = controlBoard.getX();
	double          trim            = 0;
	boolean			write			= false;
	boolean			oldWrite		= write;
	ModuleConfigs[] configs;
	
//	Solenoid intakeOpen = new Solenoid(5);
	
	Scheduler autonScheduler = new Scheduler();
	
	
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
		
		configs[1].kSteerMotor     = 6;
		configs[1].kDriveMotor     = 5;
		configs[1].kSteerEncoder   = 1;
		configs[1].kDriveEncoderA  = 5;
		configs[1].kDriveEncoderB  = 4;
		configs[1].kWheelNumber    = 2;
		configs[1].kReverseEncoder = false;
		configs[1].kReverseMotor   = false;
		
		configs[2].kSteerMotor     = 7;
		configs[2].kDriveMotor     = 8;
		configs[2].kSteerEncoder   = 0;
		configs[2].kDriveEncoderA  = 3;
		configs[2].kDriveEncoderB  = 2;
		configs[2].kWheelNumber    = 3;
		configs[2].kReverseEncoder = false;
		configs[2].kReverseMotor   = false;

		configs[3].kSteerMotor     = 17;
		configs[3].kDriveMotor     = 18;
		configs[3].kSteerEncoder   = 3;
		configs[3].kDriveEncoderA  = 9;
		configs[3].kDriveEncoderB  = 8;
		configs[3].kWheelNumber    = 4;
		configs[3].kReverseEncoder = false;
		configs[3].kReverseMotor   = false;
		
	}
	
	public String getDate(){
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		return df.format(date);
	}
	
	public void robotInit(){
		logger = new Logger(new String[] {"Accelerometer X","Accelerometer Y","Accelerometer Z"}, "/usr/local/frc/logs/"+getDate()+".txt");
		logger.makeWriter();
		
		elevator = new Elevator();
		setConfig();
		try {
			drive = new SwerveDrive(configs);
//			testModule = new SwerveModule(configs[2]);
		} 
		catch(FileNotFoundException fnfE){}
		catch(IOException ioE){}
		
    }

    /**
     * This function is called periodically during autonomous
     */
	@Override
	public void autonomousInit(){
		
    	elevator.start(10);
    	drive.start(10);
		drive.zeroNavX();
		
		String autonScript = SmartDashboard.getString("autonCode", "StringNotFound");
		System.out.println(autonScript);
		autonScheduler.setScript(autonScript);
		autonScheduler.start(20);
		
	}
	
	
	
    public void autonomousPeriodic() {

    }

    @Override
    public void teleopInit() {
    	drive.stop();
    	elevator.stop();
    	autonScheduler.stop();
    	System.out.println("stopping scheduler");
    	drive.start(10);
//    	testModule.start(10);
    	elevator.start(10);
    	logger.start(20);
    	logger.write("============================\n");
    	logger.write("        Teleop Init\n");
    	logger.write("============================\n");
    }
    
    public void teleopPeriodic() {
    	
    	logger.set(accelerometer.getX(), 0);
    	logger.set(accelerometer.getY(), 1);
    	logger.set(accelerometer.getZ(), 2);
//    	System.out.println(accelerometer.getX()+"   "+accelerometer.getY()+"   "+accelerometer.getZ());
    	
    	calibrate = joyStick.getRawButton(3);
    	if(calibrate) calibrate();
    	else robotCode();
    	
    }
    
    public void calibrate(){
    	
    	oldWrite = write;
    	write = joyStick.getRawButton(1);    	
    	trim = joyStick.getRawAxis(0)*2;
    	
    	if(joyStick.getRawButton(6))drive.frontLeft.steerEncoder.trimCenter(trim);
    	else drive.frontLeft.steerEncoder.trimCenter(0);
    	if(joyStick.getRawButton(11))drive.frontRight.steerEncoder.trimCenter(trim);
    	else drive.frontRight.steerEncoder.trimCenter(0);
    	if(joyStick.getRawButton(10))drive.backRight.steerEncoder.trimCenter(trim);
    	else drive.backRight.steerEncoder.trimCenter(0);
    	if(joyStick.getRawButton(7))drive.backLeft.steerEncoder.trimCenter(trim);
    	else drive.backLeft.steerEncoder.trimCenter(0);

    	if(write && !oldWrite){
    		drive.frontLeft.steerEncoder.writeOffset();
    		drive.frontRight.steerEncoder.writeOffset();
    		drive.backLeft.steerEncoder.writeOffset();
    		drive.backRight.steerEncoder.writeOffset();
    	}
    }
    
    public void robotCode(){
    	
    	moveUp          = controlBoard.getRawButton(1);
    	moveDown        = controlBoard.getRawButton(3);
    	
    	intakeWheelsIn  = controlBoard.getRawAxis(3)<-0.5;
    	intakeWheelsOut = controlBoard.getRawAxis(3)>0.5;
    	
//    	intakeOpen.set(joyStick.getRawButton(5));
    	
//    	testModule.setValues(Math.sqrt((yAxis*yAxis)+(xAxis*xAxis))/2, angle);
//    	testModule.override(controlBoard.getRawButton(2), controlBoard.getRawAxis(2));
    	if(joyStick.getRawButton(9)) drive.zeroOdometry();
    	oldFieldCentricButton = fieldCentricButton;
    	fieldCentricButton = controlBoard.getRawButton(10);
    	if(fieldCentricButton&& !oldFieldCentricButton) fieldCentric = !fieldCentric;
    	oldCloseIntakeButton = closeIntakeButton;
    	closeIntakeButton = controlBoard.getRawButton(5);
//    	oldUpOneTote = upOneTote;
//    	upOneTote = controlBoard.getRawButton(6);
//    	oldDownOneTote = downOneTote;
//    	downOneTote = controlBoard.getRawButton(7);
    	
    	rotation        = Util.deadZone(controlBoard.getRawAxis(0), -0.1, 0.1, 0)/2;
    	yAxis           = -Util.deadZone(Util.map(joyStick.getRawAxis(1), -1, 1, 1.5, -1.5),-0.1,0.1,0);
    	xAxis           = Util.deadZone(Util.map(joyStick.getRawAxis(0), -1, 1, 1.5, -1.5),-0.1,0.1,0);
    	
//    	System.out.print((int)drive.frontLeft.steerEncoder.offset+ "   ");
//    	System.out.print((int)drive.frontRight.steerEncoder.offset+ "   ");
//    	System.out.print((int)drive.backLeft.steerEncoder.offset+ "   ");
//    	System.out.print((int)drive.backLeft.steerEncoder.offset+ "   ");
//    	
//    	System.out.print((int)drive.frontLeft.steerEncoder.countOffset+ "   ");
//    	System.out.print((int)drive.frontRight.steerEncoder.countOffset+ "   ");
//    	System.out.print((int)drive.backLeft.steerEncoder.countOffset+ "   ");
//    	System.out.print((int)drive.backLeft.steerEncoder.countOffset+ "   ");
//    	
//    	System.out.print((int)drive.frontLeft.steerEncoder.getAngleDegrees()+ "   ");
//    	System.out.print((int)drive.frontRight.steerEncoder.getAngleDegrees()+ "   ");
//    	System.out.print((int)drive.backLeft.steerEncoder.getAngleDegrees()+ "   ");
//    	System.out.println((int)drive.backLeft.steerEncoder.getAngleDegrees()+ "   ");
    	
//    	System.out.println(drive.getPosition()[0]+ "   " + drive.getPosition()[1]+ "   " + drive.getPosition()[2]);    	
    	double angle;
    	if(Math.abs(xAxis)<0.1 && Math.abs(yAxis)<0.1) angle = 0;
    	else  angle = Math.toDegrees(-Math.atan2(xAxis, -yAxis));
    	if(angle<0) angle+=360;
    	
//    	if(Math.abs(rotation)<0.1 && Math.abs(lastRotation)>0.1){
//    		drive.setSteerControl(true); 
//    		drive.setSteerControlInput(drive.getPosition()[2]);
//    	}
//    	else if(Math.abs(rotation)>0.1 && Math.abs(lastRotation)<0.1) drive.setSteerControl(false);
    	
    	if(controlBoard.getRawButton(8))drive.setDriveValues(Math.sqrt((yAxis*yAxis)+(xAxis*xAxis))/3, angle, rotation, fieldCentric);
    	else drive.setDriveValues(Math.sqrt((yAxis*yAxis)+(xAxis*xAxis))/2, angle, rotation*3, fieldCentric);
    	
    	System.out.println(drive.getPosition()[0]+ "   " + drive.getPosition()[1] + "   " + drive.getPosition()[2]);
    	
//    	if(openIntakeButton && !oldOpenIntakeButton) elevator.toggleIntake();
    	elevator.setIntakeOverride(controlBoard.getRawButton(6));
    	elevator.setIntakeOpen(!closeIntakeButton);
    	
//    	elevator.setEjector(controlBoard.getRawButton(6));
    	if(intakeWheelsIn) elevator.setIntakeMotors(1.0);
    	else if(intakeWheelsOut) elevator.setIntakeMotors(-1.0);
    	else elevator.setIntakeMotors(0);
    	
    	if(controlBoard.getRawButton(2)) elevator.reset();
    	if(joyStick.getRawButton(1)) drive.zeroNavX();
    	if(moveUp){
			elevator.setMotion(true,false);
			elevator.overrideMotion();
		} else if(moveDown){
			elevator.setMotion(false, true);
			elevator.overrideMotion();
//		} else if (upOneTote && !oldUpOneTote) {
//			elevator.increment(true);
//			elevator.regularMotion();
//		}else if (downOneTote && !oldDownOneTote) {
//			elevator.increment(false);
//			elevator.regularMotion();
		}else {
			elevator.overrideMotion();
			elevator.setMotion(false, false);
			elevator.firstTime();
		}		
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
    	autonScheduler.stop();
    	drive.stop();
    	elevator.stop();
    }
}
