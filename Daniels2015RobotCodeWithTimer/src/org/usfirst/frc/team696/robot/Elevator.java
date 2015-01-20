package org.usfirst.frc.team696.robot;

/**
 *
 */
public class Elevator {
	private double curPos;
	private double goalPos;
	private double totesHigh;
	private double clicksPerTote = 360;	
	private boolean atBot;
	
	public void set(double _curPos,double _goalPos,boolean _atBot) {
		if (_goalPos > clicksPerTote*7) {
			_goalPos = clicksPerTote*7 - 10;
		}
		curPos = _curPos;
		goalPos = _goalPos;
		atBot = _atBot;
	}
	
	public double move() {
		height();
		double speed = goalPos - curPos;
		if (totesHigh != 6)	Util.deadZone(speed, -0.1, 0.1, 0);
		else if (speed > 0){
			speed = 0;
		}
		return speed;
	}
	
	private void height() {
		totesHigh =  (int)curPos/clicksPerTote;
	}
	
	public boolean shouldReset() {
		if (atBot) return true;
		return false;
	}
}

