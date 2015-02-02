package org.team696.baseClasses;

import java.io.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class LocalLogger {
	PrintWriter writer;
	
	
	public String getDate(){
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH/mm/ss");
		return df.format(date);
	}
	
	public LocalLogger()throws FileNotFoundException, UnsupportedEncodingException {
		String fn = "/usr/local/frc/logs/"+getDate()+".txt";
		writer = new PrintWriter(fn);
	}

	public void write(String str){
		writer.println(str);
		writer.flush();
		
	}


}
