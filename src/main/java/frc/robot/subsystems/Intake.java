package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.motors.TalonFXLance;
import java.lang.invoke.MethodHandles;

/**
 * Use this class as a template to create other subsystems.
 */
public class Intake extends SubsystemLance 
{
  // This string gets the full name of the class, including the package name
  private static final String fullClassName = MethodHandles.lookup().lookupClass().getCanonicalName();

  // *** STATIC INITIALIZATION BLOCK ***
  // This block of code is run first when the class is loaded
  static 
  {
    System.out.println("Loading: " + fullClassName);
  }

  // *** INNER ENUMS and INNER CLASSES ***
  public enum Direction 
  {
    kForward(0.1),
    kBackward(-0.1),
    kOff(0.0);

    public final double value;

    private Direction(double value) 
    {
      this.value = value;
    }
  }

  public enum Action 
  {
    kPickup,
    kEject;
  }

  // Put all inner enums and inner classes here
  private class PeriodicData 
  {
    // INPUTS
    private double topRollerPosition = 0.0;
    private double bottomRollerPosition = 0.0;
    private double topRollerVelocity;
    private double bottomRollerVelocity;

    // OUTPUTS
    // private double topRollerSpeed = 0.0;
    // private double bottomRollerSpeed = 0.0;
  }

  // *** CLASS VARIABLES & INSTANCE VARIABLES ***
  // Put all class variables and instance variables here
  private final PeriodicData periodicData = new PeriodicData();
  private final TalonFXLance topMotor = new TalonFXLance(4, Constants.ROBORIO, "Top Motor");
  private final TalonFXLance bottomMotor = new TalonFXLance(12, Constants.ROBORIO, "Bottom Motor");

  private final double GEAR_RATIO = 1.0 / 5.0; // previously 1.0 / 25.0
  private final double WHEEL_DIAMETER_FEET = 2.25 / 12.0;
  private final double MINUTES_TO_SECONDS = 1.0 / 60.0;
  private final double RPM_TO_FPS = GEAR_RATIO * MINUTES_TO_SECONDS * Math.PI * WHEEL_DIAMETER_FEET;
  // private final double PERCENT_VOLTAGE = 0.9;
  // private final double VOLTAGE = PERCENT_VOLTAGE * Constants.END_OF_MATCH_BATTERY_VOLTAGE;
  private final double DEFAULT_SPEED = 0.9;

  // *** CLASS CONSTRUCTORS ***
  // Put all class constructors here

  /**
   * Creates a new Intake.
   */
  public Intake() 
  {
    super("Intake");
    System.out.println("  Constructor Started:  " + fullClassName);

    configMotors();
    setDefaultCommand(stopCommand());

    // SendableRegistry.addLW(this, "Intake", "MY Subsystem");
    // addChild("Motor 1", motor1);
    // addChild("Motor 2", motor2);

    System.out.println("  Constructor Finished: " + fullClassName);
  }

  // *** CLASS METHODS & INSTANCE METHODS ***
  // Put all class methods and instance methods here

  private void configMotors() 
  {
    // Factory Defaults
    topMotor.setupFactoryDefaults();
    bottomMotor.setupFactoryDefaults();
    // Do Not Invert Motor Direction
    topMotor.setupInverted(false); // test later
    bottomMotor.setupInverted(true); // test later
    // Set Coast Mode
    topMotor.setupCoastMode();
    bottomMotor.setupCoastMode();
    // topMotor.setupPIDController(0, periodicData.kP, periodicData.kI, periodicData.kD);
    // bottomMotor.setupPIDController(0, 17.0, 0.0, 0.0);

    topMotor.setupVelocityConversionFactor(RPM_TO_FPS);

    topMotor.setupCurrentLimit(30.0, 35.0, 0.5);
    topMotor.setSafetyEnabled(false);
    bottomMotor.setSafetyEnabled(false);
  }

  /**
   * Returns the value of the sensor
   * @return The value of periodData.sensorValue
   */
  public void on(double speed) 
  {
    topSetVoltage(speed * Constants.END_OF_MATCH_BATTERY_VOLTAGE);
    bottomSetVoltage(speed * Constants.END_OF_MATCH_BATTERY_VOLTAGE);
  }

  public void stop() 
  {
    // periodicData.speed = 0.0;
    topSetVoltage(0.0 * Constants.END_OF_MATCH_BATTERY_VOLTAGE);
    bottomSetVoltage(0.0 * Constants.END_OF_MATCH_BATTERY_VOLTAGE);
  }

  public double getTopPosition() 
  {
    return periodicData.topRollerPosition;
  }

  public double getBottomPosition() 
  {
    return periodicData.bottomRollerPosition;
  }

  public void pickup() 
  {
    topSetVoltage(1.0 * Constants.END_OF_MATCH_BATTERY_VOLTAGE);
    bottomSetVoltage(1.0 * Constants.END_OF_MATCH_BATTERY_VOLTAGE);
  }

  public void eject() 
  {
    topSetVoltage(-1.0 * Constants.END_OF_MATCH_BATTERY_VOLTAGE);
    bottomSetVoltage(-1.0 * Constants.END_OF_MATCH_BATTERY_VOLTAGE);
  }

  private void topSetVoltage(double voltage) 
  {
    topMotor.setVoltage(voltage);
  }

  private void bottomSetVoltage(double voltage) 
  {
    bottomMotor.setVoltage(voltage);
  }

  private void topSet(double speed) 
  {
    topMotor.set(speed);
  }

  private void bottomSet(double speed) 
  {
    bottomMotor.set(speed);
  }

  public double getTopVelocity() 
  {
    return periodicData.topRollerVelocity;
  }

  public double getBottomVelocity() 
  {
    return periodicData.bottomRollerVelocity;
  }

  public Command pickupFrontCommand() 
  {
    return Commands.run(() -> pickup(), this).withName("Pickup");
  }

  public Command ejectCommand() {
    return Commands.run(() -> eject(), this).withName("Eject");
  }

  public Command stopCommand() {
    return Commands.runOnce(() -> stop(), this).withName("Stop");
  }
  // *** OVERRIDEN METHODS ***
  // Put all methods that are Overridden here
  @Override
  public void readPeriodicInputs() 
  {}

  @Override
  public void writePeriodicOutputs() 
  {}

  @Override
  public void periodic() 
  {
    // This method will be called once per scheduler run
    periodicData.topRollerPosition = topMotor.getPosition();
    periodicData.bottomRollerPosition = bottomMotor.getPosition();
    periodicData.topRollerVelocity = topMotor.getVelocity();
    periodicData.bottomRollerVelocity = bottomMotor.getVelocity();
  }

  @Override
  public void simulationPeriodic() 
  {
    // This method will be called once per scheduler run during simulation
  }

  @Override
  public String toString() 
  {
    return "";
  }
}