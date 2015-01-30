package org.team696.baseClasses;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Logger extends Runnable {
	Timer timer = new Timer();
	String[] names; 
	String[] values;
	String toSend;
	
	public Logger(String[] configName){
		names = new String[configName.length];
		values = new String[names.length];
		for(int fish = 0; fish < configName.length;fish++){
			names[fish] = configName[fish];
		}
	}
	
	@Override
	public void start(int frequency){
		super.start(frequency);
		toSend = "";
		timer.start();
	}
	
	@Override
	public void update(){
		setString();
	}
	
	public void set(int val,int pos){
		values[pos] = val+"";
		if (values[pos].length() > 5)values[pos]=values[pos].substring(0, 5);
		if(values[pos].length()<5){
			for(int fish = 0; fish < 5-values[pos].length();fish++){
				values[pos]+=" ";
			}
		}
	}
	
	public void set(double val,int pos){
		values[pos] = val+"";
	}
	
	public void set(boolean val,int pos){
		values[pos] = val+"";
	}
	
	public void set(float val,int pos){
		values[pos] = val+"";
	}
	
	public void setString(){
		for(int fish = 0; fish < names.length;fish++){
			toSend = toSend + names[fish] + ": " + values[fish] + " | ";
		}
		toSend = "time: "+ timer.get() + toSend;
		toSend+="\n";
		sendString();
	}
	
	public void sendString(){
		SmartDashboard.putString("toAppend", toSend);
	}
}
