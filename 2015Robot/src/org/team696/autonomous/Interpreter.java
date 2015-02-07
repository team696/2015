package org.team696.autonomous;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;



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
		
		Class command = Command.class;
		
		Command newCommand = new Command();
		String[] splitCommand = lines[curLine].split(":");
		double[] args = {};
		if(splitCommand.length>1){
			String[] argsString = splitCommand[1].split(",");
			args = new double[argsString.length];
			for(int jesus = 0; jesus <argsString.length; jesus++){
				args[jesus] =  Double.parseDouble(argsString[jesus]);
			}
			
			try{
				command = Class.forName(splitCommand[0].toLowerCase());
				Class[] classArg = new Class[args.length+1];
				Object[] arguments = new Object[args.length+1];
				for(int fish = 0; fish < args.length; fish++){
					classArg[fish] = Double.class;
				}
				classArg[classArg.length-1] = Boolean.class;
				
				for(int fish = 0; fish < args.length; fish++){
					arguments[fish] = args[fish];
				}
				arguments[arguments.length-1] = false;
				
				Constructor constructor = command.getDeclaredConstructor(classArg);
				System.out.println(constructor.getParameterCount());
				newCommand = (Command) constructor.newInstance(arguments);
				
			}catch(ClassNotFoundException ex){ex.printStackTrace();}
			catch(NoSuchMethodException ex){ex.printStackTrace();}
			catch(InstantiationException ex){ex.printStackTrace();}
			catch(IllegalAccessException ex){ex.printStackTrace();}
			catch(InvocationTargetException ex){ex.printStackTrace();}
			
		}
		curLine ++;
		return newCommand;
	}
	
}
