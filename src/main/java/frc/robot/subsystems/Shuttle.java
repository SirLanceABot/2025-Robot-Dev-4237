package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.motors.TalonFXLance;

/**
 * Use this class as a template to create other subsystems.
 */
public class Shuttle extends SubsystemLance
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

    

    // *** CLASS VARIABLES & INSTANCE VARIABLES ***
    // Put all class variables and instance variables here
    private final TalonFXLance motor = new TalonFXLance(Constants.Shuttle.MOTOR_PORT, Constants.Shuttle.MOTOR_CAN_BUS, "Shuttle Motor");
    private double speed;
    // private final TalonFXLance motor2 = new TalonFXLance(12, Constants.ROBORIO, "Motor 2");


    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /** 
     * Creates a new ExampleSubsystem. 
     */
    public Shuttle()
    {
        super("Shuttle");
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
        motor.setupFactoryDefaults();
        motor.setupCoastMode();
    }

    /**
     * Sets motor to move shuttle forward
     */
    private void moveForward()
    {
        motor.set(0.5);
    }

    /**
     * Sets motor to move shuttle backward
     */
    private void moveBackward()
    {
        motor.set(-0.5);
    }
 
     /**
     * Sets motor to stop shuttle 
     */
    private void stop()
    {
       motor.set(0.0);
    }

    /**
     * runs move forward method
     */
    public Command moveForwardCommand()
    {
        return Commands.run(() -> moveForward(), this).withName("Move Forward");
    }

    /**
     * runs move backwards method
     */
    public Command moveBackwardCommand()
    {
        return Commands.run(() -> moveBackward(), this).withName("Move Backward");
    }

    /**
     * runs shuttle stop method
     */
    public Command stopCommand()
    {
        return Commands.run(() -> stop(), this).withName("Stop");
    }





    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here

    @Override
    public void periodic()
    {
        // This method will be called once per scheduler run
    }

    @Override
    public String toString()
    {
        return "Shuttle speed:" + motor.get();
    }
}

