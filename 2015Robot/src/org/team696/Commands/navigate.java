package org.team696.Commands;

import org.team696.autonomous.Command;
import org.team696.baseClasses.*;
import org.team696.robot.Robot;

public class navigate extends Command{
	
	CustomPID speedController;
	CustomPID rotationController;
	int counter = 0;
	
	boolean finalWayPoint;
	double[] navVector = {0.0,0.0,0.0};//x, y, and rotation
	double[] position = {0.0,0.0,0.0};
	double[] setVector = {0.0,0.0,0.0}; // theta, speed, and rotation
	
	
	public navigate(Double xGoal,Double yGoal,Double rotDegrees,Double speed, Double _waypointRadius, boolean _finalWayPoint, Boolean _parallel) {
		parallel = _parallel;
		navVector[0] = xGoal;
		navVector[1] = yGoal;
		navVector[2] = rotDegrees;
		finalWayPoint = _finalWayPoint;
		rotationController.setConstants(0.01, 0, 0.01);
		speedController.setConstants(0.01, 0, 0.01);
	}
	
	@Override
	public void update(){
		position = Robot.drive.getPosition();
		
		setVector[0] = Math.atan2(navVector[0]-position[0],navVector[1]-position[1]);
		double distance = Math.sqrt(Math.pow(navVector[0]-position[0], 2) + Math.pow(navVector[1]-position[1], 2));
		if(distance> 12) setVector[1] = 0.6;
		else speedController.update(distance);
		
		rotationController.update(navVector[2]- position[2]);
		
		if(distance<3) isFinished = true;
		
	}
	
	
}
