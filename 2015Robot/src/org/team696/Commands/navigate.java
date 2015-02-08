package org.team696.Commands;

import org.team696.autonomous.Command;
import org.team696.baseClasses.*;
import org.team696.robot.Robot;

public class navigate extends Command{
	
	CustomPID speedController;
	CustomPID rotationController;
	int counter = 0;
	double[] navVector = {0.0,0.0,0.0};//x, y, and rotation
	double[] position = {0.0,0.0,0.0};
	double[] setVector = {0.0,0.0,0.0}; // theta, speed, and rotation
	
	
	public navigate(Double xGoal,Double yGoal,Double rotDegrees, Boolean _parallel) {
		parallel = _parallel;
		navVector[0] = xGoal;
		navVector[1] = yGoal;
		navVector[2] = rotDegrees;
		rotationController.setConstants(0.01, 0, 0.01);
		speedController.setConstants(0.01, 0, 0.01);
	}
	
	@Override
	public void update(){
		position = Robot.drive.getPosition();
		
		setVector
		
		rotationController.update(navVector[2]-position[2]);
		
	}
	
	
}
