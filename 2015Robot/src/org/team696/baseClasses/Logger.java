package org.team696.baseClasses;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.wpi.first.wpilibj.Timer;

public class Logger extends Runnable {
	PrintWriter writer;
	FileReader reader;
	Timer timer = new Timer();
	BufferedReader br;
	String[] names; 
	String[] values;
	String toSend;
	String fn;
	boolean write = false;
	boolean dontPut = false;
	
	public String getDate(){
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
		return df.format(date);
	}
	
	public void setPath(String path){
		fn = "path";
	}
	
	public void setPath(){
		fn = "/usr/local/frc/logs/"+getDate()+".txt";
	}
	
	public String[] read(int lines) throws IOException{
		String[] iRead = new String[lines];
		for(int fish=0;fish<lines;fish++){
			iRead[fish]=br.readLine();
		}
		return iRead;
	}
	
	public Logger(String[] configName) throws FileNotFoundException, UnsupportedEncodingException,IOException{
		
		writer = new PrintWriter(fn);
		reader = new FileReader(fn);
		br = new BufferedReader(reader);
		
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
	public void start(int periodMS){
		super.start(periodMS);
		toSend = "";
		timer.start();
		write = false;
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
	
	public void set(String val,int pos){
		values[pos] = val+"";
		if (values[pos].length() >= 10)values[pos]=values[pos].substring(0, 10);
		if(values[pos].length()<10){
			int lenDif =10-values[pos].length();
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
	
	public void setString(boolean noTime){
		toSend = "";
		if (!noTime)setString();
		else {
			for(int fish = 0; fish < names.length;fish++){
				toSend = toSend + names[fish] + ": " + values[fish] + " | ";
			}
		}
		sendString();
	}
	
	private void setString(){
		toSend = "time: "+ setTime() + " | ";
		for(int fish = 0; fish < names.length;fish++){
			toSend = toSend + names[fish] + ": " + values[fish] + " | ";
		}
		sendString();
	}
	
	public void write(String str){
		if(!dontPut){
			writer.println(str);
			writer.flush();
		}
	}
	
	public void sendString(){
		write(toSend);
	}
}
