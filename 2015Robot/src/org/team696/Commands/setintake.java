package org.team696.Commands;

import org.team696.autonomous.Command;
import org.team696.robot.Robot;
public class setintake extends Command {
	
	public setintake(Double _speed){
		Robot.elevator.setIntakeMotors(_speed);
	}
	
	@Override
	public void update(){
		isFinished = true;
	}
}
