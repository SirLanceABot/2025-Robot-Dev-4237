package frc.robot.subsystems;


import java.lang.invoke.MethodHandles;

import frc.robot.Constants;
import frc.robot.motors.TalonFXLance;

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
    // Put all inner enums and inner classes here
    private class PeriodicData
    {
        // INPUTS

        // OUTPUTS
        private double speed;
    }


    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final PeriodicData periodicData = new PeriodicData();
    private final TalonFXLance topMotor = new TalonFXLance(4, Constants.ROBORIO, "Top Motor");
    private final TalonFXLance bottomMotor = new TalonFXLance(12, Constants.ROBORIO, "Bottom Motor");


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
        topMotor.setupFactoryDefaults();
        bottomMotor.setupFactoryDefaults();
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
        bottomMotor.set(periodicData.speed);
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