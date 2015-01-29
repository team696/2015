package org.SavchenkovsRobots;


/**
 * S.A.S.H.A DVA; Specific Autonomous Scripting Handiness Algorithm Deterministic Variable Access
 */

public class SashaMachine {
	public enum GameState 
	{	INTERPRET_CODE,
		NAVIGATE,
		LIFT_TOTE,
		LOWER_TOTE,
		INTAKE_TOTE,
		FINISHED
	};
	public static void main(String[] arrgz) {
		GameState state = GameState.INTERPRET_CODE;
		String RawString = "navigate: 3 , 4, 30 \n navigate: 2 , 2, 0";
		String parsedCode = RawString.replace(" ", "");
		String[] lines =RawString.split("\n");
		int curLine = 0;
		
		while(state!= GameState.FINISHED){
			if(state == GameState.INTERPRET_CODE){
				if(curLine>=lines.length){ //if we're at the end of the code finish the program
					state = GameState.FINISHED;
				}else{		                                   //command reader
					String[] splitCommand = lines[curLine].split(":");
					double[] args = {0.0, 0.0, 0.0};
					
					if (splitCommand.length > 1) {
						String[] argsString = Util.split(splitCommand[1], ",");
						args = new double[argsString.length];
						
						for (int jesus = 0; jesus < argsString.length; jesus++) {
							args[jesus] = Double.parseDouble(argsString[jesus]);
						}
						
						if(splitCommand[0].equalsIgnoreCase("navigate")){
							if(args.length ==3){
								state= GameState.NAVIGATE;
							}
						}
					}
					curLine++;
				}
			}else{//actual interpreter
				switch(state){
					case NAVIGATE:
						
						System.out.println("running navigate");
						if(NavCommand.nav()) state = GameState.INTERPRET_CODE;
						break;
						
				}
				
				
			}
			
		}
		System.out.println("finished");
	}
	
	
	

}
