package org.team696.Commands;

import org.team696.autonomous.Command;
import org.team696.robot.Robot;

public class setelevator extends Command{
	double setPosition;
	public void setElevator(Double _position){
		setPosition = _position;
	}
	@Override
	public void update(){
		Robot.elevator.setGoalPos(setPosition);
		isFinished = Robot.elevator.atLocation();
	}
	
}
