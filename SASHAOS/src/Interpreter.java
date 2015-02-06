

public class Interpreter{
	String rawString = "navigate: 3 , 4, 30 \n navigate: 2 , 20, 0";
	String parsedCode;
	String[] lines;
	int curLine = 0;
	
	public Interpreter(String code){
		rawString = code;
		lines =rawString.split("\n");
		
		for (int fish = 0; fish < lines.length; fish++) {
			lines[fish] = lines[fish].replace(" ", "");
		}
		
	}
	
	public boolean hasNextLine(){
		return curLine<lines.length;
	}
	
	public Command nextLine(){
		
		Command newCommand = new Command();
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
					System.out.println(args[0]+args[1]+args[2]);
					newCommand = new ExtendedCommand(0,0,0, false);
				case 1:
					System.out.println(args[0]+args[1]+args[2]);
					newCommand = new ExtendedCommand((int) args[0],0,0, false);
				case 2:
					System.out.println(args[0]+args[1]+args[2]);
					newCommand = new ExtendedCommand((int) args[0],(int)args[1], 0, false);
				case 3:
					System.out.println(args[0]+args[1]+args[2]);
					newCommand = new ExtendedCommand((int) args[0],(int) args[1],(int) args[2], false);
				}
			}
		}
		curLine ++;
		return newCommand;
	}
	
}
