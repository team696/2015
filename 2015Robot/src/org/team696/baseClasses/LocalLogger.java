package org.team696.baseClasses;

import java.io.*;

public class LocalLogger {
	//String fn = "/usr/local/frc/logs/logger.txt";
	//File file = new File(fn);
	//PrintWriter writer = new PrintWriter(file);
	//BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(FileOutputStream(fn)), false);
	//PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(FileOutputStream(fn))));
	PrintWriter writer; // = new PrintWriter(fn, "US-ASCII");
	
	public LocalLogger()throws FileNotFoundException, UnsupportedEncodingException {
		String fn = "/usr/local/frc/logs/logger.txt";
		//File file = new File(fn);
		//PrintWriter writer = new PrintWriter(file);
		//BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(FileOutputStream(fn)), false);
		//PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(FileOutputStream(fn))));
		writer = new PrintWriter(fn/*, "US-ASCII"*/);
	}


	public void write(String hi){
		writer.println(hi);
		writer.flush();
		
	}


}
