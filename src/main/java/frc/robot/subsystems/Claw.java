package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.motors.SparkMaxLance;
import frc.robot.motors.TalonFXLance;

/**
 * Use this class as a template to create other subsystems.
 */
public class Claw extends SubsystemLance
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
    private class PeriodicData
    {
        // INPUTS
        // private double motorSpeed = 0.0;

        // OUTPUTS
        private double speed;
    }


    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final PeriodicData periodicData = new PeriodicData();
    private final SparkMaxLance topMotor = new SparkMaxLance(Constants.Claw.TOP_MOTOR_PORT, Constants.Claw.TOP_MOTOR_CAN_BUS, "Top Claw Motor");
    private final SparkMaxLance lowerMotor = new SparkMaxLance(Constants.Claw.BOTTOM_MOTOR_PORT, Constants.Claw.BOTTOM_MOTOR_CAN_BUS, "Lower Claw Motor2");

    // private final TalonFXLance motor1 = new TalonFXLance(4, Constants.ROBORIO, "Motor 1");
    // private final TalonFXLance motor2 = new TalonFXLance(12, Constants.ROBORIO, "Motor 2");


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /** 
     * Creates a new Claw. 
     */
    public Claw()
    {
        super("Example Subsystem");
        System.out.println("  Constructor Started:  " + fullClassName);

        configMotors();

        // SendableRegistry.addLW(this, "Example Subsystem", "MY Subsystem");
        // addChild("Motor 1", motor1);
        // addChild("Motor 2", motor2);

        System.out.println("  Constructor Finished: " + fullClassName);
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    private void configMotors()
    {
        topMotor.setupFactoryDefaults();
        lowerMotor.setupFactoryDefaults();
    }

    /**
     * Returns the value of the sensor
    * @return The value of periodData.sensorValue
    */
    public void set(double speed)
    {
        periodicData.speed = speed;
    }

    public void stop()
    {
        periodicData.speed = 0.0;
    }

    /* 
     * Turns on motors to place coral on reef
     */
    // * different speed and function for L4?

    public void placeCoral(double speed)
    {
        topMotor.set(speed);
        lowerMotor.set(speed);
    }

    /* 
     * Turns on motors to set speed to release coral from the claw
     * redundant with place coral
     */
    public void ejectCoral(double speed)
    {
        topMotor.set(speed);
        lowerMotor.set(speed);
    }

    public Command placeCoralCommand()
    {
        return Commands.run(() -> placeCoral(0.5), this).withName("Place Coral");
    }

    public Command ejectCoralCommand()
    {
        return Commands.run(() -> ejectCoral(0.5), this).withName("Eject Coral");
    }    
    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here
    @Override
    public void readPeriodicInputs()
    {

    }

    @Override
    public void writePeriodicOutputs()
    {
        topMotor.set(periodicData.speed);
        lowerMotor.set(periodicData.speed);
    }

    @Override
    public void periodic()
    {
        // This method will be called once per scheduler run
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
