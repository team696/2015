package org.team696.robot;
import org.team696.baseClasses.Runnable;

import edu.wpi.first.wpilibj.Timer;

public class SwerveDrive extends Runnable{
	Timer timer = new Timer();
	double oldTimer = 0.0;
	public SwerveDrive(){
		System.out.println("making swerve");
	}
	
	public void start(){
		super.start();
		timer.start();
		System.out.println("starting");
	}
	
	public void update(){
		super.update();
		System.out.println(1/(timer.get()-oldTimer));
		oldTimer = timer.get();
	}
}
