package org.team696.autonomous;

import org.team696.Commands.navigate;
import org.team696.Commands.openintake;
import org.team696.robot.Robot;

public class FullAutonInterpreter{
	
	public enum FeederPlaybook {
		NAV_TO_FEEDERSTATION,
		ALIGN_FEEDERSTATION,
		INTAKE_TOTE_ONE,
		CLAP_TOTE,
		STACK_ONE,
		LIFT_STACK,
		NAV_TO_SCORING_PLATFORM,
		DROP_TOTES,
		DONE};
		
	public enum LandfillPlaybook {
		FIND_CAN,
		NAV_TO_CAN,
		SETUP_FOR_CAN,
		NAV_INTO_CAN,
		INTAKE_CAN,
		TAKE_CAN_TO_WALL,
		LET_CAN_GO,
		NAV_AWAY_FROM_CAN,
		DROP_ELEVATOR_TO_CAN,
		CLAMP_CAN,
		NAV_TO_LANDFILL,
		LIFT_CAN,
		INTAKE_TOTE,		//
		STACK_TOTE,			//REPEAT 4X
		NAV_TO_NEXT_TOTE,	//
		SCORE_STACK,
		DONE};	
	GameState curState;
	LandfillPlaybook curPlay = LandfillPlaybook.FIND_CAN;
	
	public FullAutonInterpreter(){
		
	}
	
	public boolean hasNextLine(){
		return !(curPlay== LandfillPlaybook.DONE); 
	}
	
	public Command nextLine(){
		Command retCommand;
		switch(curPlay){
		case FIND_CAN:
			double[] curBotPosition = Robot.drive.getPosition();
			retCommand = new navigate(	curBotPosition[0],
										curBotPosition[1],
										curBotPosition[2]+ 30);
			
			break;
		case CLAMP_CAN:
			retCommand = new openintake();
		default:
			retCommand = new openintake();
		}
		return retCommand;
	}
	
}
