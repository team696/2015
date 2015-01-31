

public class Interpreter{
	String rawString = "navigate: 3 , 4, 30 \n navigate: 2 , 2, 0";
	String parsedCode;
	String[] lines;
	int curLine = 0;
	
	public Interpreter(String code){
		rawString = code;
		parsedCode = rawString.replace(" ", "");
		lines =rawString.split("\n");
	}
	
	public Command nextLine(){
		//Command newCommand = new Command(0, 0, 0, false);
		Command newCommand = new Command(0,0, 0,false);
		String[] splitCommand = lines[curLine].split(":");
		double[] args = {};
		if(splitCommand.length>1){
			String[] argsString = splitCommand[1].split(",");
			args = new double[argsString.length];
			for(int jesus = 0; jesus <argsString.length; jesus++){
				args[jesus] =  Double.parseDouble(argsString[jesus]);
			}
			if(splitCommand[0].equalsIgnoreCase("navigate")){
				switch(args.length){
				case 0:
					return new ExtendedCommand(0,0,0, false);
				case 1:
					return new ExtendedCommand((int) args[0],0,0, false);
				case 2:
					return new ExtendedCommand((int) args[0],(int)args[1], 0, false);
				case 3:
					return new ExtendedCommand((int) args[0],(int) args[1],(int) args[2], false);
				}
			}
		}
		curLine ++;
		return newCommand;
	}
	
}
