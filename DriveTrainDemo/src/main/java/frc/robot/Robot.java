// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the functions corresponding to
 * each mode, as described in the TimedRobot documentation. If you change the name of this class or
 * the package after creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {

	private final int SPARK_MAX_CURRENT_LIMIT = 40;

	private static final String kDefaultAuto = "Default";
	private static final String kCustomAuto = "My Auto";
	private String m_autoSelected;
	private final SendableChooser<String> m_chooser = new SendableChooser<>();

	// create a motor controller for each motor 
	CANSparkMax left1 = new CANSparkMax(2, CANSparkMaxLowLevel.MotorType.kBrushless);
	CANSparkMax left2 = new CANSparkMax(5, CANSparkMaxLowLevel.MotorType.kBrushless);
	CANSparkMax right1 = new CANSparkMax(4, CANSparkMaxLowLevel.MotorType.kBrushless);
	CANSparkMax right2 = new CANSparkMax(3, CANSparkMaxLowLevel.MotorType.kBrushless);

	// create a motor controller group for each side of the drive train
	MotorControllerGroup leftSide = new MotorControllerGroup(left1, left2);
	MotorControllerGroup rightSide = new MotorControllerGroup(right1, right2);

	// instantiate drivetrain from the two spark max groups
	DifferentialDrive driveTrain = new DifferentialDrive(leftSide, rightSide);

	// create a controller
	Joystick controller = new Joystick(0);

	// create two doubles to store the inputs from the controller
	double forwardVelocity;
	double turningVelocity;

	/**
	 * This function is run when the robot is first started up and should be used for any
	 * initialization code.
	 */
	@Override
	public void robotInit() {

		m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
		m_chooser.addOption("My Auto", kCustomAuto);
		SmartDashboard.putData("Auto choices", m_chooser);

		// put all motors into brake mode
		left1.setIdleMode(CANSparkMax.IdleMode.kBrake);
		left2.setIdleMode(CANSparkMax.IdleMode.kBrake);
		right1.setIdleMode(CANSparkMax.IdleMode.kBrake);
		right2.setIdleMode(CANSparkMax.IdleMode.kBrake);

		// limit the current to the motors, to prevent burn outs
		left1.setSmartCurrentLimit(SPARK_MAX_CURRENT_LIMIT);
		left2.setSmartCurrentLimit(SPARK_MAX_CURRENT_LIMIT);
		right1.setSmartCurrentLimit(SPARK_MAX_CURRENT_LIMIT);
		right2.setSmartCurrentLimit(SPARK_MAX_CURRENT_LIMIT);

		// set inversions, to ensure the motors are spinning in the correct direction
		left1.setInverted(false);
		right1.setInverted(true);

		// instruct the second motors to do everything the first motors do
		left2.follow(left1);
		right2.follow(right1);

	}

	/**
	 * This function is called every robot packet, no matter the mode. Use this for items like
	 * diagnostics that you want ran during disabled, autonomous, teleoperated and test.
	 *
	 * <p>This runs after the mode specific periodic functions, but before LiveWindow and
	 * SmartDashboard integrated updating.
	 */
	@Override
	public void robotPeriodic() {
		// TODO: replace axis numbers with the names
		SmartDashboard.putNumber("Forward velocity:", controller.getRawAxis(0));
		SmartDashboard.putNumber("Turning velocity:", controller.getRawAxis(4));
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select between different
	 * autonomous modes using the dashboard. The sendable chooser code works with the Java
	 * SmartDashboard. If you prefer the LabVIEW Dashboard, remove all of the chooser code and
	 * uncomment the getString line to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional comparisons to the switch structure
	 * below with additional strings. If using the SendableChooser make sure to add them to the
	 * chooser code above as well.
	 */
	@Override
	public void autonomousInit() {
		m_autoSelected = m_chooser.getSelected();
		// m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
		System.out.println("Auto selected: " + m_autoSelected);
	}

	/** This function is called periodically during autonomous. */
	@Override
	public void autonomousPeriodic() {
	switch (m_autoSelected) {
		case kCustomAuto:
		// Put custom auto code here
			break;
		case kDefaultAuto:
		default:
		// Put default auto code here
			break;
	}
	}

	/** This function is called once when teleop is enabled. */
	@Override
	public void teleopInit() {}

	/** This function is called periodically during operator control. */
	@Override
	public void teleopPeriodic() {

		// TODO: declare constants for each controller axis. 
		forwardVelocity = controller.getRawAxis(0);
		turningVelocity = controller.getRawAxis(4);
		driveTrain.arcadeDrive(forwardVelocity, turningVelocity, true);

	}

	/** This function is called once when the robot is disabled. */
	@Override
	public void disabledInit() {}

	/** This function is called periodically when disabled. */
	@Override
	public void disabledPeriodic() {}

	/** This function is called once when test mode is enabled. */
	@Override
	public void testInit() {}

	/** This function is called periodically during test mode. */
	@Override
	public void testPeriodic() {}
}
