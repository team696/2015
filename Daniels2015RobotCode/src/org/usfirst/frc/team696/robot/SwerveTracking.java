package org.usfirst.frc.team696.robot;

public class SwerveTracking {
	private double curPos;
	private double disPerSec = 102.85714285714285714285714285714;
	private int counter = 0;
	private double oldIn;
	private double newIn;
	
	public void set(double _oldIn,double _newIn) {
		this.curPos = Util.map(_newIn, 0, 5, 0, 360);
		this.oldIn = _oldIn;
		this.newIn = _newIn;
	}
	
	private void tracker() {
		boolean testClockWise = oldIn<1 && newIn>4;
		boolean testCounterClockWise = oldIn>4 && newIn<1;
		if(testClockWise) this.counter-=1;
		if(testCounterClockWise) this.counter+=1;
		if(counter == 7) this.counter = 0;
		if(counter==-1)this.counter=6;
	}

	public double pos() {
		return disPerSec*this.counter + (this.curPos/7)*2;	
	}
	
	public void forceCount(int _counter) {
		this.counter = _counter;
	}
	
	public int countNum() {
		tracker();
		return this.counter;
	}
	
	
	
}