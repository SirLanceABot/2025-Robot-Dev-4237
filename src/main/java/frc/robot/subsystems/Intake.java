package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.motors.SparkMaxLance;
import frc.robot.motors.TalonFXLance;
import java.lang.invoke.MethodHandles;

/**
 * This is the Intake subsystem
 * @author Tanishka
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
    // Put all inner enums and inner classes here
    /** 
     * This class sets the direction the motor is going in
     */
 
    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    // private final TalonFXLance motor = new TalonFXLance(Constants.Intake.MOTOR_PORT, Constants.Intake.MOTOR_CAN_BUS, "Intake Motor");
    private final SparkMaxLance motor = new SparkMaxLance(Constants.Intake.MOTOR_PORT, Constants.Intake.MOTOR_CAN_BUS, "Intake Motor");
    //private final TalonFXLance bottomMotor = new TalonFXLance(12, Constants.ROBORIO, "Bottom Motor");

    private final double GEAR_RATIO = 1.0 / 5.0; // previously 1.0 / 25.0
    private final double WHEEL_DIAMETER_FEET = 2.25 / 12.0;
    private final double MINUTES_TO_SECONDS = 1.0 / 60.0;
    private final double RPM_TO_FPS = GEAR_RATIO * MINUTES_TO_SECONDS * Math.PI * WHEEL_DIAMETER_FEET;
    // private final double PERCENT_VOLTAGE = 0.9;
    // private final double VOLTAGE = PERCENT_VOLTAGE * Constants.END_OF_MATCH_BATTERY_VOLTAGE;
    private final double DEFAULT_SPEED = 0.9;

    private double rollerPosition = 0.0;
    private double rollerVelocity;

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
        motor.setupFactoryDefaults();
        //bottomMotor.setupFactoryDefaults();
        // Do Not Invert Motor Direction
        motor.setupInverted(true); // set to true
        // Set Coast Mode
        motor.setupBrakeMode();

        // motor.setupVelocityConversionFactor(RPM_TO_FPS);

        // motor.setupCurrentLimit(30.0, 35.0, 0.5);
        motor.setSafetyEnabled(false);
        //bottomMotor.setSafetyEnabled(false);
    }

    // public double getPosition() 
    // {
    //     return rollerPosition;
    // }

    // public double getVelocity() 
    // {
    //     return rollerVelocity;
    // }

    private void set(double speed) 
    {
        motor.set(speed);
    }

    private void setVoltage(double voltage) 
    {
        motor.setVoltage(voltage);
    }

    public void pickupCoral() 
    {
        set(0.9);
    }

    public void ejectCoral() 
    {
        set(-0.9);
    }

    public void pickupAlgae()
    {
        set(-0.4);
    }

    public void ejectAlgae()
    {
        set(0.4);
    }

    // public void pulse()
    // {
    //     for(int i = 0; )
    // }

    public void stop() 
    {
        set(0.0);
    }

    public Command pickupCoralCommand() 
    {
        return runOnce(() -> pickupCoral()).withName("Pickup Coral");
    }

    public Command ejectCoralCommand() 
    {
        return runOnce(() -> ejectCoral()).withName("Eject Coral");
    }

    public Command pickupAlgaeCommand()
    {
        return runOnce(() -> pickupAlgae()).withName("Pickup Algae");
    }

    public Command ejectAlgaeCommand()
    {
        return runOnce(() -> ejectAlgae()).withName("Eject Algae");
    }

    public Command stopCommand() 
    {
        return runOnce(() -> stop()).withName("Stop");
    }


    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here

    @Override
    public void periodic() 
    {
        // This method will be called once per scheduler run
        //rollerPosition = motor.getPosition();
        //periodicData.bottomRollerPosition = bottomMotor.getPosition();
        //rollerVelocity = motor.getVelocity();
        //periodicDatopRollerVelocityta.bottomRollerVelocity = bottomMotor.getVelocity();
    }

    @Override
    public String toString() 
    {
        return "Current Intake Position: " + rollerPosition;
    }
}