package org.team696.Commands;

import org.team696.robot.Robot;
import org.team696.autonomous.Command;

public class openintake extends Command{

	@Override
	public void update() {
		Robot.elevator.setIntake(false, true, false);
		isFinished = true;
	}
	
}
