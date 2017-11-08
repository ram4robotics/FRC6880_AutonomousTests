package org.usfirst.frc.team6880.robot;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends SampleRobot {
	final String defaultAuto = "Default";
	final String customAuto = "My Auto";
	String autoSelected;
	SendableChooser<String> chooser = new SendableChooser<>();
	
	Joystick joystick=null;
	
	//TODO Measure commented values
	final double gearRatio = 10.71; 
	final int countsPerRotation = 1440;
	final double wheelDiameter = 6.0;
	final double robotDiameter = 32.0;
	double inchesPerCount = 0.0, wheelCircumference=0.0, robotCircumference=0.0;
	RobotDrive drivesys=null;
	VictorSP frontRight, rearRight, frontLeft, rearLeft;
	Encoder encoderR=null, encoderL=null;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		chooser.addDefault("Default Auto", defaultAuto);
		SmartDashboard.putData("Auto choices", chooser);
		wheelCircumference = Math.PI*wheelDiameter;
		inchesPerCount = wheelCircumference/1440;
		robotCircumference = Math.PI*robotDiameter;
		
		joystick = new Joystick(0);
		
		autoSelected = chooser.getSelected();
		// autoSelected = SmartDashboard.getString("Auto Selector",
		// defaultAuto);
		System.out.println("Auto selected: " + autoSelected);
		encoderL = new Encoder(0,1,false,Encoder.EncodingType.k4X);
		encoderR = new Encoder(2,3,false,Encoder.EncodingType.k4X);
		encoderL.setMinRate(10);
		encoderL.setDistancePerPulse(inchesPerCount);
		
		encoderR.setMinRate(10);
		encoderR.setDistancePerPulse(inchesPerCount);
		
		frontLeft = new VictorSP(0);
		rearLeft = new VictorSP(1);
		frontRight = new VictorSP(2);
		rearRight = new VictorSP(3);
		drivesys = new RobotDrive(frontLeft, rearLeft, frontRight, rearRight);
	}


	@Override
	public void autonomous() {
		switch (autoSelected) {
		case defaultAuto:
		default:
			//Move forward 20in
			double avgDistanceTravelled = (encoderR.getDistance() + encoderL.getDistance())/2;
			while(avgDistanceTravelled != 240.0){
				drivesys.drive(1.0, 0.0);
				avgDistanceTravelled = (encoderR.getDistance() + encoderL.getDistance())/2;
			}
			drivesys.drive(0.0, 0.0);
			encoderR.reset();
			encoderL.reset();
			
			//Spin 90 degrees
			double arcLength = Math.toRadians(90)*robotDiameter/2;
			double leftDistance = encoderL.getDistance();
			double rightDistance = encoderR.getDistance();
			while(leftDistance != arcLength && rightDistance != -arcLength){
				drivesys.tankDrive(1.0, -1.0);
				leftDistance = encoderL.getDistance();
				rightDistance = encoderR.getDistance();
			}
			drivesys.drive(0.0, 0.0);
			encoderL.reset();
			encoderR.reset();
			break;
		}
	}
	
	@Override
	public void operatorControl(){
		while(isOperatorControl() && isEnabled()){
			drivesys.tankDrive((0.5)*(-joystick.getRawAxis(1)), (0.5)*(-joystick.getRawAxis(5)));
			Timer.delay(0.001);
		}
	}

}

