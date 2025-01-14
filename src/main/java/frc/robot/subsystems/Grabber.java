package frc.robot.subsystems;

import java.lang.invoke.MethodHandles;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Constants;
import frc.robot.motors.SparkMaxLance;

/**
 * This class creates the grabber subsystem and setsup related practice commands
 */
public class Grabber extends SubsystemLance
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

    private final SparkMaxLance frontMotor = new SparkMaxLance(Constants.Grabber.FRONT_MOTOR_PORT, Constants.Grabber.FRONT_MOTOR_CAN_BUS, "Front Grabber Motor");
    private final SparkMaxLance backMotor = new SparkMaxLance(Constants.Grabber.BACK_MOTOR_PORT, Constants.Grabber.BACK_MOTOR_CAN_BUS, "Back Grabber Motor");

    // *** CLASS CONSTRUCTORS ***
    // Put all class constructors here

    /** 
     * Creates a new Grabber and congures the motors. 
     */
    public Grabber()
    {
        super("Grabber");
        System.out.println("  Constructor Started:  " + fullClassName);

        configMotors();

        System.out.println("  Constructor Finished: " + fullClassName);
    }


    // *** CLASS METHODS & INSTANCE METHODS ***
    // Put all class methods and instance methods here

    private void configMotors()
    {
        frontMotor.setupFactoryDefaults();
        backMotor.setupFactoryDefaults();
        frontMotor.setupCoastMode();
        backMotor.setupCoastMode();
    }

    /**
     *Sets speed of front motor
    */
    public void setFront(double speed)
    {
        frontMotor.set(speed);
    }

    /**
     *Sets speed of back motor
    */
    public void setBack(double speed)
    {
        backMotor.set(speed);
    }

    /** 
     * Sets speed of front and back motor to place coral
    */
    public void placeCoral(double backSpeed, double frontSpeed)
    {
        setFront(frontSpeed);
        setBack(backSpeed);
    }

    /** 
    * Sets speed of front and back motor to accept coral (speed is automaticly reversed)
    */
    public void acceptCoral(double backSpeed, double frontSpeed)
    {
        setFront(-frontSpeed);
        setBack(-backSpeed);
    }

    
    public void eject(double speed)
    {
        setFront(speed);
    }

    public void stop()
    {
        setFront(0.0);
        setBack(0.0);
    }

    /**
     * runs accept coral method 
     * */

    public Command acceptCoralCommand()
    {
        // design pending
        return Commands.run(() -> acceptCoral(0.075, 0.075), this).withName("Accept Coral");
    }

    /**
     * runs place coral method
    */
    public Command placeCoralCommand()
    {
        return Commands.run(() -> placeCoral(0.075, 0.075), this).withName("Place Coral");
    }

    /**
     * runs eject coral method
     */
    public Command ejectCoralCommand()
    {
        return Commands.run(() -> eject(0.075), this).withName("Eject Coral");
    }   
    
    /**
     * runs stop command
     * 
     */
    public Command stopCommand()
    {
        return Commands.run(() -> stop(), this).withName("Stop");
    }
    // *** OVERRIDEN METHODS ***
    // Put all methods that are Overridden here

    @Override
    public String toString()
    {
        return "Grabber Top Motor Speed: " + frontMotor.get() + "Grabber Lower Motor Speed: " + backMotor.get();
    }
}
