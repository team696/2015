package org.team696.baseClasses;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import edu.wpi.first.wpilibj.Timer;

public class Logger extends Runnable {
	Timer timer = new Timer();
	LocalLogger usrLogger;
	
	String[] names; 
	String[] values;
	String toSend;
	boolean write = false;
	boolean dontPut = false;
	
	public Logger(String[] configName) throws FileNotFoundException, UnsupportedEncodingException{
		usrLogger = new LocalLogger();
		names = new String[configName.length];
		values = new String[names.length];
		for(int fish = 0; fish < configName.length;fish++){
			names[fish] = configName[fish];
		}
	}
	
	public void init() {
		write = false;
	}
	
	@Override
	public void start(int frequency){
		super.start(frequency);
		toSend = "";
		timer.start();
		write = false;
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(){
		setString();
	}
	
	public void stop(){
		write = true;
		timer.stop();
		timer.reset();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		write = false;
	}
	
	public void set(int val,int pos){
		values[pos] = val+"";
		if (values[pos].length() >= 5)values[pos]=values[pos].substring(0, 5);
		if(values[pos].length()<5){
			int lenDif =5-values[pos].length();
			for(int fish = 0; fish < lenDif;fish++){
				values[pos]+=" ";
			}
		}
	}
	
	public void set(double val,int pos){
		values[pos] = val+"";
		if (values[pos].length() >= 5)values[pos]=values[pos].substring(0, 5);
		if(values[pos].length()<5){
			int lenDif = 5-values[pos].length();
			for(int fish = 0; fish < lenDif;fish++){
				values[pos]+=" ";
				System.out.println(values[pos].length());
			}
		}
	}
	
	public void set(boolean val,int pos){
		values[pos] = val+"";
		if (values[pos].length() > 5)values[pos]=values[pos].substring(0, 5);
		if(values[pos].length()<5){
			for(int fish = 0; fish < 5-values[pos].length();fish++){
				values[pos]+=" ";
			}
		}
	}
	
	public void set(float val,int pos){
		values[pos] = val+"";
		if (values[pos].length() > 5)values[pos]=values[pos].substring(0, 5);
		if(values[pos].length()<5){
			for(int fish = 0; fish < 5-values[pos].length();fish++){
				values[pos]+=" ";
			}
		}
	}
	
	public String setTime() {
		dontPut = false;
		if (timer.get() < 0.1)dontPut = true;
		String time = timer.get()+" | ";
		if (time.length() > 5)time=time.substring(0, 5);
		if(time.length()<5){
			for(int fish = 0; fish < 5-time.length();fish++){
				time+=" ";
			}
		}
		return time;
	}
	
	public void setString(){
		toSend = "time: "+ setTime() + " | ";
		for(int fish = 0; fish < names.length;fish++){
			toSend = toSend + names[fish] + ": " + values[fish] + " | ";
		}
		if(dontPut)toSend="";
		else toSend+="\n";
		sendString();
	}
	
	public void sendString(){
		usrLogger.write(toSend);
		
	}
}
