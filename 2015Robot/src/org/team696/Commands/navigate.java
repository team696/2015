package org.team696.Commands;

import org.team696.autonomous.Command;
import org.team696.baseClasses.*;
import org.team696.robot.Robot;

public class navigate extends Command{
	
	CustomPID speedController;
	int counter = 0;
	double[] navVector = {0.0,0.0,0.0};//x, y, and rotation
	
	public navigate(Double xGoal,Double yGoal,Double rotDegrees, Boolean _parallel) {
		parallel = _parallel;
		navVector[0] = xGoal;
		navVector[1] = yGoal;
		navVector[2] = rotDegrees;
	}
	
	@Override
	public void update(){
		
	}
	
	private double getDistance(){
		Robot.
	}
}
