package org.team696.Commands;

import org.team696.autonomous.Command;
import org.team696.robot.Robot;

public class closeintake extends Command {

	@Override
	public void update() {
		Robot.elevator.setIntakeOpen(false);
		isFinished = true;
	}
}
