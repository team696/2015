package org.usfirst.frc.team696.robot;

public class SwerveTracking extends JavaReminder{
	public SwerveTracking(int seconds) {
		super(seconds);
		// TODO Auto-generated constructor stub
	}

	private static double curPos;
	private static double disPerSec = 102.85714285714285714285714285714;
	private static int counter = 0;
	private static double oldIn;
	private static double newIn;	
	
	
	public void set(double _oldIn,double _newIn) {
		curPos = Util.map(_newIn, 0, 5, 0, 360);
		oldIn = _oldIn;
		newIn = _newIn;
		
		
	}
	
	public static void tracker() {
		
		boolean testClockWise = oldIn<1 && newIn>4;
		boolean testCounterClockWise = oldIn>4 && newIn<1;
		if(testClockWise) counter-=1;
		if(testCounterClockWise) counter+=1;
		if(counter == 7) counter = 0;
		if(counter==-1)counter=6;
		
	}


	public double pos() {
		return disPerSec*counter + (curPos/7)*2;	
	}
	
	public void forceCount(int _counter) {
		counter = _counter;
	}
	
	public int countNum() {
		JavaReminder r = new JavaReminder(1);
		return counter;
	}
	
	
	
}