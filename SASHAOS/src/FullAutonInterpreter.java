
public class FullAutonInterpreter{
	
	public enum PlayEnum {NAV_TO_FEEDERSTATION,
		ALIGN_FEEDERSTATION,
		INTAKE_TOTE_ONE,
		CLAP_TOTE,
		STACK_ONE,
		LIFT_STACK,
		NAV_TO_SCORING_PLATFORM,
		DROP_TOTES,
		DONE};
		
	PlayEnum curPlay;
	GameState curState;
	
	public FullAutonInterpreter(){
		
	}
	
	public boolean hasNextLine(){
		return !(curPlay== PlayEnum.DONE); 
	}
	
	public Command nextLine(){
		Command retCommand;
		switch(curPlay){
		case NAV_TO_FEEDERSTATION:
			if(curState.feederLeftStock>0) retCommand = new navigate(5.0,2.0,30.0,false);
			else if(curState.feederRightStock>0) retCommand = new navigate(5.0, 12.0, 30.0, false);
			else {
				retCommand = new navigate(0.0,0.0,69.0,false);
				curPlay = PlayEnum.DONE;
			}
			curPlay = PlayEnum.ALIGN_FEEDERSTATION;
			break;
		case ALIGN_FEEDERSTATION:
			retCommand = new AlignFeeder();
		default:
			retCommand = new navigate();
		}
		return retCommand;
	}
	
}
