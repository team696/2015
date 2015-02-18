package org.team696.Commands;

import org.team696.autonomous.Command;
import org.team696.robot.Robot;

public class setelevator extends Command{
	double setPosition;
	boolean movingUp;
	public setelevator(Double _position, Boolean _parallel){
		setPosition = _position;
		parallel = _parallel;
		movingUp = setPosition>Robot.elevator.getPosition();
		System.out.println("ELEVATOR CONSTRUCTOR   "+ setPosition + "   " + Robot.elevator.getPosition() + "   "+ movingUp);
	}
	@Override
	public void update(){
		System.out.println(setPosition + "   " + Robot.elevator.getPosition() + "   "+ movingUp);
		if(movingUp && setPosition-Robot.elevator.getPosition()>0.4) Robot.elevator.setMotion(true, false);
		else if(!movingUp && setPosition-Robot.elevator.getPosition()<0) Robot.elevator.setMotion(false, true);
		else{
			Robot.elevator.setMotion(false, false);
			isFinished = true;
		}
	}
}
